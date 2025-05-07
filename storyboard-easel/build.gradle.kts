import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    kotlin("plugin.compose")
    id("org.jetbrains.compose")
}

group = "dev.bnorm.storyboard"

kotlin {
    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":storyboard"))
                implementation(compose.material)

                implementation("org.jetbrains.compose.material:material-icons-core:1.7.3")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
            }
        }

        jvmMain {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("io.github.vinceglb:filekit-compose:0.8.8")
                implementation("org.apache.pdfbox:pdfbox:3.0.1")
            }
        }
    }
}
