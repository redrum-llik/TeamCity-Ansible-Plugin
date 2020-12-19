package jetbrains.buildServer.runner.ansible

class AnsibleRunnerInstanceConfiguration(private val properties: Map<String, String>) {
    fun getPlaybook(): String? {
        return properties[AnsibleRunnerConstants.RUNNER_PARAM_PLAYBOOK_FILE]
    }

    fun getInventory(): String? {
        return properties[AnsibleRunnerConstants.RUNNER_PARAM_INVENTORY_FILE]
    }

    fun getExtraArgs(): String? {
        return properties[AnsibleRunnerConstants.RUNNER_PARAM_EXTRA_ARGS]
    }
}