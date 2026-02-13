package dev.bnorm.storyboard.easel

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.bnorm.storyboard.Storyboard

@Composable
fun rememberAnimatic(
    storyboard: () -> Storyboard,
): Animatic {
    val state = remember { AnimaticInternal() }
    // TODO this all seems ugly...
    remember(storyboard) { storyboard().also { state.updateStoryboard(it) } }
    val transition = state.rememberTransition()
    return remember(state, transition) { Animatic(state, transition) }
}

// TODO redo architecture of Animatic, AnimaticInternal, and StoryController
class Animatic internal constructor(
    internal val state: AnimaticInternal,
    val transition: Transition<SceneFrame<*>>,
) : StoryController by state
