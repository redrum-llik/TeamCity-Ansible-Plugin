package jetbrains.buildServer.agent.ansibleRunner.detect

import jetbrains.buildServer.agent.BuildAgentConfiguration

interface AnsibleDetector {
    fun detectAnsibleInstances(buildAgentConfiguration: BuildAgentConfiguration): MutableMap<String, AnsibleInstance>
}