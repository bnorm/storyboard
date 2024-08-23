package dev.bnorm.storyboard.core

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Stable
import dev.bnorm.storyboard.core.SlideState.*
import kotlinx.collections.immutable.ImmutableList

@Stable
sealed interface SlideScope<out T> {
    val states: ImmutableList<T>
    val transition: Transition<out SlideState<T>>
    val direction: AdvanceDirection

    val animatedVisibilityScope: AnimatedVisibilityScope
    val sharedTransitionScope: SharedTransitionScope

    fun <R : @UnsafeVariance T> SlideState<R>.toState(): T {
        return when (this) {
            Start -> states.first()
            End -> states.last()
            is Value -> value
        }
    }
}

internal class PreviewSlideScope<T>(
    override val states: ImmutableList<T>,
    override val transition: Transition<out SlideState<T>>,
    override val animatedVisibilityScope: AnimatedVisibilityScope,
    override val sharedTransitionScope: SharedTransitionScope,
) : SlideScope<T> {
    override val direction: AdvanceDirection get() = AdvanceDirection.Forward
}

internal class StoryboardSlideScope<T>(
    private val storyboard: Storyboard,
    override val states: ImmutableList<T>,
    override val transition: Transition<out SlideState<T>>,
    override val animatedVisibilityScope: AnimatedVisibilityScope,
    override val sharedTransitionScope: SharedTransitionScope,
) : SlideScope<T> {
    override val direction: AdvanceDirection get() = storyboard.direction
}
