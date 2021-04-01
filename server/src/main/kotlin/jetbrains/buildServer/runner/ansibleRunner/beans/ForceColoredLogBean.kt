package jetbrains.buildServer.runner.ansibleRunner.beans

import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants

class ForceColoredLogBean {
    val key = AnsibleRunnerConstants.RUNNER_SETTING_FORCE_COLORED_LOG
    val label = "Force colored log:"
    val description = "Enforce Ansible ANSI-colored execution output"
}