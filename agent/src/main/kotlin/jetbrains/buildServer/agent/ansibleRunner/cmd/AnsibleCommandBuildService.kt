package jetbrains.buildServer.agent.ansibleRunner.cmd

import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.agent.runner.BuildServiceAdapter
import jetbrains.buildServer.agent.runner.ProgramCommandLine
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants as CommonConst
import jetbrains.buildServer.runner.ansible.AnsibleRunnerInstanceConfiguration
import java.io.File
import java.nio.file.NoSuchFileException
import jetbrains.buildServer.agent.ansibleRunner.AnsibleCommandLineConstants as RunnerConst

class AnsibleCommandBuildService : BuildServiceAdapter() {
    private val LOG = Logger.getInstance(this.javaClass.name)

    override fun makeProgramCommandLine(): ProgramCommandLine {
        val config = AnsibleRunnerInstanceConfiguration(runnerParameters)
        LOG.debug("Going to execute Ansible runner with following parameters: $config")

        val builder = CommandLineBuilder()
        prepareArguments(config, builder)
        prepareEnvironment(config, builder)
        builder.executablePath = File(
            configParameters[CommonConst.AGENT_PARAM_ANSIBLE_PATH]!!, RunnerConst.COMMAND_ANSIBLE_PLAYBOOK
        ).absolutePath
        builder.workingDir = workingDirectory.path

        return builder.build()
    }

    private fun prepareArguments(
        config: AnsibleRunnerInstanceConfiguration,
        builder: CommandLineBuilder
    ): CommandLineBuilder {
        val inventory = config.getInventory()
        if (!inventory.isNullOrEmpty()) {
            builder.addArgument(RunnerConst.PARAM_INVENTORY, inventory)
        }

        val extraArgs = config.getExtraArgs()
        if (!extraArgs.isNullOrEmpty()) {
            builder.addArgument(value = extraArgs)
        }

        if (config.getIsDryRun()) {
            builder.addArgument(RunnerConst.PARAM_CHECK)
        }

        val playbook = config.getPlaybook()
        if (!playbook.isNullOrEmpty()) {
            builder.addArgument(value = playbook)
        }

        return builder
    }

    private fun getNewCallbackPluginPaths(environment: Map<String, String>, callbackFolderPath: String?): String {
        var currentCallbackPluginPaths = environment[RunnerConst.ENV_DEFAULT_CALLBACK_PLUGIN_PATH]
        return when {
            callbackFolderPath.isNullOrEmpty() -> {
                throw NoSuchFileException("Runner expects custom callback path in order to process the Ansible output")
            }
            currentCallbackPluginPaths == null -> {
                "${RunnerConst.ENV_DEFAULT_CALLBACK_PLUGIN_PATH_VALUE},$callbackFolderPath"
            }
            currentCallbackPluginPaths.isEmpty() -> {
                callbackFolderPath
            }
            else -> {
                "$currentCallbackPluginPaths,$callbackFolderPath"
            }
        }
    }

    private fun prepareEnvironment(
        config: AnsibleRunnerInstanceConfiguration,
        builder: CommandLineBuilder
    ): CommandLineBuilder {
        builder.setEnvironment(environmentVariables)

        if (config.getIsLogColored()) {
            LOG.debug("Forcing colored build log for Ansible playbook execution")
            builder.addEnvironmentVariable(
                RunnerConst.ENV_FORCE_COLOR,
                true.toString()
            )
        }

        if (config.getIsFailOnChanges()) {
            LOG.debug("Will fail build if any changes are detected")
            builder.addEnvironmentVariable(
                RunnerConst.ENV_FAIL_ON_CHANGES,
                true.toString()
            )
        }

        builder.addEnvironmentVariable(
            RunnerConst.ENV_STDOUT_CALLBACK,
            RunnerConst.CALLBACK_NAME
        )

        builder.addEnvironmentVariable(
            RunnerConst.ENV_DEFAULT_CALLBACK_PLUGIN_PATH,
            getNewCallbackPluginPaths(
                environmentVariables,
                configParameters[CommonConst.AGENT_PARAM_ANSIBLE_CALLBACK_PATH]
            )
        )

        return builder
    }
}

