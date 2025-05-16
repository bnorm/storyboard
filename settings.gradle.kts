@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    plugins {
        val kotlinVersion = "2.2.0-RC"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        kotlin("plugin.compose") version kotlinVersion
        id("org.jetbrains.compose") version "1.8.0"
        id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.17.0"
        id("org.jetbrains.dokka") version "2.0.0"
        id("com.vanniktech.maven.publish") version "0.32.0"
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "storyboard-root"

include(":storyboard")
include(":storyboard-easel")
include(":storyboard-text")

include(":examples:basic")
include(":examples:interactive")
include(":examples:shared")
