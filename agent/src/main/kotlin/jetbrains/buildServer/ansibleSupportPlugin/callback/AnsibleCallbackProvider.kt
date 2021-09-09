package jetbrains.buildServer.ansibleSupportPlugin.callback

interface AnsibleCallbackProvider {
    fun getCallbackFolderPath(): String
}