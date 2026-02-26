package dev.bnorm.storyboard.layout.template

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.times
import dev.bnorm.storyboard.AdvanceDirection
import dev.bnorm.storyboard.SceneEnterTransition
import dev.bnorm.storyboard.SceneExitTransition

fun SceneEnter(
    alignment: Alignment,
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(durationMillis = 750),
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
): SceneEnterTransition = SceneEnterTransition { direction ->
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
): SceneExitTransition = SceneExitTransition { direction ->
    slideOut(animationSpec) { fullSize ->
        val fullSizeOffset = IntOffset(fullSize.width, fullSize.height)
        val offset = alignment.align(fullSize, 3 * fullSize, layoutDirection) - fullSizeOffset
        if (direction == AdvanceDirection.Forward) -offset else offset
    }
}
