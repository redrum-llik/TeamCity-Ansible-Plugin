package jetbrains.buildServer.agent.ansibleRunner

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.agent.AgentLifeCycleAdapter
import jetbrains.buildServer.agent.AgentLifeCycleListener
import jetbrains.buildServer.agent.BuildAgent
import jetbrains.buildServer.agent.BuildAgentConfiguration
import jetbrains.buildServer.agent.ansibleRunner.detect.AnsibleDetector
import jetbrains.buildServer.agent.ansibleRunner.detect.AnsibleInstance
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants.AGENT_PARAM_ANSIBLE_PREFIX
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants.AGENT_PARAM_CONFIG_FILE_POSTFIX
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants.AGENT_PARAM_PATH_POSTFIX
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants.AGENT_PARAM_PY_VERSION_POSTFIX
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants.AGENT_PARAM_VERSION_POSTFIX
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants.PARAM_SEARCH_PATH
import jetbrains.buildServer.util.EventDispatcher

class AnsibleAgentInfoProvider(
    private val myConfig: BuildAgentConfiguration,
    events: EventDispatcher<AgentLifeCycleListener>,
    detectors: List<AnsibleDetector>
) {
    private val myHolder = AnsibleInstancesHolder()
    private val LOG = Logger.getInstance(this.javaClass.name)

    init {
        events.addListener(
            object : AgentLifeCycleAdapter() {
                override fun afterAgentConfigurationLoaded(agent: BuildAgent) {
                    registerDetectedAnsibleInstances(detectors, agent.configuration)
                }
            }
        )
    }

    private fun registerDetectedAnsibleInstances(
        detectors: List<AnsibleDetector>,
        configuration: BuildAgentConfiguration?
    ) {
        for (detector in detectors) {
            LOG.debug("Detecting Ansible with ${detector.javaClass.name}.")
            for (entry in detector.detectAnsibleInstances(configuration!!).entries) {
                LOG.debug("Processing detected Ansible instance [${entry.key}][${entry.value.version}]")
                myHolder.addInstance(entry.key, entry.value)
            }
        }

        if (!myHolder.isEmpty()) {
            registerMainInstance(myHolder.getMainInstance())
            registerInstances(myHolder.getInstances())
        } else {
            LOG.info(
                "No Ansible instance detected. If it is not available on PATH, " +
                        "please provide a custom path with $PARAM_SEARCH_PATH agent property."
            )
        }
    }

    private fun registerInstances(instances: java.util.HashMap<String, AnsibleInstance>) {
        for (instance in instances.values) {
            val ansibleVersionedConfigPathName = "${AGENT_PARAM_ANSIBLE_PREFIX}_${instance.version}_${AGENT_PARAM_CONFIG_FILE_POSTFIX}"
            if (!instance.configFile.isNullOrEmpty()) {
                myConfig.addConfigurationParameter(ansibleVersionedConfigPathName, instance.configFile)
            }

            val ansibleVersionedPathName = "${AGENT_PARAM_ANSIBLE_PREFIX}_${instance.version}_${AGENT_PARAM_PATH_POSTFIX}"
            myConfig.addConfigurationParameter(ansibleVersionedPathName, instance.executablePath)

            val ansibleVersionedPyVersionName = "${AGENT_PARAM_ANSIBLE_PREFIX}_${instance.version}_${AGENT_PARAM_PY_VERSION_POSTFIX}"
            if (!instance.pythonVersion.isNullOrEmpty()) {
                myConfig.addConfigurationParameter(ansibleVersionedPyVersionName, instance.pythonVersion)
            }
        }
    }

    private fun registerMainInstance(mainInstance: AnsibleInstance) {
        val ansibleVersionParameterName = "${AGENT_PARAM_ANSIBLE_PREFIX}_${AGENT_PARAM_VERSION_POSTFIX}"
        myConfig.addConfigurationParameter(ansibleVersionParameterName, mainInstance.version)

        val ansiblePathParameterName = "${AGENT_PARAM_ANSIBLE_PREFIX}_${AGENT_PARAM_PATH_POSTFIX}"
        myConfig.addConfigurationParameter(ansiblePathParameterName, mainInstance.executablePath)
    }

    companion object {
        class AnsibleInstancesHolder {
            private val myInstances = HashMap<String, AnsibleInstance>()

            fun addInstance(path: String, ansibleInstance: AnsibleInstance) {
                myInstances[path] = ansibleInstance
            }

            fun getInstances(): HashMap<String, AnsibleInstance> {
                return myInstances
            }

            fun isEmpty(): Boolean {
                return myInstances.isEmpty()
            }

            fun getMainInstance(): AnsibleInstance {
                return myInstances.values.maxOrNull()!!
            }
        }
    }
}