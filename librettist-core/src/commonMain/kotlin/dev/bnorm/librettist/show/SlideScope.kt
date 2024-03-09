package dev.bnorm.librettist.show

import androidx.compose.runtime.Composable

interface SlideScope {
    val direction: Advancement.Direction
}

typealias SlideContent = @Composable SlideScope.() -> Unit
