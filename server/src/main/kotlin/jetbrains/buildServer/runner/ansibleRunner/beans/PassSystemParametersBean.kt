package jetbrains.buildServer.runner.ansibleRunner.beans

import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants

class PassSystemParametersBean {
    val key = AnsibleRunnerConstants.RUNNER_SETTING_PASS_SYSTEM_PARAMS
    val label = "Pass system parameters:"
    val description = "Save system parameters to file and pass them as --extra-vars"
}