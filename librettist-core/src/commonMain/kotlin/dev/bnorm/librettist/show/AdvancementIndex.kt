package dev.bnorm.librettist.show

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Starts at `0` and advances forward to `count - 1`.
 */
@Composable
fun SlideScope.rememberAdvancementIndex(count: Int): State<Int> {
    val index = remember {
        mutableStateOf(direction.toValue(forward = 0, backward = count - 1))
    }

    HandleAdvancement {
        val value = index.value
        val nextValue = it.direction.toValue(forward = value + 1, backward = value - 1)

        val inRange = nextValue in 0..<count
        if (inRange) index.value = nextValue
        return@HandleAdvancement inRange
    }

    return index
}