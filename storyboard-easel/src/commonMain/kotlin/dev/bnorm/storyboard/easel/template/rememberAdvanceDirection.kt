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
            when (transition.currentState) {
                is Frame.Start -> AdvanceDirection.Forward
                is Frame.End -> AdvanceDirection.Backward
                else -> AdvanceDirection.Forward
            }
        )
    }

    return memory.fold(toDirection(transition.currentState, transition.targetState, Frame<T>::compareTo))
}

private fun <T> toDirection(current: T, target: T, comparator: Comparator<T>): AdvanceDirection? {
    val compare = comparator.compare(current, target)
    return when {
        compare < 0 -> AdvanceDirection.Forward
        compare > 0 -> AdvanceDirection.Backward
        else -> null
    }
}
