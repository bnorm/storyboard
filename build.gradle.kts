import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

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

                    optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                    optIn("kotlin.time.ExperimentalTime")
                    optIn("androidx.compose.animation.core.ExperimentalTransitionApi")
                    optIn("androidx.compose.animation.ExperimentalAnimationApi")
                    optIn("androidx.compose.animation.ExperimentalSharedTransitionApi")
                }
            }
        }
    }
}
