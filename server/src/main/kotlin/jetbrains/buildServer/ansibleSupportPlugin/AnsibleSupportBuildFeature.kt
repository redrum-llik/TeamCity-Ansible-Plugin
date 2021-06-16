package jetbrains.buildServer.ansibleSupportPlugin

import jetbrains.buildServer.ansibleSupportPlugin.AnsibleFeatureConfiguration
import jetbrains.buildServer.serverSide.BuildFeature
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.ansibleSupportPlugin.AnsibleRunnerConstants as CommonConst

class AnsibleSupportBuildFeature(descriptor: PluginDescriptor) : BuildFeature() {
    private val myEditUrl = descriptor.getPluginResourcesPath(
        "editAnsibleIntegrationSettings.jsp"
    )

    override fun getType(): String {
        return CommonConst.FEATURE_TYPE
    }

    override fun getDisplayName(): String {
        return CommonConst.FEATURE_DISPLAY_NAME
    }

    override fun getEditParametersUrl(): String? {
        return myEditUrl
    }

    override fun describeParameters(params: MutableMap<String, String>): String {
        return buildString {
            append("Provide report tab on changes in Ansible playbook tasks")
            val config = AnsibleFeatureConfiguration(params)

            if (config.buildProblemOnChange()) {
                appendLine("Report build problem for any task change")
            }

            if (config.forceColoredLog()) {
                appendLine("Force colored log for Ansible calls")
            }

            if (config.exportSystemProperties()) {
                appendLine("Export system properties to ${config.systemPropertiesOutFile()} file")
            }
        }
    }
}