import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "dev.bnorm.librettist"
version = "1.0-SNAPSHOT"

kotlin {
    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.ui)
                api(compose.material)

                api("de.cketti.unicode:kotlin-codepoints-deluxe:0.7.0")
                api("com.strumenta:antlr-kotlin-runtime:1.0.0-RC2")
            }
        }
    }
}
