pluginManagement {
    repositories {
        gradlePluginPortal()
    }

    plugins {
        kotlin("jvm") version "1.9.22"
    }

    dependencyResolutionManagement {
        repositories {
            mavenCentral()
        }
    }
}

rootProject.name = "librettist"
