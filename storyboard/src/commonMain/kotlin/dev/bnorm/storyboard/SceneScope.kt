package dev.bnorm.storyboard

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Stable
interface SceneScope<T> {
    val states: ImmutableList<T>
    val frame: Transition<out Frame<T>>

    val direction: AdvanceDirection
    val currentState: T
        get() {
            require(states.isNotEmpty()) { "implicit conversion to state requires non-empty states" }
            return frame.currentState.toState()
        }

    fun <R : T> Frame<R>.toState(): T {
        require(states.isNotEmpty()) { "implicit conversion to state requires non-empty states" }
        return when (this) {
            Frame.Start -> states.first()
            Frame.End -> states.last()
            is Frame.State -> state
        }
    }
}
