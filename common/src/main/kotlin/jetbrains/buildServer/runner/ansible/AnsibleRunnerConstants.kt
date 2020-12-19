package jetbrains.buildServer.runner.ansible

object AnsibleRunnerConstants {
    // plugin-level data
    val RUNNER_DESCRIPTION = "Runner for execution of Ansible playbooks"
    val RUNNER_DISPLAY_NAME = "Ansible"
    val RUNNER_TYPE = "ansible-runner"
    val AGENT_BUNDLE_JAR = "ansible-runner.jar"

    val RUNNER_ERROR_TITLE_PROBLEMS_IN_CONF_ON_AGENT = "Failed to run Ansible..."

    // detection variables
    val PARAM_SEARCH_PATH = "teamcity.ansible.detector.search.path"
    val AGENT_PARAM_ANSIBLE_PREFIX = "Ansible"
    val AGENT_PARAM_VERSION_POSTFIX = "Version"
    val AGENT_PARAM_CONFIG_FILE_POSTFIX = "ConfigFile"
    val AGENT_PARAM_PATH_POSTFIX = "Path"
    val AGENT_PARAM_PY_VERSION_POSTFIX = "PythonVersion"

    // ansible command-line data

    val ANSIBLE_COMMAND = "ansible"
    val VERSION_PARAM = "--version"
}