package jetbrains.buildServer.runner.ansible

object AnsibleRunnerConstants {
    // plugin-level data
    const val RUNNER_DESCRIPTION = "Runner for execution of Ansible playbooks"
    const val RUNNER_DISPLAY_NAME = "Ansible"
    const val RUNNER_TYPE = "ansible-runner"
    const val CALLBACK_FOLDER = "callback"

    // detection variables
    const val BUILD_PARAM_SEARCH_PATH = "teamcity.ansible.detector.search.path"

    const val AGENT_PARAM_ANSIBLE_PREFIX = "ansible"
    const val AGENT_PARAM_VERSION_POSTFIX = "version"
    const val AGENT_PARAM_CONFIG_FILE_POSTFIX = "configfile"
    const val AGENT_PARAM_PATH_POSTFIX = "path"
    const val AGENT_PARAM_PY_VERSION_POSTFIX = "pythonversion"

    const val AGENT_PARAM_ANSIBLE_PATH = "${AGENT_PARAM_ANSIBLE_PREFIX}.${AGENT_PARAM_PATH_POSTFIX}"
    const val AGENT_PARAM_ANSIBLE_VERSION = "${AGENT_PARAM_ANSIBLE_PREFIX}.${AGENT_PARAM_VERSION_POSTFIX}"
    const val AGENT_PARAM_ANSIBLE_PY_VERSION = "${AGENT_PARAM_ANSIBLE_PREFIX}.${AGENT_PARAM_PY_VERSION_POSTFIX}"
    const val AGENT_PARAM_ANSIBLE_CALLBACK_PATH = "${AGENT_PARAM_ANSIBLE_PREFIX}.callback.${AGENT_PARAM_PATH_POSTFIX}"

    // ansible bean parameters
    const val RUNNER_SETTING_PLAYBOOK_MODE = "playbookMode"
    const val RUNNER_SETTING_PLAYBOOK_FILE = "playbookFile"
    const val RUNNER_SETTING_PLAYBOOK_YAML = "playbookYaml"
    const val RUNNER_SETTING_INVENTORY_FILE = "inventoryFile"
    const val RUNNER_SETTING_ADDITIONAL_ARGS = "additionalArguments"
    const val RUNNER_SETTING_FAIL_IF_CHANGES = "failIfChanges"
    const val RUNNER_SETTING_FORCE_COLORED_LOG = "forceColoredLog"
    const val RUNNER_SETTING_PASS_SYSTEM_PARAMS = "passSystemParams"
}

fun getVersionedAnsibleVarPrefix(version: String) : String {
    return "${AnsibleRunnerConstants.AGENT_PARAM_ANSIBLE_PREFIX}.${version}"
}

fun getVersionedPathVarName(version: String) : String {
    return "${getVersionedAnsibleVarPrefix(version)}.${AnsibleRunnerConstants.AGENT_PARAM_PATH_POSTFIX}"
}

fun getVersionedConfigVarName(version: String) : String {
    return "${getVersionedAnsibleVarPrefix(version)}.${AnsibleRunnerConstants.AGENT_PARAM_CONFIG_FILE_POSTFIX}"
}

fun getVersionedPyVersionVarName(version: String) : String {
    return "${getVersionedAnsibleVarPrefix(version)}.${AnsibleRunnerConstants.AGENT_PARAM_PY_VERSION_POSTFIX}"
}
