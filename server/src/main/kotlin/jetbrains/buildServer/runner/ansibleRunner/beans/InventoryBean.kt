package jetbrains.buildServer.runner.ansibleRunner.beans

import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants

class InventoryBean {
    val key = AnsibleRunnerConstants.RUNNER_SETTING_INVENTORY_FILE
    val label = "Inventory:"
    val description = "Path to the inventory file, absolute or relative to the working directory"
}