package jetbrains.buildServer.ansibleSupportPlugin

object AnsibleFeatureConstants {
    // plugin-level data
    const val FEATURE_DISPLAY_NAME = "Ansible Integration"
    const val FEATURE_TYPE = "ansible-integration"

    // detection variables
    const val BUILD_PARAM_SEARCH_PATH = "teamcity.ansible.detector.search.path"

    // feature variables
    const val BUILD_PARAM_REPORT_ENABLED = "teamcity.ansible.report.enabled"

    const val CALLBACK_FOLDER = "callback"

    const val HIDDEN_ARTIFACT_REPORT_FILENAME = "ansibleReport.html"
    const val HIDDEN_ARTIFACT_REPORT_FOLDER = ".teamcity/ansible"

    const val AGENT_PARAM_ANSIBLE_PREFIX = "ansible"
    const val AGENT_PARAM_VERSION_POSTFIX = "version"
    const val AGENT_PARAM_CONFIG_FILE_POSTFIX = "configfile"
    const val AGENT_PARAM_PATH_POSTFIX = "path"
    const val AGENT_PARAM_PY_VERSION_POSTFIX = "pythonversion"

    const val AGENT_PARAM_ANSIBLE_PATH = "$AGENT_PARAM_ANSIBLE_PREFIX.$AGENT_PARAM_PATH_POSTFIX"
    const val AGENT_PARAM_ANSIBLE_VERSION = "$AGENT_PARAM_ANSIBLE_PREFIX.$AGENT_PARAM_VERSION_POSTFIX"
    const val AGENT_PARAM_ANSIBLE_PY_VERSION = "$AGENT_PARAM_ANSIBLE_PREFIX.$AGENT_PARAM_PY_VERSION_POSTFIX"
    const val AGENT_PARAM_ANSIBLE_CALLBACK_PATH = "$AGENT_PARAM_ANSIBLE_PREFIX.callback.$AGENT_PARAM_PATH_POSTFIX"

    // ansible bean parameters
    const val FEATURE_SETTING_BUILD_PROBLEM_ON_CHANGE = "buildProblemOnChange"
    const val FEATURE_SETTING_SYSTEM_PROPERTIES = "systemProperties"
    const val FEATURE_SETTING_FORCE_COLORED_LOG = "forceColoredLog"
}

fun getVersionedAnsibleVarPrefix(version: String) : String {
    return "${AnsibleFeatureConstants.AGENT_PARAM_ANSIBLE_PREFIX}.${version}"
}

fun getVersionedPathVarName(version: String) : String {
    return "${getVersionedAnsibleVarPrefix(version)}.${AnsibleFeatureConstants.AGENT_PARAM_PATH_POSTFIX}"
}

fun getVersionedConfigVarName(version: String) : String {
    return "${getVersionedAnsibleVarPrefix(version)}.${AnsibleFeatureConstants.AGENT_PARAM_CONFIG_FILE_POSTFIX}"
}

fun getVersionedPyVersionVarName(version: String) : String {
    return "${getVersionedAnsibleVarPrefix(version)}.${AnsibleFeatureConstants.AGENT_PARAM_PY_VERSION_POSTFIX}"
}
