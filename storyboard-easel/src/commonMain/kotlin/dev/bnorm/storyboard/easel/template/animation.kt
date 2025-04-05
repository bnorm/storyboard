@file:OptIn(ExperimentalTypeInference::class)

package dev.bnorm.storyboard.easel.template

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import dev.bnorm.storyboard.AdvanceDirection
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.SceneScope
import kotlin.experimental.ExperimentalTypeInference

/**
 * Storyboard animations may have to deal with at least two separate things:
 *
 *  1. Visibility (enter, exit) - very similar to standard Compose animation.
 *  2. Direction (forward, backward) - which way is the Storyboard advancing.
 *
 * For example, consider a simple left-to-right animation of an element within a
 * scene: when the scene advances forward, the element will need to enter from
 * the left and then exit to the right. When the scene advances backwards, the
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
 * This distinction is the same as the [SceneScope.frame] of a scene, which has
 * a [Frame.Start] as the first element and a [Frame.End] as the last
 * element, to help manage transitions between scenes.
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

inline fun enter(
    crossinline start: (AdvanceDirection) -> EnterTransition = { EnterTransition.None },
    crossinline end: (AdvanceDirection) -> EnterTransition = { EnterTransition.None },
): (AdvanceDirection) -> EnterTransition = { direction ->
    direction.enter(
        { start(direction) },
        { end(direction) },
    )
}

inline fun exit(
    crossinline start: (AdvanceDirection) -> ExitTransition = { ExitTransition.None },
    crossinline end: (AdvanceDirection) -> ExitTransition = { ExitTransition.None },
): (AdvanceDirection) -> ExitTransition = { direction ->
    direction.exit(
        { start(direction) },
        { end(direction) },
    )
}
