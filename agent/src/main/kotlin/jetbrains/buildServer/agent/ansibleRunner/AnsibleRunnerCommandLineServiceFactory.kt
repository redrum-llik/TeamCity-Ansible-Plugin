package jetbrains.buildServer.agent.ansibleRunner

import jetbrains.buildServer.agent.AgentBuildRunnerInfo
import jetbrains.buildServer.agent.BuildAgentConfiguration
import jetbrains.buildServer.agent.runner.CommandLineBuildService
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants

class AnsibleRunnerCommandLineServiceFactory : CommandLineBuildServiceFactory {
    override fun createService(): CommandLineBuildService {
        return AnsibleCommandBuildService()
    }

    override fun getBuildRunnerInfo(): AgentBuildRunnerInfo {
        return AgentAnsibleRunnerInfo()
    }

    companion object {
        class AgentAnsibleRunnerInfo : AgentBuildRunnerInfo {
            override fun getType(): String {
                return AnsibleRunnerConstants.RUNNER_TYPE
            }

            override fun canRun(buildAgentConfiguration: BuildAgentConfiguration): Boolean {
                return true
            }
        }
    }
}