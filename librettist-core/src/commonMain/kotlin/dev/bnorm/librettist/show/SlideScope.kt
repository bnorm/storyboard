package dev.bnorm.librettist.show

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Composable

// TODO rename to SlideBuilder and add ShowBuilderDsl?
interface SlideScope {
    val transition: Transition<Int>
}

typealias SlideContent = @Composable SlideScope.() -> Unit

fun SlideScope(advancement: Transition<Int>): SlideScope {
    class SimpleSlideScope(
        override val transition: Transition<Int>,
    ) : SlideScope

    return SimpleSlideScope(advancement)
}
