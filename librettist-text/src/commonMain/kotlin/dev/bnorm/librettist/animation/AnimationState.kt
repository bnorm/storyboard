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
