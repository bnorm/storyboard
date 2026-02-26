package dev.bnorm.storyboard.layout.template

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import dev.bnorm.storyboard.AdvanceDirection
import dev.bnorm.storyboard.SceneEnterTransition
import dev.bnorm.storyboard.SceneExitTransition

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
 * This distinction is the same as the [dev.bnorm.storyboard.SceneScope.transition] of a scene, which
 * has a [dev.bnorm.storyboard.Frame.Start] as the first element and a [dev.bnorm.storyboard.Frame.End] as the last
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

fun enter(
    start: SceneEnterTransition = SceneEnterTransition.None,
    end: SceneEnterTransition = SceneEnterTransition.None,
): SceneEnterTransition = SceneEnterTransition { direction ->
    direction.enter(
        { start(direction) },
        { end(direction) },
    )
}

fun exit(
    start: SceneExitTransition = SceneExitTransition.None,
    end: SceneExitTransition = SceneExitTransition.None,
): SceneExitTransition = SceneExitTransition { direction ->
    direction.exit(
        { start(direction) },
        { end(direction) },
    )
}
