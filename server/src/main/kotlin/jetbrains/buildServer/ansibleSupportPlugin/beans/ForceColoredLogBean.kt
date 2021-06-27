package jetbrains.buildServer.ansibleSupportPlugin.beans

import jetbrains.buildServer.ansibleSupportPlugin.AnsibleFeatureConstants

class ForceColoredLogBean {
    val key = AnsibleFeatureConstants.FEATURE_SETTING_FORCE_COLORED_LOG
    val label = "Force colored log:"
    val description = "Enforce Ansible ANSI-colored execution output"
}