plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.serialization") version "1.9.25"
    application
    id("com.gradleup.shadow") version "8.3.6"
}

group = "com.github.pcarrier.linoder"
version = "2.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.github.ajalt.clikt:clikt-jvm:3.5.4")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("com.github.pcarrier.linoder.cli.MainKt")
}

tasks.test {
    useJUnitPlatform()
}
