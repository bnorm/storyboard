@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven { setUrl("https://redirector.kotlinlang.org/maven/dev") }
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        val kotlinVersion = "2.2.0-dev-12451"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        kotlin("plugin.compose") version kotlinVersion
        id("org.jetbrains.compose") version "1.8.0-beta01"
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven { setUrl("https://redirector.kotlinlang.org/maven/dev") }
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

rootProject.name = "storyboard"

include(":storyboard-core")
include(":storyboard-easel")
include(":storyboard-text")

include(":examples:basic")
include(":examples:interactive")
include(":examples:shared")
