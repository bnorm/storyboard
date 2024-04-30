package dev.bnorm.librettist.show

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

// TODO rename to SlideBuilder and add ShowBuilderDsl?
class SlideScope<out T>(
    // TODO expose the number of states?
    val transition: Transition<out T>,
)

@Composable
fun <T> SlideScope(value: T): SlideScope<T> {
    return SlideScope(updateTransition(value))
}

@Composable
fun <T, R> SlideScope<T>.createChildScope(transform: (T) -> R): SlideScope<R> {
    val transition = transition.createChildTransition { transform(it) }
    return remember(transition) { SlideScope(transition) }
}
