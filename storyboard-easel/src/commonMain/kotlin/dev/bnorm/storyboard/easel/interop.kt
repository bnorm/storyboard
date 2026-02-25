package dev.bnorm.storyboard.easel

import androidx.compose.animation.*
import androidx.compose.animation.SharedTransitionScope.*
import androidx.compose.animation.SharedTransitionScope.PlaceholderSize.Companion.ContentSize
import androidx.compose.animation.SharedTransitionScope.ResizeMode.Companion.scaleToBounds
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

// ============================ //
// ----- animateEnterExit ----- //
// ============================ //

context(animatedVisibilityScope: AnimatedVisibilityScope)
fun Modifier.animateEnterExit(
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    label: String = "animateEnterExit",
): Modifier {
    with(animatedVisibilityScope) {
        return animateEnterExit(
            enter = enter,
            exit = exit,
            label = label,
        )
    }
}

// ========================= //
// ----- sharedElement ----- //
// ========================= //

@Composable
context(sharedTransitionScope: SharedTransitionScope)
fun rememberSharedContentState(key: Any): SharedContentState {
    return sharedTransitionScope.rememberSharedContentState(key)
}

@Composable
context(sharedTransitionScope: SharedTransitionScope)
fun rememberSharedContentState(key: Any, config: SharedContentConfig): SharedContentState {
    return sharedTransitionScope.rememberSharedContentState(key, config)
}

// ========================= //
// ----- sharedElement ----- //
// ========================= //

context(sharedTransitionScope: SharedTransitionScope)
fun Modifier.sharedElement(
    sharedContentState: SharedContentState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    boundsTransform: BoundsTransform = SharedTransitionDefaults.BoundsTransform,
    placeholderSize: PlaceholderSize = ContentSize,
    renderInOverlayDuringTransition: Boolean = true,
    zIndexInOverlay: Float = 0f,
): Modifier {
    with(sharedTransitionScope) {
        return sharedElement(
            sharedContentState = sharedContentState,
            animatedVisibilityScope = animatedVisibilityScope,
            boundsTransform = boundsTransform,
            placeholderSize = placeholderSize,
            renderInOverlayDuringTransition = renderInOverlayDuringTransition,
            zIndexInOverlay = zIndexInOverlay,
        )
    }
}

context(sharedTransitionScope: SharedTransitionScope)
fun Modifier.sharedElement(
    sharedContentState: SharedContentState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    boundsTransform: BoundsTransform = SharedTransitionDefaults.BoundsTransform,
    placeholderSize: PlaceholderSize = ContentSize,
    renderInOverlayDuringTransition: Boolean = true,
    zIndexInOverlay: Float = 0f,
    clipInOverlayDuringTransition: OverlayClip,
): Modifier {
    with(sharedTransitionScope) {
        return sharedElement(
            sharedContentState,
            animatedVisibilityScope,
            boundsTransform,
            placeholderSize,
            renderInOverlayDuringTransition,
            zIndexInOverlay,
            clipInOverlayDuringTransition
        )
    }
}

context(animatedVisibilityScope: AnimatedVisibilityScope, sharedTransitionScope: SharedTransitionScope)
fun Modifier.sharedElement(
    sharedContentState: SharedContentState,
    boundsTransform: BoundsTransform = SharedTransitionDefaults.BoundsTransform,
    placeholderSize: PlaceholderSize = ContentSize,
    renderInOverlayDuringTransition: Boolean = true,
    zIndexInOverlay: Float = 0f,
): Modifier {
    with(sharedTransitionScope) {
        return sharedElement(
            sharedContentState = sharedContentState,
            animatedVisibilityScope = animatedVisibilityScope,
            boundsTransform = boundsTransform,
            placeholderSize = placeholderSize,
            renderInOverlayDuringTransition = renderInOverlayDuringTransition,
            zIndexInOverlay = zIndexInOverlay,
        )
    }
}

context(animatedVisibilityScope: AnimatedVisibilityScope, sharedTransitionScope: SharedTransitionScope)
fun Modifier.sharedElement(
    sharedContentState: SharedContentState,
    boundsTransform: BoundsTransform = SharedTransitionDefaults.BoundsTransform,
    placeholderSize: PlaceholderSize = ContentSize,
    renderInOverlayDuringTransition: Boolean = true,
    zIndexInOverlay: Float = 0f,
    clipInOverlayDuringTransition: OverlayClip,
): Modifier {
    with(sharedTransitionScope) {
        return sharedElement(
            sharedContentState,
            animatedVisibilityScope,
            boundsTransform,
            placeholderSize,
            renderInOverlayDuringTransition,
            zIndexInOverlay,
            clipInOverlayDuringTransition
        )
    }
}

