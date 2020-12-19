package jetbrains.buildServer.agent.ansibleRunner.detect

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.util.text.StringUtil.isEmptyOrSpaces
import jetbrains.buildServer.agent.BuildAgentConfiguration
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants.ANSIBLE_COMMAND
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants.PARAM_SEARCH_PATH
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants.VERSION_PARAM
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.HashMap


class AnsibleCommandLineDetector() : AnsibleDetector {
    private val LOG = Logger.getInstance(this.javaClass.name)

    override fun detectAnsibleInstances(buildAgentConfiguration: BuildAgentConfiguration): MutableMap<String, AnsibleInstance> {
        val instances = HashMap<String, AnsibleInstance>()
        for (path in getSearchPaths(buildAgentConfiguration)) {
            val output = runDetectionCommand(path)
            parseAnsibleInstance(instances, output, path)
        }
        return instances
    }

    private fun getSearchPaths(buildAgentConfiguration: BuildAgentConfiguration): MutableList<SearchPath> {
        val searchPath = buildAgentConfiguration.configurationParameters[PARAM_SEARCH_PATH]
        val workDirPath = buildAgentConfiguration.workDirectory.toString()
        val result: MutableList<SearchPath> = ArrayList<SearchPath>()
        if (!searchPath.isNullOrBlank()) {
            result.add(SearchPath(searchPath))
        }
        result.add(SearchPath(workDirPath, true)) // if Ansible is available on PATH, any directory should be good to run detection
        return result
    }

    private fun runDetectionCommand(detectionPath: SearchPath): ProcessOutput {
        val commandLine = GeneralCommandLine()
        commandLine.exePath = ANSIBLE_COMMAND
        commandLine.addParameter(VERSION_PARAM)
        commandLine.setWorkDirectory(detectionPath.path)

        LOG.debug("Detecting Ansible in: $detectionPath")
        return handleDetectionProcess(commandLine)
    }

    private fun logDetectionProcessOutput(output: ProcessOutput) {
        val stdOut = output.stdout.trim { it <= ' ' }
        val stdErr = output.stderr.trim { it <= ' ' }
        val b = StringBuilder("Ansible detection command output: \n")
        if (!isEmptyOrSpaces(stdOut)) {
            b.append("\n----- stdout: -----\n").append(stdOut).append("\n")
        }
        if (!isEmptyOrSpaces(stdErr)) {
            b.append("\n----- stderr: -----\n").append(stdErr).append("\n")
        }
        LOG.warn(b.toString())
    }

    private fun handleDetectionProcess(commandLine: GeneralCommandLine): ProcessOutput {
        val handler = CapturingProcessHandler(commandLine.createProcess(), StandardCharsets.UTF_8)
        val output = handler.runProcess() ?: throw Exception("Command produced no output.")
        val errorOutput = output.stderr
        if (!errorOutput.isNullOrEmpty()) {
            logDetectionProcessOutput(output)
            throw Exception(errorOutput)
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
                executablePath = map["executable location"]!!
            }

            // get Python version
            var pythonVersion: String? = String()
            if (map.containsKey("python version")) {
                val result = pythonVersionPattern.find(map["python version"]!!)
                if (result != null) {
                    pythonVersion = result.groupValues[0]
                }
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