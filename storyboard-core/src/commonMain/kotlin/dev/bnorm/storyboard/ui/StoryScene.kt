package dev.bnorm.storyboard.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import dev.bnorm.storyboard.core.*

@Composable
fun StoryScene(storyState: StoryState, modifier: Modifier = Modifier) {
    val holder = rememberSaveableStateHolder()
    CompositionLocalProvider(
        LocalStoryState provides storyState,
        LocalStoryboard provides storyState.storyboard,
    ) {
        SceneWrapper(storyState.storyboard.size, storyState.storyboard.decorator, DisplayType.Story, modifier) {
            val frame = storyState.rememberTransition()
            frame.createChildTransition { it.scene }.AnimatedContent(
                transitionSpec = {
                    val direction = when {
                        targetState > initialState -> AdvanceDirection.Forward
                        else -> AdvanceDirection.Backward
                    }
                    targetState.scene.enterTransition(direction) togetherWith
                            initialState.scene.exitTransition(direction)
                }
            ) { scene ->
                holder.SaveableStateProvider(scene) {
                    Box(Modifier.fillMaxSize()) {
                        SceneContent(storyState, scene, frame)
                    }
                }
            }
        }
    }
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun <T> SceneContent(
    storyState: StoryState,
    stateScene: StoryState.StateScene<T>,
    frame: Transition<StoryState.StateFrame<*>>,
) {
    val state = frame.createChildTransition {
        @Suppress("UNCHECKED_CAST")
        when {
            stateScene > it.scene -> Frame.Start
            stateScene < it.scene -> Frame.End
            else -> it.frame as Frame<T>
        }
    }

    val scope = remember(storyState, stateScene, state) {
        StoryboardSceneScope(
            storyState = storyState,
            states = stateScene.scene.states,
            frame = state,
        )
    }

    stateScene.scene.content(scope)
}

@Composable
internal fun SceneWrapper(
    size: DpSize,
    decorator: SceneDecorator,
    displayType: DisplayType,
    modifier: Modifier = Modifier,
    content: @Composable context(SharedTransitionScope) () -> Unit,
) {
    FixedSize(size = size, modifier = modifier) {
        CompositionLocalProvider(LocalDisplayType provides displayType) {
            decorator.decorate {
                SharedTransitionLayout {
                    content()
                }
            }
        }
    }
}
