package dev.bnorm.librettist.text

import androidx.compose.runtime.*
import dev.bnorm.librettist.animation.AnimationState
import dev.bnorm.librettist.animation.LaunchedAnimation
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

fun flowLines(values: List<String>): Flow<String> {
    require(values.isNotEmpty())
    return values.zipWithNext { a, b -> a.flowLines(b) }.concat()
}

fun AnimationSequence<String>.thenLines(next: String): AnimationSequence<String> {
    val nextFlow = end.flowLines(next)
    val flow = flow { emitAll(flow); emitAll(nextFlow) }
    return copy(end = next, flow = flow)
}

@Composable
fun AnimateLines(
    values: List<String>,
    state: MutableState<AnimationState>,
    content: @Composable (String) -> Unit
) {
    require(values.size >= 2)

    var text by remember(values, state) {
        mutableStateOf(if (state.value == AnimationState.PENDING) values.first() else values.last())
    }

    LaunchedAnimation(state) {
        when (it) {
            AnimationState.PENDING -> text = values.first()
            AnimationState.RUNNING -> flowLines(values).collect { text = it }
            AnimationState.COMPLETE -> text = values.last()
        }
    }

    content(text)
}
