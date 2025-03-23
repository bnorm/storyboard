package dev.bnorm.storyboard.core

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Stable
import dev.bnorm.storyboard.core.Frame.*
import kotlinx.collections.immutable.ImmutableList

@Stable
sealed interface SceneScope<T> : AnimatedVisibilityScope, SharedTransitionScope {
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
            Start -> states.first()
            End -> states.last()
            is State -> state
        }
    }
}

internal class PreviewSceneScope<T>(
    override val states: ImmutableList<T>,
    override val frame: Transition<out Frame<T>>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) : SceneScope<T>,
    AnimatedVisibilityScope by animatedVisibilityScope,
    SharedTransitionScope by sharedTransitionScope {
    override val direction: AdvanceDirection get() = AdvanceDirection.Forward
}

internal class StoryboardSceneScope<T>(
    private val storyboard: StoryboardState,
    override val states: ImmutableList<T>,
    override val frame: Transition<out Frame<T>>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) : SceneScope<T>,
    AnimatedVisibilityScope by animatedVisibilityScope,
    SharedTransitionScope by sharedTransitionScope {
    override val direction: AdvanceDirection get() = storyboard.currentDirection
}
