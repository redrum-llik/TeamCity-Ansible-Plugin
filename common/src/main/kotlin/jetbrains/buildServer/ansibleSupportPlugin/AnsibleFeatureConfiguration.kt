package jetbrains.buildServer.ansibleSupportPlugin

class AnsibleFeatureConfiguration(private val properties: MutableMap<String, String>) {
    fun buildProblemOnChange(): Boolean {
        return properties[AnsibleFeatureConstants.FEATURE_SETTING_BUILD_PROBLEM_ON_CHANGE].toBoolean()
    }

    fun forceColoredLog(): Boolean {
        return properties[AnsibleFeatureConstants.FEATURE_SETTING_FORCE_COLORED_LOG].toBoolean()
    }

    fun exportSystemProperties(): Boolean {
        return !systemPropertiesOutFile().isNullOrEmpty()
    }

    fun systemPropertiesOutFile(): String? {
        return properties[AnsibleFeatureConstants.FEATURE_SETTING_SYSTEM_PROPERTIES]
    }
}