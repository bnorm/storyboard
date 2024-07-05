pluginManagement {
    repositories {
        gradlePluginPortal()
    }

    plugins {
        kotlin("multiplatform") version "2.0.0"
        kotlin("plugin.compose") version "2.0.0"
        id("org.jetbrains.compose") version "1.7.0-alpha01"
    }

    dependencyResolutionManagement {
        repositories {
            mavenCentral()
            google()
        }
    }
}

rootProject.name = "librettist"

include(":librettist-core")
include(":librettist-text")
