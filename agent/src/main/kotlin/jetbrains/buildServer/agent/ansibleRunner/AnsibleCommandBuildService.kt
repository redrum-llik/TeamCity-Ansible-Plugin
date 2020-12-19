package jetbrains.buildServer.agent.ansibleRunner

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.agent.runner.BuildServiceAdapter
import jetbrains.buildServer.agent.runner.ProgramCommandLine
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine
import jetbrains.buildServer.runner.ansible.AnsibleRunnerInstanceConfiguration
import java.util.*

class AnsibleCommandBuildService : BuildServiceAdapter() {
    private val LOG = Logger.getInstance(this.javaClass.name)

    override fun makeProgramCommandLine(): ProgramCommandLine {
        val config = AnsibleRunnerInstanceConfiguration(runnerParameters)
        LOG.debug("Going to execute Ansible runner with following parameters: $config")

        return makePlaybookCall(config, environmentVariables, workingDirectory.path)
    }

    fun makePlaybookCall(
        config: AnsibleRunnerInstanceConfiguration,
        environment: Map<String, String>,
        workingDirectory: String
    ): SimpleProgramCommandLine {
        val arguments = ArrayList<String>()
        arguments.add("Playbook file: ${config.getPlaybook()}")
        arguments.add("Inventory file: ${config.getInventory()}")
        arguments.add("Extra args: ${config.getExtraArgs()}")

        return SimpleProgramCommandLine(
            environment,
            workingDirectory,
            "echo",
            arguments
        )
    }
}