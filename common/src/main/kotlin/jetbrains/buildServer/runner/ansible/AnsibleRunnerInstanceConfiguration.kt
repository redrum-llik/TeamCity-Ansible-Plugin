package jetbrains.buildServer.runner.ansible

class AnsibleRunnerInstanceConfiguration(private val properties: Map<String, String>) {
    fun getPlaybookMode(): AnsiblePlaybookType {
        return AnsiblePlaybookType.valueOf(
            properties[AnsibleRunnerConstants.RUNNER_SETTING_PLAYBOOK_MODE]
                ?: error("Playbook mode not set")
        )
    }

    fun getPlaybookFilePath(): String? {
        return properties[AnsibleRunnerConstants.RUNNER_SETTING_PLAYBOOK_FILE]
    }

    fun getPlaybookYaml(): String? {
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
        return properties[AnsibleRunnerConstants.RUNNER_SETTING_COLORED_BUILD_LOG].toBoolean()
    }

    fun getDoPassConfigParams(): Boolean {
        return properties[AnsibleRunnerConstants.RUNNER_SETTING_DO_PASS_CONFIG_PARAMS].toBoolean()
    }
}