import kotlinx.validation.ExperimentalBCVApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.compose")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

group = "dev.bnorm.storyboard"

kotlin {
    explicitApi()

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            freeCompilerArgs.add("-Xjvm-default=all")
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(compose.runtime)
                api(compose.animation)

                api("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
            }
        }
    }
}

apiValidation {
    @OptIn(ExperimentalBCVApi::class)
    klib {
        strictValidation = true
        enabled = true
    }
}
