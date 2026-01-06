import com.strumenta.antlrkotlin.gradle.AntlrKotlinTask
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.compose)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.antlr.kotlin)
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
                implementation(compose.material3)
                implementation(libs.antlr.kotlin.runtime)
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

val generateXmlGrammarSource by tasks.registering(AntlrKotlinTask::class) {
    val pkgName = "dev.bnorm.storyboard.text.highlight.antlr.xml"
    val outDir = "generatedAntlr/${pkgName.replace(".", "/")}"

    inputs.dir(layout.projectDirectory.dir("antlr"))
    outputs.dir(layout.buildDirectory.dir(outDir))
    doFirst { delete(layout.buildDirectory.dir(outDir)) }

    source = fileTree(layout.projectDirectory.dir("antlr/xml")) {
        include("**/*.g4")
    }
    packageName = pkgName
    outputDirectory = layout.buildDirectory.dir(outDir).get().asFile
}
