package jetbrains.buildServer.agent.ansibleRunner.detect

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessNotCreatedException
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.text.StringUtil
import jetbrains.buildServer.agent.BuildAgentConfiguration
import jetbrains.buildServer.agent.ansibleRunner.AnsibleCommandLineConstants as RunnerConst
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.HashMap


class AnsibleCommandLineDetector : AnsibleDetector {
    private val LOG = Logger.getInstance(this.javaClass.name)

    override fun detectAnsibleInstances(buildAgentConfiguration: BuildAgentConfiguration): MutableMap<String, AnsibleInstance> {
        val instances = HashMap<String, AnsibleInstance>()
        for (path in getSearchPaths(buildAgentConfiguration)) {
            val output = runDetectionCommand(path)
            if (output != null) {
                parseAnsibleInstance(instances, output, path)
            }
        }
        return instances
    }

    private fun getSearchPaths(buildAgentConfiguration: BuildAgentConfiguration): MutableList<SearchPath> {
        val searchPath = buildAgentConfiguration.configurationParameters[AnsibleRunnerConstants.BUILD_PARAM_SEARCH_PATH]
        val workDirPath = buildAgentConfiguration.workDirectory.toString()
        val result: MutableList<SearchPath> = ArrayList<SearchPath>()
        if (!searchPath.isNullOrBlank()) {
            result.add(SearchPath(searchPath))
        }
        result.add(SearchPath(workDirPath, true)) // if Ansible is available on PATH, any directory should be good to run detection
        return result
    }

    private fun runDetectionCommand(detectionPath: SearchPath): ProcessOutput? {
        val commandLine = GeneralCommandLine()
        commandLine.exePath = RunnerConst.COMMAND_ANSIBLE
        commandLine.addParameter(RunnerConst.PARAM_VERSION)
        commandLine.setWorkDirectory(detectionPath.path)

        LOG.debug("Detecting Ansible in: $detectionPath")
        return handleDetectionProcess(commandLine)
    }

    private fun logDetectionProcessOutput(output: ProcessOutput) {
        val stdOut = output.stdout.trim { it <= ' ' }
        val stdErr = output.stderr.trim { it <= ' ' }
        val b = StringBuilder("Ansible detection command output: \n")
        if (!StringUtil.isEmptyOrSpaces(stdOut)) {
            b.append("\n----- stdout: -----\n").append(stdOut).append("\n")
        }
        if (!StringUtil.isEmptyOrSpaces(stdErr)) {
            b.append("\n----- stderr: -----\n").append(stdErr).append("\n")
        }
        LOG.warn(b.toString())
    }

    private fun handleDetectionProcess(commandLine: GeneralCommandLine): ProcessOutput? {
        val output: ProcessOutput
        try {
            val handler = CapturingProcessHandler(commandLine.createProcess(), StandardCharsets.UTF_8)
            output = handler.runProcess()
            if (output == null) {
                return null
            }
        }
        catch (e: ProcessNotCreatedException) {
            return null
        }
        val errorOutput = output.stderr
        if (!errorOutput.isNullOrEmpty()) {
            logDetectionProcessOutput(output)
            return null
        }
        return output
    }

    companion object {
        private val pythonVersionPattern = "^([0-9.]*)".toRegex()

        data class SearchPath(
            val path: String,
            val isDefault: Boolean = false
        )

        fun parseAnsibleInstance(
            instances: HashMap<String, AnsibleInstance>,
            output: ProcessOutput,
            detectionPath: SearchPath
        ) {
            val outputLines = output.stdoutLines

            // process version line
            val version = outputLines[0].substringAfter("ansible ")
            if (version.isEmpty()) {
                throw Exception("Could not parse Ansible version.")
            }
            outputLines.removeAt(0)

            // convert to map
            val map = HashMap<String, String>()
            for (line in outputLines) {
                val entry = line.trimStart().split(" = ")
                map[entry[0]] = entry[1]
            }

            // get config file
            var configFile: String? = String()
            if (map.containsKey("config file")) {
                configFile = map["config file"]
            }

            // get executable path
            var executablePath = String()
            if (map.containsKey("executable location")) {
                val executableFile = File(map["executable location"]!!)
                executablePath = executableFile.parentFile.absolutePath
            }

            // get Python version
            var pythonVersion = String()
            if (map.containsKey("python version")) {
                val pyVersion = pythonVersionPattern.find(map["python version"]!!)
                if (pyVersion == null) {
                    throw Exception("Could not parse Python version used by Ansible instance.")
                }
                pythonVersion = pyVersion.groupValues[0]
            }

            instances[version] = AnsibleInstance(
                version,
                configFile,
                executablePath,
                pythonVersion,
                detectionPath.isDefault
            )
        }
    }
}