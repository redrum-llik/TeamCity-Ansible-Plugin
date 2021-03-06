package jetbrains.buildServer.runner.ansibleRunner.beans

import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants

class PassSystemPropertiesBean {
    val key = AnsibleRunnerConstants.RUNNER_SETTING_PASS_SYSTEM_PARAMS
    val label = "Pass system properties:"
    val description = "Save system properties to file and pass them as --extra-vars"
}