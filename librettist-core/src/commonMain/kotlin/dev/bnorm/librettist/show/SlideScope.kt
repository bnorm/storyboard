package dev.bnorm.librettist.show

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.runtime.Composable

// TODO rename to SlideBuilder and add ShowBuilderDsl?
class SlideScope<out T>(
    // TODO expose the number of states?
    val transition: Transition<out T>,
)

@Composable
fun <T, R> SlideScope<T>.createChildScope(transform: (T) -> R): SlideScope<R> {
    val transition = transition.createChildTransition { transform(it) }
    return SlideScope(transition) // TODO remember?
}

