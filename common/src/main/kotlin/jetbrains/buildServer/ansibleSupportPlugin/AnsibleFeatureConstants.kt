package jetbrains.buildServer.ansibleSupportPlugin

object AnsibleFeatureConstants {
    // plugin-level data
    const val FEATURE_DISPLAY_NAME = "Ansible Integration"
    const val FEATURE_TYPE = "ansible-integration"

    // feature variables
    const val BUILD_PARAM_REPORT_ENABLED = "teamcity.ansible.report.enabled"

    const val CALLBACK_FOLDER = "callback"

    const val HIDDEN_ARTIFACT_REPORT_FILENAME = "ansibleReport.html"
    const val HIDDEN_ARTIFACT_REPORT_FOLDER = ".teamcity/ansible"

    const val AGENT_PARAM_ANSIBLE_PREFIX = "ansible"
    const val AGENT_PARAM_PATH_POSTFIX = "path"

    const val AGENT_PARAM_ANSIBLE_CALLBACK_PATH = "$AGENT_PARAM_ANSIBLE_PREFIX.callback.$AGENT_PARAM_PATH_POSTFIX"

    // ansible bean parameters
    const val FEATURE_SETTING_BUILD_PROBLEM_ON_CHANGE = "buildProblemOnChange"
    const val FEATURE_SETTING_FORCE_COLORED_LOG = "forceColoredLog"
}

