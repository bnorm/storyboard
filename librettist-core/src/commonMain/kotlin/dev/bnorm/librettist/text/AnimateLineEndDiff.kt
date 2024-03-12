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
    val leftLines = this.annotatedLines()
    val rightLines = other.annotatedLines()
    require(rightLines.size == leftLines.size)
    val lines = leftLines.size

    return flow {
        emit(this@flowLineEndDiff)
        for (lineIndex in 0..<lines) {
            val left = leftLines[lineIndex]
            val right = rightLines[lineIndex]

            fun buildString(i: Int, line: AnnotatedString): AnnotatedString {
                return buildAnnotatedString {
                    for (l in 0..<lineIndex) {
                        append(leftLines[l])
                        if (l + 1 < lines) appendLine()
                    }
                    append(line.subSequence(0, i))
                    for (l in lineIndex + 1..<lines) {
                        appendLine()
                        append(rightLines[l])
                    }
                }
            }

            // TODO codepoints?
            var index = 0
            while (index < left.length && index < right.length && left[index] == right[index]) index++

            if (index < left.length) {
                for (i in (index..<left.length).reversed()) {
                    emit(buildString(i, left))
                }
            }
            if (index < right.length) {
                for (i in index + 1..right.length) {
                    emit(buildString(i, right))
                }
            }
        }
        emit(other)
    }
}

fun AnimationSequence<AnnotatedString>.thenLineEndDiff(next: AnnotatedString): AnimationSequence<AnnotatedString> {
    val nextFlow = end.flowLineEndDiff(next)
    val flow = flow { emitAll(flow); emitAll(nextFlow) }
    return copy(end = next, flow = flow)
}
