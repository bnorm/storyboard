package dev.bnorm.librettist.show

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.bnorm.librettist.slide.onSlideEnter
import dev.bnorm.librettist.slide.onSlideExit

// TODO rename to SlideBuilder and add ShowBuilderDsl?
@Stable
class SlideScope<out T>(
    // TODO expose the number of states?
    val transition: Transition<out T>,
    val direction: AdvanceDirection,
    val animatedContentScope: AnimatedContentScope,
    val sharedTransitionScope: SharedTransitionScope,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SlideScope<*>

        if (transition != other.transition) return false
        if (direction != other.direction) return false
        if (animatedContentScope != other.animatedContentScope) return false
        if (sharedTransitionScope != other.sharedTransitionScope) return false

        return true
    }

    override fun hashCode(): Int {
        var result = transition.hashCode()
        result = 31 * result + direction.hashCode()
        result = 31 * result + animatedContentScope.hashCode()
        result = 31 * result + sharedTransitionScope.hashCode()
        return result
    }

    fun Modifier.animateSlideEnter(
        forward: EnterTransition,
        backward: ExitTransition,
    ): Modifier = with(animatedContentScope) {
        animateEnterExit(onSlideEnter { forward }, onSlideEnter { backward })
    }

    fun Modifier.animateSlideExit(
        backward: EnterTransition,
        forward: ExitTransition,
    ): Modifier = with(animatedContentScope) {
        animateEnterExit(onSlideExit { backward }, onSlideExit { forward })
    }
}

@Composable
fun <T> SlideScope(
    value: T,
    direction: AdvanceDirection,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
): SlideScope<T> {
    return SlideScope(updateTransition(value), direction, animatedContentScope, sharedTransitionScope)
}

@Composable
fun <T, R> SlideScope<T>.createChildScope(transform: (T) -> R): SlideScope<R> {
    val transition = transition.createChildTransition { transform(it) }
    return remember(transition) { SlideScope(transition, direction, animatedContentScope, sharedTransitionScope) }
}
