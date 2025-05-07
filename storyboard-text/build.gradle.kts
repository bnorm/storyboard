import com.strumenta.antlrkotlin.gradle.AntlrKotlinTask
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    kotlin("plugin.compose")
    id("org.jetbrains.compose")

    id("com.strumenta.antlr-kotlin") version "1.0.2"
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

                implementation("de.cketti.unicode:kotlin-codepoints-deluxe:0.7.0")
                implementation("com.strumenta:antlr-kotlin-runtime:1.0.2")
            }
        }
    }
}

val generateKotlinGrammarSource by tasks.registering(AntlrKotlinTask::class) {
    val pkgName = "dev.bnorm.storyboard.text.highlight.antlr.kotlin"
    val outDir = "generatedAntlr/${pkgName.replace(".", "/")}"

    inputs.dir(layout.projectDirectory.dir("antlr"))
    outputs.dir(layout.buildDirectory.dir(outDir))
    doFirst { delete(layout.buildDirectory.dir(outDir)) }

    source = fileTree(layout.projectDirectory.dir("antlr/kotlin")) {
        include("**/*.g4")
    }
    packageName = pkgName
    outputDirectory = layout.buildDirectory.dir(outDir).get().asFile
}
