package jetbrains.buildServer.runner.ansible

class AnsibleRunnerInstanceConfiguration(private val properties: Map<String, String>) {
    fun getPlaybook(): String? {
        //TODO: implement reading playbook from a file. Users will enter the file path relative to checkout directory.
        //  This function probably have to try to get YAML first and throw if this is not possible. The exception can be
        //  caught: catch section should make sure the file exists and read from it.
        return properties[AnsibleRunnerConstants.RUNNER_SETTING_PLAYBOOK_YAML]
    }

    fun getInventory(): String? {
        return properties[AnsibleRunnerConstants.RUNNER_SETTING_INVENTORY_FILE]
    }

    fun getExtraArgs(): String? {
        return properties[AnsibleRunnerConstants.RUNNER_SETTING_EXTRA_ARGS]
    }

    fun getIsDryRun(): Boolean {
        return properties[AnsibleRunnerConstants.RUNNER_SETTING_DRY_RUN].toBoolean()
    }

    fun getIsFailOnChanges(): Boolean {
        return properties[AnsibleRunnerConstants.RUNNER_SETTING_FAIL_ON_CHANGES].toBoolean()
    }

    fun getIsLogColored(): Boolean {
        return properties[AnsibleRunnerConstants.RUNNER_COLORED_BUILD_LOG].toBoolean()
    }
}