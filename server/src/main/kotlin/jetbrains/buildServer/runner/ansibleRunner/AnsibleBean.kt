package jetbrains.buildServer.runner.ansibleRunner

import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants

class AnsibleBean {
    val playbookFileKey: String = AnsibleRunnerConstants.RUNNER_PARAM_PLAYBOOK_FILE
    val inventoryFileKey: String = AnsibleRunnerConstants.RUNNER_PARAM_INVENTORY_FILE
    val extraArgsKey: String = AnsibleRunnerConstants.RUNNER_PARAM_EXTRA_ARGS
}