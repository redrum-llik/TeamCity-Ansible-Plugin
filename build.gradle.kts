plugins {
    id("com.github.rodm.teamcity-common") version "1.3" apply false
    id("com.github.rodm.teamcity-agent") version "1.3" apply false
    id("com.github.rodm.teamcity-server") version "1.3" apply false

    kotlin("jvm") version "1.4.10"
}

ext {
    set("teamcityVersion", "2021.1")
}

allprojects {

    repositories {
        mavenCentral()
        jcenter()
    }

    group = "jetbrains.buildserver"
    version = System.getenv("BUILD_NUMBER") ?: "1.0"

}

subprojects {

    tasks.withType<Zip> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

task("teamcity") {
    // dependsOn(":tests:test")
    dependsOn(":server:teamcity")
}
