package dev.bnorm.librettist.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

fun String.flowLineEndDiff(other: String): Flow<String> {
    val leftLines = this.lines()
    val rightLines = other.lines()
    require(rightLines.size == leftLines.size)
    val lines = leftLines.size

    return flow {
        emit(this@flowLineEndDiff)
        for (line in 0..<lines) {
            val left = leftLines[line]
            val right = rightLines[line]

            fun buildLeftString(i: Int): String {
                return buildString {
                    for (l in 0..<line) appendLine(leftLines[l])
                    appendLine(left.substring(0, i))
                    for (l in line + 1..<lines) appendLine(rightLines[l])
                }
            }

            fun buildRightString(i: Int): String {
                return buildString {
                    for (l in 0..<line) appendLine(leftLines[l])
                    appendLine(right.substring(0, i))
                    for (l in line + 1..<lines) appendLine(rightLines[l])
                }
            }

            // TODO codepoints
            var index = 0
            while (index < left.length && index < right.length && left[index] == right[index]) index++

            if (index < left.length) {
                for (i in (index..<left.length).reversed()) {
                    emit(buildLeftString(i))
                }
            }
            if (index < right.length) {
                for (i in index + 1..right.length) {
                    emit(buildRightString(i))
                }
            }
        }
    }
}

fun AnimationSequence<String>.thenLineEndDiff(next: String): AnimationSequence<String> {
    val nextFlow = end.flowLineEndDiff(next)
    val flow = flow { emitAll(flow); emitAll(nextFlow) }
    return copy(end = next, flow = flow)
}

fun AnnotatedString.flowLineEndDiff(other: AnnotatedString): Flow<AnnotatedString> {
    fun AnnotatedString.annotatedLines(): List<AnnotatedString> {
        val delimiters = listOf("\r\n", "\n", "\r")
        return buildList {
            var previousIndex = 0
            var index = text.findAnyOf(delimiters)?.first ?: -1
            while (index != -1) {
                add(subSequence(previousIndex, index))
                previousIndex = index + 1
                index = text.findAnyOf(delimiters, previousIndex)?.first ?: -1
            }
            add(subSequence(previousIndex, length))
        }
    }

    val leftLines = this.annotatedLines()
    val rightLines = other.annotatedLines()
    require(rightLines.size == leftLines.size)
    val lines = leftLines.size

    return flow {
        emit(this@flowLineEndDiff)
        for (line in 0..<lines) {
            val left = leftLines[line]
            val right = rightLines[line]

            fun buildLeftString(i: Int): AnnotatedString {
                return buildAnnotatedString {
                    for (l in 0..<line) appendLine(leftLines[l])
                    appendLine(left.subSequence(0, i))
                    for (l in line + 1..<lines) appendLine(rightLines[l])
                }
            }

            fun buildRightString(i: Int): AnnotatedString {
                return buildAnnotatedString {
                    for (l in 0..<line) appendLine(leftLines[l])
                    appendLine(right.subSequence(0, i))
                    for (l in line + 1..<lines) appendLine(rightLines[l])
                }
            }

            // TODO codepoints
            var index = 0
            while (index < left.length && index < right.length && left[index] == right[index]) index++

            if (index < left.length) {
                for (i in (index..<left.length).reversed()) {
                    emit(buildLeftString(i))
                }
            }
            if (index < right.length) {
                for (i in index + 1..right.length) {
                    emit(buildRightString(i))
                }
            }
        }
    }
}

fun AnimationSequence<AnnotatedString>.thenLineEndDiff(next: AnnotatedString): AnimationSequence<AnnotatedString> {
    val nextFlow = end.flowLineEndDiff(next)
    val flow = flow { emitAll(flow); emitAll(nextFlow) }
    return copy(end = next, flow = flow)
}
