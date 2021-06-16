package jetbrains.buildServer.ansibleSupportPlugin

object AnsibleRuntimeConstants {
    // ansible command-line data

    const val COMMAND_ANSIBLE = "ansible"
    const val PARAM_VERSION = "--version"

    // ansible environment values

    const val ENV_FORCE_COLOR = "ANSIBLE_FORCE_COLOR"
    const val ENV_STDOUT_CALLBACK = "ANSIBLE_STDOUT_CALLBACK"
    const val ENV_DEFAULT_CALLBACK_PLUGIN_PATH = "ANSIBLE_CALLBACK_PLUGINS"

    const val ENV_DEFAULT_CALLBACK_PLUGIN_PATH_VALUE = "~/.ansible/plugins/callback:/usr/share/ansible/plugins/callback"

    // used as a callback plugin parameter
    const val ENV_FAIL_ON_CHANGES = "ANSIBLE_TEAMCITY_FAIL_ON_CHANGES"
    const val ENV_REPORT_PATH = "ANSIBLE_TEAMCITY_REPORT_PATH"

    // ansible callback data

    const val CALLBACK_NAME = "teamcity_callback"

}