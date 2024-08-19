@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    plugins {
        val kotlinVersion = "2.0.20-Beta2"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.compose") version kotlinVersion
        id("org.jetbrains.compose") version "1.7.0-alpha01"
    }

    dependencyResolutionManagement {
        repositories {
            mavenCentral()
            google()
        }
    }
}

rootProject.name = "storyboard"

include(":storyboard-core")
include(":storyboard-easel")
include(":storyboard-text")
