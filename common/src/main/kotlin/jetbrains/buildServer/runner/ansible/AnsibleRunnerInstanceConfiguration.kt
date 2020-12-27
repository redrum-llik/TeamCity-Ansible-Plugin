package jetbrains.buildServer.runner.ansible

class AnsibleRunnerInstanceConfiguration(private val properties: Map<String, String>) {
    fun getPlaybook(): String? {
        return properties[AnsibleRunnerConstants.RUNNER_SETTING_PLAYBOOK_FILE]
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