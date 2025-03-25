package dev.bnorm.storyboard.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import dev.bnorm.storyboard.core.*

@Composable
fun StoryboardScene(storyboard: StoryboardState, modifier: Modifier = Modifier) {
    val holder = rememberSaveableStateHolder()
    ProvideStoryboard(storyboard.storyboard) {
        SceneWrapper(storyboard.storyboard.size, storyboard.storyboard.decorator, modifier) {
            val frame = storyboard.rememberTransition()
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
                        SceneContent(storyboard, scene, frame, this@AnimatedContent, this@SceneWrapper)
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> SceneContent(
    storyboard: StoryboardState,
    stateScene: StoryboardState.StateScene<T>,
    frame: Transition<StoryboardState.StateFrame<*>>,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    val state = frame.createChildTransition {
        @Suppress("UNCHECKED_CAST")
        when {
            stateScene > it.scene -> Frame.Start
            stateScene < it.scene -> Frame.End
            else -> it.state as Frame<T>
        }
    }

    val scope = remember(storyboard, stateScene, state, animatedContentScope, sharedTransitionScope) {
        StoryboardSceneScope(
            storyboard = storyboard,
            states = stateScene.scene.states,
            frame = state,
            animatedVisibilityScope = animatedContentScope,
            sharedTransitionScope = sharedTransitionScope
        )
    }

    stateScene.scene.content(scope)
}

@Composable
internal fun SceneWrapper(
    size: DpSize,
    decorator: SceneDecorator,
    modifier: Modifier = Modifier,
    content: @Composable SharedTransitionScope.() -> Unit,
) {
    FixedSize(size = size, modifier = modifier, contentAlignment = Alignment.Center) {
        decorator.decorate {
            SharedTransitionLayout {
                content()
            }
        }
    }
}
