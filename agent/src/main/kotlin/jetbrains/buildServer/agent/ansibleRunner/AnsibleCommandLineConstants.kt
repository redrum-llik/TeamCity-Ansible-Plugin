package jetbrains.buildServer.agent.ansibleRunner

object AnsibleCommandLineConstants {
    // ansible command-line data

    const val COMMAND_ANSIBLE = "ansible"
    const val COMMAND_ANSIBLE_PLAYBOOK = "ansible-playbook"

    const val PARAM_VERSION = "--version"
    const val PARAM_CHECK = "--check"
    const val PARAM_DIFF = "--diff"
    const val PARAM_INVENTORY = "--inventory"

    // ansible environment values

    const val ENV_FORCE_COLOR = "ANSIBLE_FORCE_COLOR"
    const val ENV_STDOUT_CALLBACK = "ANSIBLE_STDOUT_CALLBACK"

    // ansible callback data

    const val CALLBACK_ADJACENT_DIR = "callback_plugins"
    const val CALLBACK_NAME = "teamcity_callback"

}