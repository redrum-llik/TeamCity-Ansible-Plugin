import java.util.zip.*

plugins {
    kotlin("jvm")
    id("com.github.rodm.teamcity-agent")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":common"))
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
                from (project(":callback").file("src")) {
                    include("**/*.py")
                }
            }
        }
    }
}

tasks.withType<Jar> {
    archiveBaseName.set("ansible-agent")
}

tasks["agentPlugin"].doLast {
    val zipTask = tasks["agentPlugin"] as Zip
    val zipFile = zipTask.archiveFile.get().asFile

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