package dev.bnorm.librettist.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import dev.bnorm.librettist.animation.AnimationSequence

fun String.flowLines(other: String): Sequence<String> {
    val thisLines = this.lines()
    val otherLines = other.lines()
    require(otherLines.size >= thisLines.size)

    return sequence {
        yield(this@flowLines)
        for (line in otherLines.indices) {
            yield(buildString {
                for (i in 0..line) {
                    appendLine(otherLines[i])
                }
                for (i in line + 1..<thisLines.size) {
                    appendLine(thisLines[i])
                }
            }.trim())
        }
    }
}

fun AnnotatedString.flowLines(other: AnnotatedString): Sequence<AnnotatedString> {
    val thisLines = this.annotatedLines()
    val otherLines = other.annotatedLines()
    require(otherLines.size >= thisLines.size)

    return sequence {
        yield(this@flowLines)
        for (line in otherLines.indices) {
            yield(buildAnnotatedString {
                for (i in 0..line) {
                    append(otherLines[i])
                    if (i + 1 < thisLines.size) appendLine()
                }
                for (i in line + 1..<thisLines.size) {
                    append(thisLines[i])
                    if (i + 1 < thisLines.size) appendLine()
                }
            })
        }
    }
}

fun AnimationSequence<String>.thenLines(next: String): AnimationSequence<String> {
    val nextSequence = end.flowLines(next)
    val sequence = sequence { yieldAll(sequence); yieldAll(nextSequence) }
    return copy(end = next, sequence = sequence)
}


fun AnimationSequence<AnnotatedString>.thenLines(next: AnnotatedString): AnimationSequence<AnnotatedString> {
    val nextSequence = end.flowLines(next)
    val sequence = sequence { yieldAll(sequence); yieldAll(nextSequence) }
    return copy(end = next, sequence = sequence)
}

