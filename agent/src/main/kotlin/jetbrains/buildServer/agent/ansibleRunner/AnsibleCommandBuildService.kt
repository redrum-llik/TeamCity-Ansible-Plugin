package jetbrains.buildServer.agent.ansibleRunner

import jetbrains.buildServer.agent.runner.BuildServiceAdapter
import jetbrains.buildServer.agent.runner.ProgramCommandLine
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine
import java.util.*

class AnsibleCommandBuildService : BuildServiceAdapter() {
    override fun makeProgramCommandLine(): ProgramCommandLine {
        val ret = HashMap<String, String>()
        val arguments = ArrayList<String>()
        arguments.add(0, "8.8.8.8")
        return SimpleProgramCommandLine(
                ret,
                "D:/Temp",
                "ping",
                arguments
        )
    }
}