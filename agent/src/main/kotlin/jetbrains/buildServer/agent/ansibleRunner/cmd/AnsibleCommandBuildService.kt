package jetbrains.buildServer.agent.ansibleRunner.cmd

import com.google.gson.Gson
import com.intellij.openapi.diagnostic.Logger
import jetbrains.buildServer.agent.runner.BuildServiceAdapter
import jetbrains.buildServer.agent.runner.ProgramCommandLine
import jetbrains.buildServer.runner.ansible.AnsiblePlaybookType
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants as CommonConst
import jetbrains.buildServer.runner.ansible.AnsibleRunnerInstanceConfiguration
import java.io.File
import java.io.FileWriter
import java.nio.file.NoSuchFileException
import java.util.*
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

    private fun getPlaybookFilePath(
        config: AnsibleRunnerInstanceConfiguration
    ): String {
        return when (config.getPlaybookMode()) {
            AnsiblePlaybookType.File -> config.getPlaybookFilePath()!!
            AnsiblePlaybookType.YAML -> savePlaybookToFile(config)
        }
    }

    private fun savePlaybookToFile(
        config: AnsibleRunnerInstanceConfiguration
    ): String {
        val playbook = config.getPlaybookYaml()
        val playbookFile = File(buildTempDirectory.absolutePath, "custom_playbook_${UUID.randomUUID()}.yml")
        playbookFile.writeText(playbook!!)
        return playbookFile.normalize().absolutePath
    }

    private fun saveArgumentsToFile(
    ): String {
        val gson = Gson()
        val varFile = File(
            buildTempDirectory.absolutePath,
            "ansible_varfile_${UUID.randomUUID()}.json"
        ).normalize()
        val writer = FileWriter(varFile)
        val json = gson.toJson(configParameters)
        writer.run {
            write(json)
            close()
        }

        return "@${varFile.absolutePath}"
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

        val doPassConfigParams = config.getDoPassConfigParams()
        if (doPassConfigParams) {
            builder.addArgument(
                RunnerConst.PARAM_EXTRA_VARS,
                saveArgumentsToFile()
            )
        }

        if (config.getIsDryRun()) {
            builder.addArgument(RunnerConst.PARAM_CHECK)
        }

        val playbookPath = getPlaybookFilePath(config)
        builder.addArgument(value = playbookPath)

        return builder
    }

    private fun getNewCallbackPluginPaths(environment: Map<String, String>, callbackFolderPath: String?): String {
        var currentCallbackPluginPaths = environment[RunnerConst.ENV_DEFAULT_CALLBACK_PLUGIN_PATH]
        return when {
            callbackFolderPath.isNullOrEmpty() -> {
                throw NoSuchFileException("Runner expects custom callback path in order to process the Ansible output")
            }
            currentCallbackPluginPaths == null -> {
                "$callbackFolderPath:${RunnerConst.ENV_DEFAULT_CALLBACK_PLUGIN_PATH_VALUE}"
            }
            currentCallbackPluginPaths.isEmpty() -> {
                callbackFolderPath
            }
            else -> {
                "$callbackFolderPath:$currentCallbackPluginPaths"
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

