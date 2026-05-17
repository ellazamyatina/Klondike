plugins {
    application
    kotlin("jvm") version "2.0.0"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

repositories {
    mavenCentral()
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.graphics")
}

application {
    mainClass.set("javafx.Main")
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation("org.testfx:testfx-core:4.0.18")
    testImplementation("org.testfx:testfx-junit5:4.0.18")
}

ktlint {
    android.set(false)
    ignoreFailures.set(false)
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
kotlin {
    jvmToolchain(21)
}
