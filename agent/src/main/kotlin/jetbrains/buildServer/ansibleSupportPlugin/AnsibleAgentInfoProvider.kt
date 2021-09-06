package jetbrains.buildServer.ansibleSupportPlugin

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.agent.AgentLifeCycleAdapter
import jetbrains.buildServer.agent.AgentLifeCycleListener
import jetbrains.buildServer.agent.BuildAgent
import jetbrains.buildServer.agent.BuildAgentConfiguration
import jetbrains.buildServer.ansibleSupportPlugin.callback.AnsibleCallbackProvider
import jetbrains.buildServer.ansibleSupportPlugin.AnsibleFeatureConstants as CommonConst
import jetbrains.buildServer.util.EventDispatcher

class AnsibleAgentInfoProvider(
    private val myConfig: BuildAgentConfiguration,
    events: EventDispatcher<AgentLifeCycleListener>,
    providers: List<AnsibleCallbackProvider>
) {
    private val LOG = Logger.getInstance(this.javaClass.name)

    init {
        events.addListener(
            object : AgentLifeCycleAdapter() {
                override fun afterAgentConfigurationLoaded(agent: BuildAgent) {
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
}