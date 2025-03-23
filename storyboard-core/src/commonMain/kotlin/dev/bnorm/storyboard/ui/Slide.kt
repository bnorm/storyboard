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
fun StoryboardSlide(storyboard: StoryboardState, modifier: Modifier = Modifier) {
    val holder = rememberSaveableStateHolder()
    ProvideStoryboard(storyboard.storyboard) {
        SlideWrapper(storyboard.storyboard.size, storyboard.storyboard.decorator, modifier) {
            val frame = storyboard.rememberTransition()
            frame.createChildTransition { it.scene }.AnimatedContent(
                transitionSpec = {
                    val direction = when {
                        targetState > initialState -> AdvanceDirection.Forward
                        else -> AdvanceDirection.Backward
                    }
                    targetState.slide.enterTransition(direction) togetherWith
                            initialState.slide.exitTransition(direction)
                }
            ) { scene ->
                holder.SaveableStateProvider(scene) {
                    Box(Modifier.fillMaxSize()) {
                        SceneContent(storyboard, scene, frame, this@AnimatedContent, this@SlideWrapper)
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
            stateScene > it.scene -> SlideState.Start
            stateScene < it.scene -> SlideState.End
            else -> it.state as SlideState<T>
        }
    }

    val scope = remember(storyboard, stateScene, state, animatedContentScope, sharedTransitionScope) {
        StoryboardSlideScope(
            storyboard = storyboard,
            states = stateScene.slide.states,
            state = state,
            animatedVisibilityScope = animatedContentScope,
            sharedTransitionScope = sharedTransitionScope
        )
    }

    stateScene.slide.content(scope)
}

@Composable
internal fun SlideWrapper(
    size: DpSize,
    decorator: SlideDecorator,
    modifier: Modifier = Modifier,
    content: @Composable SharedTransitionScope.() -> Unit,
) {
    // TODO i don't like that this box is part of the "slide"
    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
        FixedSize(size = size) {
            decorator.decorate {
                SharedTransitionLayout {
                    content()
                }
            }
        }
    }
}
