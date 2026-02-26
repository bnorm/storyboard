@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
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
include(":storyboard-layout")
include(":storyboard-text")

include(":examples:basic")
include(":examples:diagram")
include(":examples:interactive")
include(":examples:shared")
include(":examples:teleprompter")
