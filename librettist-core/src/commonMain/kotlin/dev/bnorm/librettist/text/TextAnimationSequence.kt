package dev.bnorm.librettist.text

import androidx.compose.runtime.*
import dev.bnorm.librettist.animation.AnimationState
import dev.bnorm.librettist.animation.LaunchedAnimation
import dev.bnorm.librettist.animation.rememberAdvancementAnimation
import dev.bnorm.librettist.show.Advancement
import dev.bnorm.librettist.show.SlideScope
import dev.bnorm.librettist.show.rememberAdvancementIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

// TODO hook into SeekableTransitionState and KeyframesSpec?
data class AnimationSequence<T>(
    val start: T,
    val end: T,
    val flow: Flow<T>,
)

fun <T> startAnimation(start: T): AnimationSequence<T> {
    return AnimationSequence(start, start, flowOf(start))
}

@Composable
fun <T> AnimateSequence(
    sequence: AnimationSequence<T>,
    state: MutableState<AnimationState>,
    delay: Duration = 50.milliseconds,
    content: @Composable (T) -> Unit,
) {
    var text by remember(sequence) {
        mutableStateOf(if (state.value == AnimationState.PENDING) sequence.start else sequence.end)
    }

    LaunchedAnimation(state) {
        when (it) {
            AnimationState.PENDING -> text = sequence.start
            AnimationState.RUNNING -> sequence.flow.dedup().collect { delay(delay); text = it }
            AnimationState.COMPLETE -> text = sequence.end
        }
    }

    content(text)
}

@Composable
fun <T> SlideScope.AnimateSequence(
    sequence: AnimationSequence<T>,
    state: MutableState<AnimationState> = rememberAdvancementAnimation(),
    delay: Duration = 50.milliseconds,
    content: @Composable (T) -> Unit,
) {
    var text by remember(sequence) {
        mutableStateOf(if (state.value == AnimationState.PENDING) sequence.start else sequence.end)
    }

    LaunchedAnimation(state) {
        when (it) {
            AnimationState.PENDING -> text = sequence.start
            AnimationState.RUNNING -> sequence.flow.dedup().collect { delay(delay); text = it }
            AnimationState.COMPLETE -> text = sequence.end
        }
    }

    content(text)
}

@Composable
fun <T> SlideScope.AnimateSequences(
    sequences: List<AnimationSequence<T>>,
    delay: Duration = 50.milliseconds,
    content: @Composable (T) -> Unit,
) {
    var index by rememberAdvancementIndex(sequences.size * 2 + 1)

    var value by remember(sequences) {
        mutableStateOf(if (index == 0) sequences.first().start else sequences.last().end)
    }

    LaunchedEffect(index) {
        when {
            index % 2 == 0 -> {
                value = when {
                    index / 2 >= sequences.size -> sequences[index / 2 - 1].end
                    else -> sequences[index / 2].start
                }
            }

            else -> {
                if (direction == Advancement.Direction.Forward) {
                    sequences[index / 2].flow.dedup().collect { delay(delay); value = it }
                    index++
                } else {
                    index--
                }
            }
        }
    }

    content(value)
}
