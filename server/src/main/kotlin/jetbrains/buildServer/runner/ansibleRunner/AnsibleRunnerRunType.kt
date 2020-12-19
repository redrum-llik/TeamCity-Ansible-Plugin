package jetbrains.buildServer.runner.ansibleRunner

import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants
import jetbrains.buildServer.serverSide.InvalidProperty
import jetbrains.buildServer.serverSide.PropertiesProcessor
import jetbrains.buildServer.serverSide.RunType
import jetbrains.buildServer.serverSide.RunTypeRegistry
import java.util.*

class AnsibleRunnerRunType() : RunType() {
    constructor(runTypeRegistry: RunTypeRegistry) : this() {
        runTypeRegistry.registerRunType(this)
    }

    override fun getRunnerPropertiesProcessor(): PropertiesProcessor? {
        return ParametersValidator()
    }

    override fun getEditRunnerParamsJspFilePath(): String? {
        return "taskRunnerRunParams.jsp";
    }

    override fun getViewRunnerParamsJspFilePath(): String? {
        return "viewTaskRunnerRunParams.jsp";
    }

    override fun getDefaultRunnerProperties(): MutableMap<String, String> {
        val map: MutableMap<String, String> = HashMap()
        return map;
    }

    override fun getType(): String {
        return AnsibleRunnerConstants.RUNNER_TYPE
    }

    override fun getDisplayName(): String {
        return AnsibleRunnerConstants.RUNNER_DISPLAY_NAME
    }

    override fun getDescription(): String {
        return AnsibleRunnerConstants.RUNNER_DESCRIPTION
    }

    companion object {
        class ParametersValidator : PropertiesProcessor {
            override fun process(p0: MutableMap<String, String>?): MutableCollection<InvalidProperty> {
                val ret: MutableCollection<InvalidProperty> = ArrayList<InvalidProperty>(1)
                return ret
            }
        }
    }
}