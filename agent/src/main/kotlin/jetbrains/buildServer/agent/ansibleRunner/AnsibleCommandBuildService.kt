package jetbrains.buildServer.agent.ansibleRunner

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.agent.runner.BuildServiceAdapter
import jetbrains.buildServer.agent.runner.ProgramCommandLine
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine
import jetbrains.buildServer.runner.ansible.AnsibleRunnerInstanceConfiguration
import java.util.*
import kotlin.collections.ArrayList

class AnsibleCommandBuildService : BuildServiceAdapter() {
    private val LOG = Logger.getInstance(this.javaClass.name)

    override fun makeProgramCommandLine(): ProgramCommandLine {
        val config = AnsibleRunnerInstanceConfiguration(runnerParameters)
        LOG.debug("Going to execute Ansible runner with following parameters: $config")

        return makePlaybookCall(config, environmentVariables, workingDirectory.path)
    }

    private fun makePlaybookCall(
        config: AnsibleRunnerInstanceConfiguration,
        environment: Map<String, String>,
        workingDirectory: String
    ): SimpleProgramCommandLine {
        val arguments = prepareArguments(config)
        val patchedEnvironment = prepareEnvironment(environment)

        return SimpleProgramCommandLine(
            patchedEnvironment,
            workingDirectory,
            AnsibleCommandLineConstants.ANSIBLE_PLAYBOOK_COMMAND,
            arguments
        )
    }

    private fun addArgument(
        arguments: ArrayList<String>, argName: String? = null, value: String? = null
    ) {
        when {
            argName.isNullOrEmpty() && value.isNullOrEmpty() -> {
                return
            }
            argName.isNullOrEmpty() -> {
                arguments.add(value as String)
            }
            value.isNullOrEmpty() -> {
                arguments.add(argName)
            }
            else -> {
                arguments.add(argName)
                arguments.add(value)
            }
        }
    }

    private fun prepareArguments(config: AnsibleRunnerInstanceConfiguration): ArrayList<String> {
        val arguments = ArrayList<String>()

        val inventory = config.getInventory()
        if (!inventory.isNullOrEmpty()) {
            addArgument(arguments, AnsibleCommandLineConstants.INVENTORY_PARAM, inventory)
        }

        val extraArgs = config.getExtraArgs()
        if (!extraArgs.isNullOrEmpty()) {
            addArgument(arguments, value = extraArgs)
        }

        val playbook = config.getPlaybook()
        if (!playbook.isNullOrEmpty()) {
            addArgument(arguments, value = playbook)
        }

        return arguments
    }

    private fun addEnvironmentVariable(
        environment: MutableMap<String, String>, varName: String, value: String = ""
    ) {
        if (environment.containsKey(varName)) {
            LOG.warn("Overriding environment variable $varName with value $value")
        }
        else {
            LOG.debug("Adding environment variable $varName: $value")
        }
        environment[varName] = value
    }

    private fun prepareEnvironment(environment: Map<String, String>): Map<String, String> {
        return environment
    }
}