package dev.bnorm.storyboard.easel.template

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.bnorm.storyboard.AdvanceDirection
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.SceneScope
import dev.bnorm.storyboard.compareTo

@Composable
fun <T : Comparable<T>> SceneScope<T>.rememberAdvanceDirection(): AdvanceDirection {
    class Memory<R : Any>(initial: R) {
        private var memory = initial

        fun fold(next: R?): R {
            memory = next ?: memory
            return memory
        }
    }

    val memory = remember {
        Memory(
            when (frame.currentState) {
                is Frame.Start -> AdvanceDirection.Forward
                is Frame.End -> AdvanceDirection.Backward
                else -> AdvanceDirection.Forward
            }
        )
    }

    return memory.fold(AdvanceDirection.from(frame.currentState, frame.targetState, Frame<T>::compareTo))
}
