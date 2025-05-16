import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    kotlin("plugin.compose")
    id("org.jetbrains.compose")
}

group = "dev.bnorm.storyboard.example"
version = "0.1-SNAPSHOT"

kotlin {
    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        binaries.executable()
        browser()
    }

    sourceSets {
        val ktor_version = "3.1.1"
        commonMain {
            dependencies {
                implementation(project(":storyboard"))
                implementation(project(":storyboard-easel"))
                implementation(project(":storyboard-text"))

                implementation(project(":examples:shared"))

                implementation(compose.material)
                implementation(compose.components.resources)

                implementation("io.ktor:ktor-client-core:${ktor_version}")
                implementation("io.ktor:ktor-client-content-negotiation:${ktor_version}")
                implementation("io.ktor:ktor-serialization-kotlinx-json:${ktor_version}")
            }
        }
        jvmMain {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("io.ktor:ktor-client-okhttp:$ktor_version")
            }
        }
        wasmJsMain {
            dependencies {
                implementation("io.ktor:ktor-client-js:$ktor_version")
            }
        }
    }
}

compose {
    resources.publicResClass = true
    desktop.application.mainClass = "Main_desktopKt"
}
