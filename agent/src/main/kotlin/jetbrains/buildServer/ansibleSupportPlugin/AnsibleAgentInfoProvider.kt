package jetbrains.buildServer.ansibleSupportPlugin

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.agent.AgentLifeCycleAdapter
import jetbrains.buildServer.agent.AgentLifeCycleListener
import jetbrains.buildServer.agent.BuildAgent
import jetbrains.buildServer.agent.BuildAgentConfiguration
import jetbrains.buildServer.ansibleSupportPlugin.callback.AnsibleCallbackProvider
import jetbrains.buildServer.ansibleSupportPlugin.detect.AnsibleDetector
import jetbrains.buildServer.ansibleSupportPlugin.detect.AnsibleInstance
import jetbrains.buildServer.ansibleSupportPlugin.AnsibleFeatureConstants as CommonConst
import jetbrains.buildServer.util.EventDispatcher

class AnsibleAgentInfoProvider(
    private val myConfig: BuildAgentConfiguration,
    events: EventDispatcher<AgentLifeCycleListener>,
    detectors: List<AnsibleDetector>,
    providers: List<AnsibleCallbackProvider>
) {
    private val myHolder = AnsibleInstancesHolder()
    private val LOG = Logger.getInstance(this.javaClass.name)

    init {
        events.addListener(
            object : AgentLifeCycleAdapter() {
                override fun afterAgentConfigurationLoaded(agent: BuildAgent) {
                    registerDetectedAnsibleInstances(detectors, agent.configuration)
                    registerAgentSideCallbackFolder(providers, agent.configuration)
                }
            }
        )
    }

    private fun registerAgentSideCallbackFolder(
        providers: List<AnsibleCallbackProvider>,
        configuration: BuildAgentConfiguration?
    ) {
        val paths = ArrayList<String>()
        for (provider in providers) {
            LOG.debug("Fetching callback path from ${provider.javaClass.name}.")
            paths.add(provider.getCallbackFolderPath())
        }
        configuration!!.addConfigurationParameter(
            CommonConst.AGENT_PARAM_ANSIBLE_CALLBACK_PATH,
            paths.joinToString(",")
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
                        "please provide a custom path with ${CommonConst.BUILD_PARAM_SEARCH_PATH} agent property."
            )
        }
    }

    private fun registerInstances(instances: java.util.HashMap<String, AnsibleInstance>) {
        for (instance in instances.values) {
            LOG.info("Registering detected Ansible instance at ${instance.executablePath}")

            val ansibleVersionedConfigPathName = getVersionedConfigVarName(instance.version)
            if (!instance.configFile.isNullOrEmpty()) {
                myConfig.addConfigurationParameter(ansibleVersionedConfigPathName, instance.configFile)
            }

            val ansibleVersionedPathName = getVersionedPathVarName(instance.version)
            myConfig.addConfigurationParameter(ansibleVersionedPathName, instance.executablePath)

            val ansibleVersionedPyVersionName = getVersionedPyVersionVarName(instance.version)
            if (!instance.pythonVersion.isNullOrEmpty()) {
                myConfig.addConfigurationParameter(ansibleVersionedPyVersionName, instance.pythonVersion)
            }
        }
    }

    private fun registerMainInstance(mainInstance: AnsibleInstance) {
        LOG.info("Registering detected Ansible instance at ${mainInstance.executablePath} as main instance")

        myConfig.addConfigurationParameter(CommonConst.AGENT_PARAM_ANSIBLE_VERSION, mainInstance.version)
        myConfig.addConfigurationParameter(CommonConst.AGENT_PARAM_ANSIBLE_PATH, mainInstance.executablePath)
        myConfig.addConfigurationParameter(CommonConst.AGENT_PARAM_ANSIBLE_PY_VERSION, mainInstance.pythonVersion)
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