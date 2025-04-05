package dev.bnorm.storyboard.easel.template

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.times
import dev.bnorm.storyboard.core.AdvanceDirection

fun SceneEnter(
    alignment: Alignment,
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(durationMillis = 750),
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
): (AdvanceDirection) -> EnterTransition = { direction ->
    slideIn(animationSpec) { fullSize ->
        val fullSizeOffset = IntOffset(fullSize.width, fullSize.height)
        val offset = alignment.align(fullSize, 3 * fullSize, layoutDirection) - fullSizeOffset
        if (direction == AdvanceDirection.Forward) offset else -offset
    }
}

fun SceneExit(
    alignment: Alignment,
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(durationMillis = 750),
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
): (AdvanceDirection) -> ExitTransition = { direction ->
    slideOut(animationSpec) { fullSize ->
        val fullSizeOffset = IntOffset(fullSize.width, fullSize.height)
        val offset = alignment.align(fullSize, 3 * fullSize, layoutDirection) - fullSizeOffset
        if (direction == AdvanceDirection.Forward) -offset else offset
    }
}
