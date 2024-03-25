package dev.bnorm.librettist.show

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.runtime.Composable

// TODO rename to SlideBuilder and add ShowBuilderDsl?
interface SlideScope<out T> {
    // TODO expose the number of states?
    val transition: Transition<out T>
}

typealias SlideContent<T> = @Composable SlideScope<T>.() -> Unit

fun <T> SlideScope(advancement: Transition<T>): SlideScope<T> {
    class SimpleSlideScope(
        override val transition: Transition<T>,
    ) : SlideScope<T>

    return SimpleSlideScope(advancement)
}

@Composable
fun <T, R> SlideScope<T>.createChildScope(transform: (T) -> R): SlideScope<R> {
    val transition = transition.createChildTransition { transform(it) }
    return SlideScope(transition) // TODO remember?
}

