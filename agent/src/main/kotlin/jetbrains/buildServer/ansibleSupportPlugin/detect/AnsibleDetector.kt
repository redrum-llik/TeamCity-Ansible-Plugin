package jetbrains.buildServer.ansibleSupportPlugin.detect

import jetbrains.buildServer.agent.BuildAgentConfiguration

interface AnsibleDetector {
    fun detectAnsibleInstances(buildAgentConfiguration: BuildAgentConfiguration): MutableMap<String, AnsibleInstance>
}