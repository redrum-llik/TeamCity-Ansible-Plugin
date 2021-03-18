plugins {
    kotlin("jvm")
    id("com.github.rodm.teamcity-common")
}

dependencies {
    compile(kotlin("stdlib"))
    testImplementation ("org.testng:testng:6.9.9")
    testImplementation ("org.assertj:assertj-core:2.1.0")
}


teamcity {
    version = rootProject.extra["teamcityVersion"] as String
}


tasks.withType<Jar> {
    baseName = "ansible-common"
}