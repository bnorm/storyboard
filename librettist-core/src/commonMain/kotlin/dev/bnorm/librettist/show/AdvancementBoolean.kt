package dev.bnorm.librettist.show

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Starts as `false` and advances forward to `true`.
 */
@Composable
fun SlideScope.rememberAdvancementBoolean(): MutableState<Boolean> {
    val state = remember {
        mutableStateOf(direction.toValue(forward = false, backward = true))
    }

    HandleAdvancement {
        val value = state.value
        val nextValue = direction.toValue(forward = true, backward = false)

        val flipped = value != nextValue
        if (flipped) state.value = nextValue
        return@HandleAdvancement flipped
    }

    return state
}
