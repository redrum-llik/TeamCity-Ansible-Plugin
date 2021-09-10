package jetbrains.buildServer.ansibleSupportPlugin

import jetbrains.buildServer.serverSide.BuildFeature
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.ansibleSupportPlugin.AnsibleFeatureConstants as CommonConst

class AnsibleSupportBuildFeature(descriptor: PluginDescriptor) : BuildFeature() {
    private val myEditUrl = descriptor.getPluginResourcesPath(
        "editAnsibleIntegrationSettings.jsp"
    )

    override fun getType(): String = CommonConst.FEATURE_TYPE

    override fun getDisplayName(): String = CommonConst.FEATURE_DISPLAY_NAME

    override fun getEditParametersUrl(): String? = myEditUrl

    override fun isMultipleFeaturesPerBuildTypeAllowed(): Boolean = false

    override fun describeParameters(params: MutableMap<String, String>): String {
        return buildString {
            appendLine("Provide report tab on changes in Ansible playbook tasks")
            val config = AnsibleFeatureConfiguration(params)

            if (config.buildProblemOnChange()) {
                appendLine("Report build problem for any task change")
            }

            if (config.forceColoredLog()) {
                appendLine("Force colored log for Ansible calls")
            }
        }
    }
}