package dev.bnorm.librettist.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

fun String.flowLines(other: String): Flow<String> {
    val thisLines = this.lines()
    val otherLines = other.lines()
    require(otherLines.size >= thisLines.size)

    return flow {
        emit(this@flowLines)
        for (line in otherLines.indices) {
            emit(buildString {
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

fun AnnotatedString.flowLines(other: AnnotatedString): Flow<AnnotatedString> {
    val thisLines = this.annotatedLines()
    val otherLines = other.annotatedLines()
    require(otherLines.size >= thisLines.size)

    return flow {
        emit(this@flowLines)
        for (line in otherLines.indices) {
            emit(buildAnnotatedString {
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
    val nextFlow = end.flowLines(next)
    val flow = flow { emitAll(flow); emitAll(nextFlow) }
    return copy(end = next, flow = flow)
}


fun AnimationSequence<AnnotatedString>.thenLines(next: AnnotatedString): AnimationSequence<AnnotatedString> {
    val nextFlow = end.flowLines(next)
    val flow = flow { emitAll(flow); emitAll(nextFlow) }
    return copy(end = next, flow = flow)
}

