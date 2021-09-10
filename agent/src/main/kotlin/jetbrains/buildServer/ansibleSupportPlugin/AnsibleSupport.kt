package jetbrains.buildServer.ansibleSupportPlugin

import jetbrains.buildServer.agent.*
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher
import jetbrains.buildServer.util.EventDispatcher
import java.io.File
import java.nio.file.NoSuchFileException

class AnsibleSupport(
    events: EventDispatcher<AgentLifeCycleListener>,
    watcher: ArtifactsWatcher
) : AgentLifeCycleAdapter() {

    private val myWatcher = watcher
    private var myReportPath: String? = null
    private val myFlowId = FlowGenerator.generateNewFlow()

    init {
        events.addListener(this)
    }

    private fun getFeature(build: AgentRunningBuild): AgentBuildFeature? {
        val features = build.getBuildFeaturesOfType(AnsibleFeatureConstants.FEATURE_TYPE)
        if (features.isNotEmpty()) {
            return features.first()
        }
        return null
    }

    private fun getFeatureConfiguration(
        feature: AgentBuildFeature
    ): AnsibleFeatureConfiguration {
        return AnsibleFeatureConfiguration(
            feature.parameters
        )
    }

    private fun getBuildLogger(build: AgentRunningBuild): FlowLogger {
        return build.buildLogger.getFlowLogger(myFlowId)!!
    }

    private fun passEnvParamToBuild(
        build: AgentRunningBuild,
        logger: BuildProgressLogger,
        name: String,
        value: String = String()
    ) {
        if (build.sharedBuildParameters.environmentVariables.containsKey(name)) {
            logger.warning("Overriding environment variable $name with value $value")
        } else {
            logger.debug("Adding environment variable $name: $value")
        }
        build.addSharedEnvironmentVariable(name, value)
    }

    private fun passForceColoredLogEnvParam(
        build: AgentRunningBuild,
        logger: BuildProgressLogger
    ) {
        logger.debug("Forcing colored build log for Ansible playbook execution")
        passEnvParamToBuild(
            build,
            logger,
            AnsibleRuntimeConstants.ENV_FORCE_COLOR,
            true.toString()
        )
    }

    private fun passBuildProblemOnChangeEnvParam(
        build: AgentRunningBuild,
        logger: BuildProgressLogger
    ) {
        logger.debug("Will fail build if any changes are detected during Ansible playbook execution")
        passEnvParamToBuild(
            build,
            logger,
            AnsibleRuntimeConstants.ENV_FAIL_ON_CHANGES,
            true.toString()
        )
    }

    private fun getNewCallbackPluginPaths(build: AgentRunningBuild): String {
        val callbackFolderPath = build.sharedConfigParameters[
                AnsibleFeatureConstants.AGENT_PARAM_ANSIBLE_CALLBACK_PATH
        ]

        val currentCallbackPluginPaths = build.sharedBuildParameters.environmentVariables[
                AnsibleRuntimeConstants.ENV_DEFAULT_CALLBACK_PLUGIN_PATH
        ]
        return when {
            callbackFolderPath.isNullOrEmpty() -> {
                throw NoSuchFileException("Feature expects custom callback path to be available in order to process the Ansible output")
            }
            currentCallbackPluginPaths == null -> {
                "$callbackFolderPath:${AnsibleRuntimeConstants.ENV_DEFAULT_CALLBACK_PLUGIN_PATH_VALUE}"
            }
            currentCallbackPluginPaths.isEmpty() -> {
                callbackFolderPath
            }
            else -> {
                "$callbackFolderPath:$currentCallbackPluginPaths"
            }
        }
    }

    private fun passCallbackEnvParams(
        build: AgentRunningBuild,
        logger: BuildProgressLogger
    ) {
        passEnvParamToBuild(
            build,
            logger,
            AnsibleRuntimeConstants.ENV_STDOUT_CALLBACK,
            AnsibleRuntimeConstants.CALLBACK_NAME
        )
        passEnvParamToBuild(
            build,
            logger,
            AnsibleRuntimeConstants.ENV_DEFAULT_CALLBACK_PLUGIN_PATH,
            getNewCallbackPluginPaths(build)
        )
    }

    private fun isReportEnabled(runningBuild: AgentRunningBuild): Boolean {
        val param = runningBuild.sharedBuildParameters.allParameters[AnsibleFeatureConstants.BUILD_PARAM_REPORT_ENABLED]
        if (param == null || param.toBoolean()) {
            return true
        }
        return false
    }

    private fun passReportPathParam(runningBuild: AgentRunningBuild, logger: BuildProgressLogger, reportPath: String) {
        passEnvParamToBuild(
            runningBuild,
            logger,
            AnsibleRuntimeConstants.ENV_REPORT_PATH,
            reportPath
        )
    }

    override fun sourcesUpdated(runningBuild: AgentRunningBuild) {
        val feature = getFeature(runningBuild)
        if (feature != null) {
            val logger = getBuildLogger(runningBuild)
            try {
                prepareEnvironment(runningBuild, feature, logger)
            } catch (e: Exception) {
                logger.warning(e.stackTraceToString())
                throw e
            }
        }
    }

    private fun prepareEnvironment(
        runningBuild: AgentRunningBuild,
        feature: AgentBuildFeature,
        logger: BuildProgressLogger
    ) {
        val configuration = getFeatureConfiguration(feature)

        passCallbackEnvParams(
            runningBuild,
            logger
        ) // pass environment variables needed to initialize the callback plugin

        if (isReportEnabled(runningBuild)) { // pass report path to the script and register it as an artifact path
            myReportPath = File(
                runningBuild.agentTempDirectory,
                AnsibleFeatureConstants.HIDDEN_ARTIFACT_REPORT_FILENAME
            ).absolutePath
            passReportPathParam(runningBuild, logger, myReportPath.toString())
        }

        if (configuration.buildProblemOnChange()) { // should the callback create build problem if any changes are detected
            passBuildProblemOnChangeEnvParam(runningBuild, logger)
        }

        if (configuration.forceColoredLog()) { // pass Ansible environment variable to enforce colored log
            passForceColoredLogEnvParam(runningBuild, logger)
        }
    }

    override fun beforeBuildFinish(build: AgentRunningBuild, buildStatus: BuildFinishedStatus) {
        if (!buildStatus.isFailed && isFeatureEnabled(build)) {
            val logger = getBuildLogger(build)
            try {
                handleAnsibleOutput(build, logger)
            } catch (e: Exception) {
                logger.warning(e.stackTraceToString())
                throw e
            }
        }
    }

    private fun handleAnsibleOutput(build: AgentRunningBuild, logger: BuildProgressLogger) {
        if (!myReportPath.isNullOrEmpty()) {
            myWatcher.addNewArtifactsPath(
                "$myReportPath => ${AnsibleFeatureConstants.HIDDEN_ARTIFACT_REPORT_FOLDER}"
            )
        } else if (isReportEnabled(build)) {
            logger.warning("Report path is not set")
        }
    }
}