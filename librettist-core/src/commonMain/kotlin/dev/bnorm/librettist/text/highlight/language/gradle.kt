package ch.deletescape.highlight.languages
import ch.deletescape.highlight.core.*
/*
Language = Gradle
Author = Damian Mee <mee.damian@gmail.com>
Website = http = //meeDamian.com
*/
/**
 * This function was automatically generated, avoid directly editing it if possible!
 * Origin highlight.js/src/languages/gradle.js MD5 <361d6b214d79207349db04d9afec339a>
 */
internal fun gradle(): Mode {
    return Mode(
        case_insensitive = true,
        keywords = listOf(
            keyword(
                className = "keyword",
                value =
                "task project allprojects subprojects artifacts buildscript configurations dependencies repositories sourceSets description delete from into include exclude source classpath destinationDir includes options sourceCompatibility targetCompatibility group flatDir doLast doFirst flatten todir fromdir ant def abstract break case catch continue default do else extends final finally for if implements instanceof native new private protected public return static switch synchronized throw throws transient try volatile while strictfp package import false null super this true antlrtask checkstyle codenarc copy boolean byte char class double float int interface long short void compile runTime file fileTree abs any append asList asWritable call collect compareTo count div dump each eachByte eachFile eachLine every find findAll flatten getAt getErr getIn getOut getText grep immutable inject inspect intersect invokeMethods isCase join leftShift minus multiply newInputStream newOutputStream newPrintWriter newReader newWriter next plus pop power previous print println push putAt read readBytes readLines reverse reverseEach round size sort splitEachLine step subMap times toInteger toList tokenize upto waitForOrKill withPrintWriter withReader withStream withWriter withWriterAppend write writeLine"
            )
        ).flatten(),
        contains = listOf(
            hljs.C_LINE_COMMENT_MODE,
            hljs.C_BLOCK_COMMENT_MODE,
            hljs.APOS_STRING_MODE,
            hljs.QUOTE_STRING_MODE,
            hljs.NUMBER_MODE,
            hljs.REGEXP_MODE
        )
    )
}