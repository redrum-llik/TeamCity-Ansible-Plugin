package jetbrains.buildServer.agent.ansibleRunner.callback

interface AnsibleCallbackProvider {
    fun getCallbackFolderPath(): String
}