// ======================== //
// ----- sharedBounds ----- //
// ======================== //

context(sharedTransitionScope: SharedTransitionScope)
fun Modifier.sharedBounds(
    sharedContentState: SharedContentState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    boundsTransform: BoundsTransform = SharedTransitionDefaults.BoundsTransform,
    resizeMode: ResizeMode = scaleToBounds(ContentScale.FillWidth, Center),
    placeholderSize: PlaceholderSize = ContentSize,
    renderInOverlayDuringTransition: Boolean = true,
    zIndexInOverlay: Float = 0f,
): Modifier {
    with(sharedTransitionScope) {
        return sharedBounds(
            sharedContentState = sharedContentState,
            animatedVisibilityScope = animatedVisibilityScope,
            enter = enter,
            exit = exit,
            boundsTransform = boundsTransform,
            resizeMode = resizeMode,
            placeholderSize = placeholderSize,
            renderInOverlayDuringTransition = renderInOverlayDuringTransition,
            zIndexInOverlay = zIndexInOverlay,
        )
    }
}

context(sharedTransitionScope: SharedTransitionScope)
fun Modifier.sharedBounds(
    sharedContentState: SharedContentState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    boundsTransform: BoundsTransform = SharedTransitionDefaults.BoundsTransform,
    resizeMode: ResizeMode = scaleToBounds(ContentScale.FillWidth, Center),
    placeholderSize: PlaceholderSize = ContentSize,
    renderInOverlayDuringTransition: Boolean = true,
    zIndexInOverlay: Float = 0f,
    clipInOverlayDuringTransition: OverlayClip,
): Modifier {
    with(sharedTransitionScope) {
        return sharedBounds(
            sharedContentState = sharedContentState,
            animatedVisibilityScope = animatedVisibilityScope,
            enter = enter,
            exit = exit,
            boundsTransform = boundsTransform,
            resizeMode = resizeMode,
            placeholderSize = placeholderSize,
            renderInOverlayDuringTransition = renderInOverlayDuringTransition,
            zIndexInOverlay = zIndexInOverlay,
            clipInOverlayDuringTransition = clipInOverlayDuringTransition,
        )
    }
}

context(animatedVisibilityScope: AnimatedVisibilityScope, sharedTransitionScope: SharedTransitionScope)
fun Modifier.sharedBounds(
    sharedContentState: SharedContentState,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    boundsTransform: BoundsTransform = SharedTransitionDefaults.BoundsTransform,
    resizeMode: ResizeMode = scaleToBounds(ContentScale.FillWidth, Center),
    placeholderSize: PlaceholderSize = ContentSize,
    renderInOverlayDuringTransition: Boolean = true,
    zIndexInOverlay: Float = 0f,
): Modifier {
    with(sharedTransitionScope) {
        return sharedBounds(
            sharedContentState = sharedContentState,
            animatedVisibilityScope = animatedVisibilityScope,
            enter = enter,
            exit = exit,
            boundsTransform = boundsTransform,
            resizeMode = resizeMode,
            placeholderSize = placeholderSize,
            renderInOverlayDuringTransition = renderInOverlayDuringTransition,
            zIndexInOverlay = zIndexInOverlay,
        )
    }
}

context(animatedVisibilityScope: AnimatedVisibilityScope, sharedTransitionScope: SharedTransitionScope)
fun Modifier.sharedBounds(
    sharedContentState: SharedContentState,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    boundsTransform: BoundsTransform = SharedTransitionDefaults.BoundsTransform,
    resizeMode: ResizeMode = scaleToBounds(ContentScale.FillWidth, Center),
    placeholderSize: PlaceholderSize = ContentSize,
    renderInOverlayDuringTransition: Boolean = true,
    zIndexInOverlay: Float = 0f,
    clipInOverlayDuringTransition: OverlayClip,
): Modifier {
    with(sharedTransitionScope) {
        return sharedBounds(
            sharedContentState = sharedContentState,
            animatedVisibilityScope = animatedVisibilityScope,
            enter = enter,
            exit = exit,
            boundsTransform = boundsTransform,
            resizeMode = resizeMode,
            placeholderSize = placeholderSize,
            renderInOverlayDuringTransition = renderInOverlayDuringTransition,
            zIndexInOverlay = zIndexInOverlay,
            clipInOverlayDuringTransition = clipInOverlayDuringTransition,
        )
    }
}
