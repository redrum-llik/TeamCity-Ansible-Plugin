package jetbrains.buildServer.runner.ansibleRunner.beans

import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants

class FailIfChangesBean {
    val key = AnsibleRunnerConstants.RUNNER_SETTING_FAIL_IF_CHANGES
    val label = "Fail if changes detected:"
    val description = "Fail build if changes were detected"
}