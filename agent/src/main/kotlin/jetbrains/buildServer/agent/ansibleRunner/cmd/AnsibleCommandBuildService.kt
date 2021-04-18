package jetbrains.buildServer.agent.ansibleRunner.cmd

import com.google.gson.Gson
import jetbrains.buildServer.agent.runner.BuildServiceAdapter
import jetbrains.buildServer.agent.runner.ProgramCommandLine
import jetbrains.buildServer.runner.ansible.AnsiblePlaybookType
import jetbrains.buildServer.runner.ansible.AnsibleRunnerInstanceConfiguration
import java.io.File
import java.io.FileWriter
import java.nio.file.NoSuchFileException
import java.util.*
import jetbrains.buildServer.runner.ansible.AnsibleCommandLineConstants as RunnerConst
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants as CommonConst

class AnsibleCommandBuildService : BuildServiceAdapter() {

    override fun makeProgramCommandLine(): ProgramCommandLine {
        val config = AnsibleRunnerInstanceConfiguration(runnerParameters)
        val builder = CommandLineBuilder(logger)
        prepareArguments(config, builder)
        prepareEnvironment(config, builder)
        builder.executablePath = getExecutablePath()
        builder.workingDir = runnerContext.workingDirectory.path

        return builder.build()
    }

    private fun getExecutablePath(): String {
        if (runnerContext.isVirtualContext) {
            return RunnerConst.COMMAND_ANSIBLE_PLAYBOOK
        }
        return File(
            configParameters[CommonConst.AGENT_PARAM_ANSIBLE_PATH]!!,
            RunnerConst.COMMAND_ANSIBLE_PLAYBOOK
        ).absolutePath
    }

    private fun getPlaybookFilePath(
        config: AnsibleRunnerInstanceConfiguration
    ): String {
        return when (config.getPlaybookMode()) {
            AnsiblePlaybookType.FILE -> config.getPlaybookFilePath()!!
            AnsiblePlaybookType.YAML -> savePlaybookToFile(config)
        }
    }

    private fun savePlaybookToFile(
        config: AnsibleRunnerInstanceConfiguration
    ): String {
        val playbook = config.getPlaybookYaml()
        val playbookFile = File(agentTempDirectory.absolutePath, "custom_playbook_${UUID.randomUUID()}.yml")
        playbookFile.writeText(playbook!!)
        return playbookFile.normalize().absolutePath
    }

    private fun formatSystemProperties(): MutableMap<String, String> {
        val result: MutableMap<String, String> = HashMap()
        for (parameter in systemProperties) {
            // Ansible variables cannot include dots
            val newKey = parameter.key.replace(".", "_")
            result[newKey] = parameter.value
        }
        return result
    }

    private fun saveArgumentsToFile(): String {
        val varFile = File(
            agentTempDirectory.absolutePath,
            "ansible_varfile_${UUID.randomUUID()}.json"
        ).normalize()
        val writer = FileWriter(varFile)
        val json = Gson().toJson(
            formatSystemProperties()
        )
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

        val additionalArgs = config.getAdditionalArgs()
        if (!additionalArgs.isNullOrEmpty()) {
            builder.addArgument(value = additionalArgs)
        }

        if (config.getPassSystemParams()) {
            builder.addArgument(
                RunnerConst.PARAM_EXTRA_VARS,
                saveArgumentsToFile()
            )

        }

        if (config.getIsFailOnChanges()) {
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
            logger.debug("Forcing colored build log for Ansible playbook execution")
            builder.addEnvironmentVariable(
                RunnerConst.ENV_FORCE_COLOR,
                true.toString()
            )
        }

        if (config.getIsFailOnChanges()) {
            logger.debug("Will fail build if any changes are detected")
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

