plugins {
    kotlin("jvm") version "2.0.0"
    application
}

group = "io.github.mattshoe.shoebox"
version = "1.0.0"

repositories {
    mavenCentral()
}

application {
    applicationName = "trove"
    mainClass = "io.github.mattshoe.shoebox.trove.MainKt"
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:4.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.10.0.202406032230-r")
    implementation("org.eclipse.jgit:org.eclipse.jgit.ssh.jsch:6.10.0.202406032230-r")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}
kotlin {
    jvmToolchain(19)
}