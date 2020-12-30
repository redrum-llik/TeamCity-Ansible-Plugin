import java.util.zip.*

plugins {
    kotlin("jvm")
    id("com.github.rodm.teamcity-agent")
}

dependencies {
    compile(kotlin("stdlib"))
    compile(project(":common"))

}

teamcity {
    version = rootProject.extra["teamcityVersion"] as String

    agent {
        descriptor {
            pluginDeployment {
                useSeparateClassloader = true
            }
        }

        archiveName = "ansible-agent"

        files {
            into("callback") {
                from (project(":callback").file("src")) {
                    include("**/*.py")
                }
            }
        }
    }
}

tasks.withType<Jar> {
    baseName = "ansible-agent"
}

tasks["agentPlugin"].doLast {
    val zipTask = tasks["agentPlugin"] as Zip
    val zipFile = zipTask.archivePath

    val entries = zipFile.inputStream().use { it ->
        ZipInputStream(it).use { z ->
            generateSequence { z.nextEntry }
                .filterNot { it.isDirectory }
                .map { it.name }
                .toList()
                .sorted()
        }
    }
}