pluginManagement {
    repositories {
        gradlePluginPortal()
    }

    plugins {
        kotlin("multiplatform") version "1.9.22"
        id("org.jetbrains.compose") version "1.6.0"
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
