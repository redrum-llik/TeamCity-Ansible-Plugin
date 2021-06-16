package jetbrains.buildServer.ansibleSupportPlugin.beans

import jetbrains.buildServer.ansibleSupportPlugin.AnsibleRunnerConstants

class SystemPropertiesBean {
    val key = AnsibleRunnerConstants.FEATURE_SETTING_SYSTEM_PROPERTIES
    val label = "Save system properties to file:"
    val description = "Save system properties in Ansible format to use with --var-file"
}