import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsRootPlugin

plugins {
    kotlin("multiplatform") apply false
    kotlin("plugin.serialization") apply false
    kotlin("plugin.compose") apply false
    id("org.jetbrains.compose") apply false
}

group = "dev.bnorm.storyboard"
version = "0.1-SNAPSHOT"

allprojects {
    plugins.withId("org.jetbrains.kotlin.multiplatform") {
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.all {
                languageSettings {
                    enableLanguageFeature("ContextParameters")
                    enableLanguageFeature("WhenGuards")
                    enableLanguageFeature("MultiDollarInterpolation")

                    optIn("androidx.compose.animation.core.ExperimentalTransitionApi")
                    optIn("androidx.compose.animation.ExperimentalAnimationApi")
                    optIn("androidx.compose.animation.ExperimentalSharedTransitionApi")
                }
            }
        }
    }
}

plugins.withType(WasmNodeJsRootPlugin::class.java) {
    tasks.register("rootPackageJson") {
        dependsOn(tasks.named("wasmRootPackageJson"))
    }
}
