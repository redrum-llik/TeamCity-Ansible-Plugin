package jetbrains.buildServer.agent.ansibleRunner

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.agent.runner.BuildServiceAdapter
import jetbrains.buildServer.agent.runner.ProgramCommandLine
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine
import jetbrains.buildServer.agent.ansibleRunner.AnsibleCommandLineConstants as RunnerConst
import jetbrains.buildServer.runner.ansible.AnsibleRunnerInstanceConfiguration
import java.util.*
import kotlin.collections.ArrayList

class AnsibleCommandBuildService : BuildServiceAdapter() {
    private val LOG = Logger.getInstance(this.javaClass.name)

    override fun makeProgramCommandLine(): ProgramCommandLine {
        val config = AnsibleRunnerInstanceConfiguration(runnerParameters)
        LOG.debug("Going to execute Ansible runner with following parameters: $config")

        val arguments = prepareArguments(config)
        val patchedEnvironment = prepareEnvironment(environmentVariables, config)

        return SimpleProgramCommandLine(
            patchedEnvironment,
            workingDirectory.path,
            RunnerConst.COMMAND_ANSIBLE_PLAYBOOK,
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
            addArgument(arguments, RunnerConst.PARAM_INVENTORY, inventory)
        }

        val extraArgs = config.getExtraArgs()
        if (!extraArgs.isNullOrEmpty()) {
            addArgument(arguments, value = extraArgs)
        }

        if (config.getIsDryRun()) {
            addArgument(arguments, RunnerConst.PARAM_CHECK)
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

    private fun prepareEnvironment(
        environment: Map<String, String>,
        config: AnsibleRunnerInstanceConfiguration
    ): MutableMap<String, String> {
        val patchedEnvironment = environment.toMutableMap()

        if (config.getIsLogColored()) {
            LOG.debug("Forcing colored build log for Ansible playbook execution")
            addEnvironmentVariable(
                patchedEnvironment,
                RunnerConst.ENV_FORCE_COLOR,
                true.toString()
            )
        }

        if (config.getIsFailOnChanges()) {
            LOG.debug("Will fail build if any changes are detected")
            addEnvironmentVariable(
                patchedEnvironment,
                RunnerConst.ENV_FAIL_ON_CHANGES,
                true.toString()
            )
        }

        LOG.debug("Overriding stdout callback via ${RunnerConst.ENV_STDOUT_CALLBACK} (${RunnerConst.CALLBACK_NAME})")
        addEnvironmentVariable(
            patchedEnvironment,
            RunnerConst.ENV_STDOUT_CALLBACK,
            RunnerConst.CALLBACK_NAME
        )

        return patchedEnvironment
    }
}