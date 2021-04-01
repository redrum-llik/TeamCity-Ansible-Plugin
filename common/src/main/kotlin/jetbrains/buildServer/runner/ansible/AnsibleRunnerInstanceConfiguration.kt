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
        return properties[AnsibleRunnerConstants.RUNNER_SETTING_ADDITIONAL_ARGS]
    }

    fun getIsFailOnChanges(): Boolean {
        return properties[AnsibleRunnerConstants.RUNNER_SETTING_FAIL_IF_CHANGES].toBoolean()
    }

    fun getIsLogColored(): Boolean {
        return properties[AnsibleRunnerConstants.RUNNER_SETTING_FORCE_COLORED_LOG].toBoolean()
    }
}