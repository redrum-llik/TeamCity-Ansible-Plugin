package jetbrains.buildServer.ansibleSupportPlugin.beans

import jetbrains.buildServer.ansibleSupportPlugin.AnsibleRunnerConstants

class BuildProblemOnChangesBean {
    val key = AnsibleRunnerConstants.FEATURE_SETTING_BUILD_PROBLEM_ON_CHANGE
    val label = "Create a build problem for any detected change:"
}