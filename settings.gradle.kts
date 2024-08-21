@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven { setUrl("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }

    plugins {
        val kotlinVersion = "2.0.20-Beta2"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.compose") version kotlinVersion
        id("org.jetbrains.compose") version "1.7.0-dev1756"
    }

    dependencyResolutionManagement {
        repositories {
            mavenCentral()
            google()
            maven { setUrl("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        }
    }
}

rootProject.name = "storyboard"

include(":storyboard-core")
include(":storyboard-easel")
include(":storyboard-text")
