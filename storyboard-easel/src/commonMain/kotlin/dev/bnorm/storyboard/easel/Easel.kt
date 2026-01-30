package dev.bnorm.storyboard.easel

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.bnorm.storyboard.Storyboard

@Composable
fun rememberEasel(
    storyboard: () -> Storyboard,
): Easel {
    val state = remember { StoryState() }
    // TODO this all seems ugly...
    remember(storyboard) { storyboard().also { state.updateStoryboard(it) } }
    val transition = state.rememberTransition()
    return remember(state, transition) { Easel(state, transition) }
}

class Easel internal constructor(
    internal val state: StoryState,
    val transition: Transition<SceneFrame<*>>,
) : StoryController by state
