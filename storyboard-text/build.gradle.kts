import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
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
        all {
            languageSettings {
                optIn("androidx.compose.animation.core.ExperimentalTransitionApi")
                optIn("androidx.compose.animation.ExperimentalAnimationApi")
                optIn("androidx.compose.animation.ExperimentalSharedTransitionApi")
            }
        }

        commonMain {
            dependencies {
                api(project(":storyboard-core"))
                api(compose.ui)
                api(compose.material)

                api("de.cketti.unicode:kotlin-codepoints-deluxe:0.7.0")
                api("com.strumenta:antlr-kotlin-runtime:1.0.0-RC2")
            }
        }
    }
}
