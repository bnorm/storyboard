@file:OptIn(ExperimentalTypeInference::class)

package dev.bnorm.storyboard.easel

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import dev.bnorm.storyboard.core.AdvanceDirection
import dev.bnorm.storyboard.core.SlideScope
import dev.bnorm.storyboard.core.SlideState
import kotlin.experimental.ExperimentalTypeInference
import kotlin.jvm.JvmName

/**
 * Storyboard animations may have to deal with at least two separate things:
 *
 *  1. Visibility (enter, exit) - very similar to standard Compose animation.
 *  2. Direction (forward, backward) - which way is the Storyboard advancing.
 *
 * For example, consider a simple left-to-right animation of an element within a
 * slide: when the slide advances forward, the element will need to enter from
 * the left and then exit to the right. When the slide advances backwards, the
 * element will need to enter from the right and exit to the left.
 *
 * To help simplify a common situation like this, Storyboard considers two
 * edges: start and end. The start is when the element needs to enter from the
 * left when advancing forwards and exit to the left when advancing backwards.
 * The end is when the element needs to exit to the right when advancing
 * forwards and enter from the right when advancing backwards.
 *
 * ```text
 * [     ] --{Enter}-> [     ] ---{Exit}-> [     ] // Forwards
 * [ n-1 ]   (START)   [  n  ]    (END)    [ n+1 ]
 * [     ] <-{Exit}--- [     ] <-{Enter}-- [     ] // Backwards
 * ```
 *
 * This distinction is a mirror of the [SlideScope.state] of a slide, which has
 * a [SlideState.Start] as the first element and a [SlideState.End] as the last
 * element, to help manage transitions between slides.
 */
@Suppress("unused")
private object FileKDoc

inline fun AdvanceDirection.enter(
    start: () -> EnterTransition = { EnterTransition.None },
    end: () -> EnterTransition = { EnterTransition.None },
): EnterTransition = when (this) {
    AdvanceDirection.Forward -> start()
    AdvanceDirection.Backward -> end()
}

inline fun AdvanceDirection.exit(
    start: () -> ExitTransition = { ExitTransition.None },
    end: () -> ExitTransition = { ExitTransition.None },
): ExitTransition = when (this) {
    AdvanceDirection.Forward -> end()
    AdvanceDirection.Backward -> start()
}

inline fun SlideScope<*>.enter(
    start: () -> EnterTransition = { EnterTransition.None },
    end: () -> EnterTransition = { EnterTransition.None },
): EnterTransition = direction.enter(start, end)

inline fun SlideScope<*>.exit(
    start: () -> ExitTransition = { ExitTransition.None },
    end: () -> ExitTransition = { ExitTransition.None },
): ExitTransition = direction.exit(start, end)

@OverloadResolutionByLambdaReturnType
@JvmName("onStart_EnterTransition")
inline fun onStart(
    crossinline animation: (AdvanceDirection) -> EnterTransition,
): (AdvanceDirection) -> EnterTransition = { it.enter(start = { animation(it) }) }

@OverloadResolutionByLambdaReturnType
@JvmName("onStart_ExitTransition")
inline fun onStart(
    crossinline animation: (AdvanceDirection) -> ExitTransition,
): (AdvanceDirection) -> ExitTransition = { it.exit(start = { animation(it) }) }

@OverloadResolutionByLambdaReturnType
@JvmName("onEnd_EnterTransition")
inline fun onEnd(
    crossinline animation: (AdvanceDirection) -> EnterTransition,
): (AdvanceDirection) -> EnterTransition = { it.enter(end = { animation(it) }) }

@OverloadResolutionByLambdaReturnType
@JvmName("onEnd_ExitTransition")
inline fun onEnd(
    crossinline animation: (AdvanceDirection) -> ExitTransition,
): (AdvanceDirection) -> ExitTransition = { it.exit(end = { animation(it) }) }
