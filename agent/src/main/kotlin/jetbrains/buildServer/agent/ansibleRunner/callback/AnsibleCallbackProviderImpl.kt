package jetbrains.buildServer.agent.ansibleRunner.callback

import jetbrains.buildServer.agent.plugins.beans.PluginDescriptor
import java.io.File
import jetbrains.buildServer.runner.ansible.AnsibleRunnerConstants as CommonConst

class AnsibleCallbackProviderImpl(private val pluginDescriptor: PluginDescriptor): AnsibleCallbackProvider {
    override fun getCallbackFolderPath(): String {
        return File(pluginDescriptor.pluginRoot, CommonConst.CALLBACK_FOLDER).absolutePath
    }
}