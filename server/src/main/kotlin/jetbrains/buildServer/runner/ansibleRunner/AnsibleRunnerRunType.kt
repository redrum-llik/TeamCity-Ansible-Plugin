package jetbrains.buildServer.runner.ansibleRunner

import jetbrains.buildServer.requirements.Requirement
import jetbrains.buildServer.requirements.RequirementType
import jetbrains.buildServer.runner.ansible.AnsiblePlaybookType
import jetbrains.buildServer.runner.ansible.AnsibleRunnerInstanceConfiguration
import jetbrains.buildServer.serverSide.InvalidProperty
import jetbrains.buildServer.serverSide.PropertiesProcessor
import jetbrains.buildServer.serverSide.RunType
import jetbrains.buildServer.serverSide.RunTypeRegistry
import jetbrains.buildServer.web.openapi.PluginDescriptor
import java.util.*
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants as CommonConst


class AnsibleRunnerRunType(runTypeRegistry: RunTypeRegistry, val myDescriptor: PluginDescriptor) : RunType() {
    init {
        runTypeRegistry.registerRunType(this)
    }

    override fun getRunnerPropertiesProcessor(): PropertiesProcessor? {
        return ParametersValidator()
    }

    override fun getEditRunnerParamsJspFilePath(): String? {
        return myDescriptor.getPluginResourcesPath("ansibleRunnerParams.jsp")
    }

    override fun getViewRunnerParamsJspFilePath(): String? {
        return myDescriptor.getPluginResourcesPath("viewAnsibleRunnerParams.jsp")
    }

    override fun getDefaultRunnerProperties(): MutableMap<String, String> {
        val map: MutableMap<String, String> = HashMap()
        map[CommonConst.RUNNER_SETTING_PLAYBOOK_MODE] = CommonConst.RUNNER_SETTING_PLAYBOOK_FILE
        map[CommonConst.RUNNER_SETTING_PASS_SYSTEM_PARAMS] = "true"
        return map
    }

    override fun getType(): String {
        return CommonConst.RUNNER_TYPE
    }

    override fun getDisplayName(): String {
        return CommonConst.RUNNER_DISPLAY_NAME
    }

    override fun getDescription(): String {
        return CommonConst.RUNNER_DESCRIPTION
    }

    override fun getRunnerSpecificRequirements(runParameters: MutableMap<String, String>): MutableList<Requirement> {
        val result: MutableList<Requirement> = ArrayList<Requirement>()
        result.add(
            Requirement(
                CommonConst.AGENT_PARAM_ANSIBLE_PATH,
                null,
                RequirementType.EXISTS
            )
        )
        result.add(
            Requirement(
                CommonConst.AGENT_PARAM_ANSIBLE_CALLBACK_PATH,
                null,
                RequirementType.EXISTS
            )
        )
        result.add(
            Requirement(
                CommonConst.AGENT_PARAM_ANSIBLE_PY_VERSION,
                "3.0.0",
                RequirementType.VER_NO_LESS_THAN
            )
        )
        return result
    }

    companion object {
        class ParametersValidator : PropertiesProcessor {
            override fun process(properties: MutableMap<String, String>): MutableCollection<InvalidProperty> {
                val ret: MutableCollection<InvalidProperty> = ArrayList<InvalidProperty>(1)
                val config = AnsibleRunnerInstanceConfiguration(properties)
                when (config.getPlaybookMode()) {
                    AnsiblePlaybookType.FILE -> if (config.getPlaybookFilePath().isNullOrEmpty()) {
                        ret.add(InvalidProperty(CommonConst.RUNNER_SETTING_PLAYBOOK_FILE, "Required parameter"))
                    }
                    AnsiblePlaybookType.YAML -> if (config.getPlaybookYaml().isNullOrEmpty()) {
                        ret.add(InvalidProperty(CommonConst.RUNNER_SETTING_PLAYBOOK_YAML, "Required parameter"))
                    }
                }

                if (config.getInventory().isNullOrEmpty()) {
                    ret.add(InvalidProperty(CommonConst.RUNNER_SETTING_INVENTORY_FILE, "Required parameter"))
                }

                return ret
            }
        }
    }
}