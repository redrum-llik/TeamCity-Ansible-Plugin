package jetbrains.buildServer.runner.ansibleRunner.beans

import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants

class AdditionalArgumentsBean {
    val key = AnsibleRunnerConstants.RUNNER_SETTING_ADDITIONAL_ARGS
    val label = "Additional arguments:"
    val description = "Additional arguments to be passed to the command"
}