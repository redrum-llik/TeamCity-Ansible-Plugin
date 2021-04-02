package jetbrains.buildServer.runner.ansibleRunner.beans

import jetbrains.buildServer.runner.ansible.AnsiblePlaybookType
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants

class PlaybookBean {
    val key = AnsibleRunnerConstants.RUNNER_SETTING_PLAYBOOK_MODE
    val label = "Playbook source:"

    val fileModeKey = AnsiblePlaybookType.FILE.name
    val fileModeValue = AnsiblePlaybookType.FILE.id
    val yamlModeKey = AnsiblePlaybookType.YAML.name
    val yamlModeValue = AnsiblePlaybookType.YAML.id

    val fileKey = AnsibleRunnerConstants.RUNNER_SETTING_PLAYBOOK_FILE
    val fileLabel = "Playbook file:"
    val fileDescription = "Path to the playbook file, absolute or relative to the working directory"

    val yamlKey = AnsibleRunnerConstants.RUNNER_SETTING_PLAYBOOK_YAML
    val yamlLabel = "Playbook YAML:"
    val yamlDescription = "Enter Playbook YAML content"
}
