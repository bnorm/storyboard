package dev.bnorm.storyboard

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Stable
interface SceneScope<T> {
    val states: ImmutableList<T>
    val frame: Transition<out Frame<T>>

    // TODO figure out how to remove this from the scope...
    val direction: AdvanceDirection
}

val <T> SceneScope<T>.currentState: T
    get() {
        require(states.isNotEmpty()) { "implicit conversion to state requires non-empty states" }
        return frame.currentState.toState()
    }

context(sceneScope: SceneScope<T>)
fun <T, R : T> Frame<R>.toState(): T {
    require(sceneScope.states.isNotEmpty()) { "implicit conversion to state requires non-empty states" }
    return when (this) {
        Frame.Start -> sceneScope.states.first()
        Frame.End -> sceneScope.states.last()
        is Frame.State -> state
    }
}
