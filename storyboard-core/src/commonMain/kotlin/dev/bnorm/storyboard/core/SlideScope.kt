package dev.bnorm.storyboard.core

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Stable
import dev.bnorm.storyboard.core.SlideState.*
import kotlinx.collections.immutable.ImmutableList

@Stable
sealed interface SlideScope<T> : AnimatedVisibilityScope, SharedTransitionScope {
    val states: ImmutableList<T>
    val state: Transition<out SlideState<T>>

    val direction: AdvanceDirection
    val currentState: T
        get() {
            require(states.isNotEmpty()) { "implicit conversion to state requires non-empty states" }
            return state.currentState.toState()
        }

    fun <R : T> SlideState<R>.toState(): T {
        require(states.isNotEmpty()) { "implicit conversion to state requires non-empty states" }
        return when (this) {
            Start -> states.first()
            End -> states.last()
            is Value -> value
        }
    }
}

internal class PreviewSlideScope<T>(
    override val states: ImmutableList<T>,
    override val state: Transition<out SlideState<T>>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) : SlideScope<T>,
    AnimatedVisibilityScope by animatedVisibilityScope,
    SharedTransitionScope by sharedTransitionScope {
    override val direction: AdvanceDirection get() = AdvanceDirection.Forward
}

internal class StoryboardSlideScope<T>(
    private val storyboard: StoryboardState,
    override val states: ImmutableList<T>,
    override val state: Transition<out SlideState<T>>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) : SlideScope<T>,
    AnimatedVisibilityScope by animatedVisibilityScope,
    SharedTransitionScope by sharedTransitionScope {
    override val direction: AdvanceDirection get() = storyboard.currentDirection
}
