package jetbrains.buildServer.runner.ansible

object AnsibleRunnerConstants {
    // plugin-level data
    const val RUNNER_DESCRIPTION = "Runner for execution of Ansible playbooks"
    const val RUNNER_DISPLAY_NAME = "Ansible"
    const val RUNNER_TYPE = "ansible-runner"
    const val AGENT_BUNDLE_JAR = "ansible-runner.jar"

    const val RUNNER_ERROR_TITLE_PROBLEMS_IN_CONF_ON_AGENT = "Failed to run Ansible..."

    // detection variables
    const val BUILD_PARAM_SEARCH_PATH = "teamcity.ansible.detector.search.path"
    const val AGENT_PARAM_ANSIBLE_PREFIX = "Ansible"
    const val AGENT_PARAM_VERSION_POSTFIX = "Version"
    const val AGENT_PARAM_CONFIG_FILE_POSTFIX = "ConfigFile"
    const val AGENT_PARAM_PATH_POSTFIX = "Path"
    const val AGENT_PARAM_PY_VERSION_POSTFIX = "PythonVersion"

    const val AGENT_PARAM_ANSIBLE_PATH = "${AGENT_PARAM_ANSIBLE_PREFIX}_${AGENT_PARAM_PATH_POSTFIX}"
    const val AGENT_PARAM_ANSIBLE_VERSION = "${AGENT_PARAM_ANSIBLE_PREFIX}_${AGENT_PARAM_VERSION_POSTFIX}"

    // ansible bean parameters
    const val RUNNER_SETTING_PLAYBOOK_FILE = "Playbook file"
    const val RUNNER_SETTING_INVENTORY_FILE = "Inventory file"
    const val RUNNER_SETTING_EXTRA_ARGS = "Additional arguments"
    const val RUNNER_SETTING_DRY_RUN = "Do a dry run"
    const val RUNNER_SETTING_FAIL_ON_CHANGES = "Fail build if changes are detected"
    const val RUNNER_COLORED_BUILD_LOG = "Force color the build log"
}