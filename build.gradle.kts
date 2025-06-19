import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

plugins {
    kotlin("multiplatform") apply false
    kotlin("plugin.serialization") apply false
    kotlin("plugin.compose") apply false
    id("org.jetbrains.compose") apply false
    id("org.jetbrains.dokka")
    id("com.vanniktech.maven.publish")
}

dokka {
    dokkaPublications.html {
        includes.from(project.layout.projectDirectory.file("README.md"))
    }
}

dependencies {
    dokka(project(":storyboard"))
    dokka(project(":storyboard-easel"))
    dokka(project(":storyboard-text"))
}

allprojects {
    group = "dev.bnorm.storyboard"
    version = "0.1.0-SNAPSHOT"

    val javaVersion = JavaVersion.VERSION_11

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

            targets.withType(KotlinJvmTarget::class.java) {
                compilerOptions {
                    jvmTarget.set(JvmTarget.fromTarget(javaVersion.toString()))
                    freeCompilerArgs.add("-Xjvm-default=all")
                }
            }
        }
    }

    tasks.withType(JavaCompile::class.java).configureEach {
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
    }

    plugins.withId("com.vanniktech.maven.publish") {
        apply(plugin = "org.jetbrains.dokka")
        configure<MavenPublishBaseExtension> {
            publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)
            signAllPublications()
            pom {
                description = "Compose Multiplatform library for building presentations."
                name = project.name
                url = "https://github.com/bnorm/storyboard/"
                licenses {
                    license {
                        name = "The Apache Software License, Version 2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                        distribution = "repo"
                    }
                }
                developers {
                    developer {
                        id = "bnorm"
                        name = "Brian Norman"
                    }
                }
                scm {
                    url = "https://github.com/bnorm/storyboard/"
                    connection = "scm:git:https://github.com/bnorm/storyboard.git"
                    developerConnection = "scm:git:ssh://git@github.com/bnorm/storyboard.git"
                }
            }
        }
    }

    plugins.withId("org.jetbrains.dokka") {
        configure<DokkaExtension> {
            dokkaSourceSets.configureEach {
                documentedVisibilities.add(VisibilityModifier.Public)
                sourceLink {
                    localDirectory = rootProject.projectDir
                    remoteUrl("https://github.com/bnorm/storyboard/tree/main/")
                    remoteLineSuffix = "#L"
                }
            }
        }
    }
}

tasks.register<Sync>("site") {
    into(project.layout.buildDirectory.dir("_site"))

    into("docs/api/latest") {
        from(tasks.named("dokkaGeneratePublicationHtml"))
    }

    into("examples") {
        into("basic") {
            from(project(":examples:basic").tasks.named("wasmJsBrowserDistribution"))
        }
        into("interactive") {
            from(project(":examples:interactive").tasks.named("wasmJsBrowserDistribution"))
        }
    }
}
