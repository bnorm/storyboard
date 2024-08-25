package dev.bnorm.storyboard.easel.template

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import dev.bnorm.storyboard.core.AdvanceDirection

val SlideRtlEnter: (AdvanceDirection) -> EnterTransition = { direction ->
    slideInHorizontally(animationSpec = tween(durationMillis = 750)) {
        if (direction == AdvanceDirection.Forward) it else -it
    }
}

val SlideRtlExit: (AdvanceDirection) -> ExitTransition = { direction ->
    slideOutHorizontally(animationSpec = tween(durationMillis = 750)) {
        if (direction == AdvanceDirection.Forward) -it else it
    }
}
