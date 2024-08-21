@file:OptIn(ExperimentalTypeInference::class)

package dev.bnorm.storyboard.easel

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.bnorm.storyboard.core.AdvanceDirection
import dev.bnorm.storyboard.core.SlideScope
import kotlinx.coroutines.CoroutineScope
import kotlin.experimental.ExperimentalTypeInference
import kotlin.jvm.JvmName

fun Transition<*>.isIdle(): Boolean =
    targetState == currentState && !isRunning

@Composable
inline fun SlideScope<*>.LaunchedIdleEffect(
    vararg keys: Any?,
    crossinline block: suspend CoroutineScope.() -> Unit,
) {
    val idle = animatedVisibilityScope.transition.isIdle()
    LaunchedEffect(idle, *keys) {
        if (!idle) return@LaunchedEffect
        block()
    }
}

val SlideEnter: (AdvanceDirection) -> EnterTransition = { direction ->
    slideInHorizontally(animationSpec = tween(durationMillis = 750)) {
        if (direction == AdvanceDirection.Forward) it else -it
    }
}

val SlideExit: (AdvanceDirection) -> ExitTransition = { direction ->
    slideOutHorizontally(animationSpec = tween(durationMillis = 750)) {
        if (direction == AdvanceDirection.Forward) -it else it
    }
}

@OverloadResolutionByLambdaReturnType
@JvmName("onSlideEnter_EnterTransition")
inline fun SlideScope<*>.onSlideEnter(
    animation: () -> EnterTransition,
): EnterTransition = when (direction) {
    AdvanceDirection.Forward -> animation()
    AdvanceDirection.Backward -> EnterTransition.None
}

@OverloadResolutionByLambdaReturnType
@JvmName("onSlideEnter_ExitTransition")
inline fun SlideScope<*>.onSlideEnter(
    animation: () -> ExitTransition,
): ExitTransition = when (direction) {
    AdvanceDirection.Forward -> ExitTransition.None
    AdvanceDirection.Backward -> animation()
}

@OverloadResolutionByLambdaReturnType
@JvmName("onSlideExit_EnterTransition")
inline fun SlideScope<*>.onSlideExit(
    animation: () -> EnterTransition,
): EnterTransition = when (direction) {
    AdvanceDirection.Forward -> EnterTransition.None
    AdvanceDirection.Backward -> animation()
}

@OverloadResolutionByLambdaReturnType
@JvmName("onSlideExit_ExitTransition")
inline fun SlideScope<*>.onSlideExit(
    animation: () -> ExitTransition,
): ExitTransition = when (direction) {
    AdvanceDirection.Forward -> animation()
    AdvanceDirection.Backward -> ExitTransition.None
}
