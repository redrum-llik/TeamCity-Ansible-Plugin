import java.util.zip.*

plugins {
    kotlin("jvm")
    id("com.github.rodm.teamcity-agent")
}

dependencies {
    testImplementation ("org.testng:testng:6.9.9")
    testImplementation ("org.assertj:assertj-core:2.1.0")
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
                from(project(":callback").file("src")) {
                    include("**/*.py")
                }
            }
        }
    }
}

tasks.withType<Jar> {
    baseName = "ansible-agent"
}

tasks.getByName<Test>("test") {
    useTestNG {
        suites("/src/test/agent-tests.xml")
    }
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