package jetbrains.buildServer.ansibleSupportPlugin.beans

import jetbrains.buildServer.ansibleSupportPlugin.AnsibleFeatureConstants

class BuildProblemOnChangesBean {
    val key = AnsibleFeatureConstants.FEATURE_SETTING_BUILD_PROBLEM_ON_CHANGE
    val label = "Create a build problem for any detected change:"
}