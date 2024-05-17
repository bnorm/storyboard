pluginManagement {
    repositories {
        gradlePluginPortal()
    }

    plugins {
        kotlin("multiplatform") version "2.0.0-RC3"
        kotlin("plugin.compose") version "2.0.0-RC3"
        id("org.jetbrains.compose") version "1.6.10-rc03"
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
