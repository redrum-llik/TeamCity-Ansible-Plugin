package jetbrains.buildServer.runner.ansible

enum class AnsiblePlaybookType(val id: String) {
    FILE("File"),
    YAML("YAML")
}