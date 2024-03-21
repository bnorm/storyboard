package dev.bnorm.librettist.show

import androidx.compose.runtime.Composable

// TODO rename to SlideBuilder and add ShowBuilderDsl?
interface SlideScope {
    val advancement: Int
}

typealias SlideContent = @Composable SlideScope.() -> Unit

fun SlideScope(advancement: Int): SlideScope {
    class SimpleSlideScope(
        override val advancement: Int,
    ) : SlideScope

    return SimpleSlideScope(advancement)
}
