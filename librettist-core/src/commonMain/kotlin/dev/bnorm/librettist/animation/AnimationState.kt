package dev.bnorm.librettist.animation

import androidx.compose.runtime.*
import dev.bnorm.librettist.show.Advancement.Direction
import dev.bnorm.librettist.show.HandleAdvancement
import dev.bnorm.librettist.show.SlideScope
import kotlinx.coroutines.CoroutineScope

enum class AnimationState {
    PENDING,
    RUNNING,
    COMPLETE,
}

@Composable
fun LaunchedAnimation(
    state: MutableState<AnimationState>,
    block: suspend CoroutineScope.(AnimationState) -> Unit,
) {
    LaunchedEffect(state.value) {
        block(state.value)
        if (state.value == AnimationState.RUNNING) {
            state.value = AnimationState.COMPLETE
        }
    }
}

@Composable
fun SlideScope.rememberAdvancementAnimation(): MutableState<AnimationState> {
    val state = remember {
        mutableStateOf(direction.toValue(forward = AnimationState.PENDING, backward = AnimationState.COMPLETE))
    }

    HandleAdvancement {
        when (it.direction) {
            Direction.Forward -> {
                when (state.value) {
                    AnimationState.PENDING -> state.value = AnimationState.RUNNING
                    AnimationState.RUNNING -> state.value = AnimationState.COMPLETE
                    AnimationState.COMPLETE -> return@HandleAdvancement false
                }
            }
            Direction.Backward -> {
                when (state.value) {
                    AnimationState.PENDING -> return@HandleAdvancement false
                    AnimationState.RUNNING -> state.value = AnimationState.PENDING
                    AnimationState.COMPLETE -> state.value = AnimationState.PENDING
                }
            }
        }

        return@HandleAdvancement true
    }

    return state
}

@Composable
fun SlideScope.rememberAdvancementAnimations(count: Int): List<MutableState<AnimationState>> {
    require(count > 1)

    // TODO should index be a mutable state?
    var index = remember(count) { direction.toValue(forward = 0, backward = count - 1) }
    val states = remember(count) {
        val start = direction.toValue(forward = AnimationState.PENDING, backward = AnimationState.COMPLETE)
        List(count) { mutableStateOf(start) }
    }

    HandleAdvancement {
        fun handle(state: MutableState<AnimationState>): Boolean {
            when (it.direction) {
                Direction.Forward -> {
                    when (state.value) {
                        AnimationState.PENDING -> state.value = AnimationState.RUNNING
                        AnimationState.RUNNING -> state.value = AnimationState.COMPLETE

                        AnimationState.COMPLETE -> {
                            index = minOf(index + 1, count - 1)
                            return false
                        }
                    }
                }

                Direction.Backward -> {
                    when (state.value) {
                        AnimationState.PENDING -> {
                            index = maxOf(index - 1, 0)
                            return false
                        }

                        AnimationState.RUNNING -> state.value = AnimationState.PENDING
                        AnimationState.COMPLETE -> state.value = AnimationState.PENDING
                    }
                }
            }

            return true
        }

        if (handle(states[index])) return@HandleAdvancement true
        return@HandleAdvancement handle(states[index]) // Retry in case we've moved to the next/previous animation
    }

    return states
}
