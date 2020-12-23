package jetbrains.buildServer.agent.ansibleRunner

object AnsibleCommandLineConstants {
    // ansible command-line data

    val ANSIBLE_COMMAND = "ansible"
    val ANSIBLE_PLAYBOOK_COMMAND = "ansible-playbook"

    val VERSION_PARAM = "--version"
    val CHECK_PARAM = "--check"
    val DIFF_PARAM = "--diff"
    val INVENTORY_PARAM = "--inventory"
}