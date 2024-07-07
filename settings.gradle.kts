pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
    }

    plugins {
        val kotlinVersion = "2.0.10-RC-510"

        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.compose") version kotlinVersion
        id("org.jetbrains.compose") version "1.7.0-alpha01"
    }

    dependencyResolutionManagement {
        repositories {
            mavenCentral()
            google()
            maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/")
        }
    }
}

rootProject.name = "librettist"

include(":librettist-core")
include(":librettist-text")
