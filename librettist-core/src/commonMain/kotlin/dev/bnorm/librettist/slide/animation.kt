@file:OptIn(ExperimentalTypeInference::class)

package dev.bnorm.librettist.slide

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.runtime.Composable
import dev.bnorm.librettist.show.AdvanceDirection
import dev.bnorm.librettist.show.SlideScope
import dev.bnorm.librettist.show.SlideState
import kotlin.experimental.ExperimentalTypeInference
import kotlin.jvm.JvmName

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

@Composable
fun SlideScope<*>.createSlideTransition(): Transition<SlideState<*>> {
    return animatedContentScope.transition.createChildTransition {
        when (it) {
            EnterExitState.PreEnter -> when (direction) {
                AdvanceDirection.Forward -> SlideState.Entering
                AdvanceDirection.Backward -> SlideState.Exiting
            }

            EnterExitState.Visible -> SlideState.Index(0)

            EnterExitState.PostExit -> when (direction) {
                AdvanceDirection.Forward -> SlideState.Exiting
                AdvanceDirection.Backward -> SlideState.Entering
            }
        }
    }
}