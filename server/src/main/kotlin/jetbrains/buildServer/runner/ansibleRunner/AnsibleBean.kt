package jetbrains.buildServer.runner.ansibleRunner

import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants as CommonConst

class AnsibleBean {
    val playbookFileKey: String = CommonConst.RUNNER_SETTING_PLAYBOOK_FILE
    val inventoryFileKey: String = CommonConst.RUNNER_SETTING_INVENTORY_FILE
    val extraArgsKey: String = CommonConst.RUNNER_SETTING_EXTRA_ARGS
    val dryRunKey: String = CommonConst.RUNNER_SETTING_DRY_RUN
    val failOnChangesKey: String = CommonConst.RUNNER_SETTING_FAIL_ON_CHANGES
    val coloredBuildLogKey: String = CommonConst.RUNNER_COLORED_BUILD_LOG
}