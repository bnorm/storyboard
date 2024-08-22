package dev.bnorm.storyboard.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import dev.bnorm.storyboard.core.*
import dev.bnorm.storyboard.core.internal.SlideNode
import kotlinx.collections.immutable.persistentListOf

@Composable
fun StoryboardSlide(storyboard: Storyboard, modifier: Modifier = Modifier) {
    // TODO maybe this should be somewhere else...
    LaunchedEffect(storyboard) {
        storyboard.handleEvents()
    }

    val holder = rememberSaveableStateHolder()
    SlideWrapper(storyboard.size, storyboard.decorator, modifier) {
        rememberTransition(storyboard.node).AnimatedContent(
            transitionSpec = {
                val direction = if (targetState > initialState) AdvanceDirection.Forward else AdvanceDirection.Backward
                targetState.slide.enterTransition(direction) togetherWith initialState.slide.exitTransition(direction)
            }
        ) { node ->
            @Composable
            fun <T> Content(node: SlideNode<T>) {
                val scope = rememberScope(storyboard, node, this@AnimatedContent, this@SlideWrapper)
                node.slide.content(scope)
            }

            holder.SaveableStateProvider(node) {
                Box(Modifier.fillMaxSize()) {
                    Content(node)
                }
            }
        }
    }
}

@Composable
fun <T> PreviewSlide(
    state: T,
    content: SlideContent<T>,
    size: DpSize,
    decorator: SlideDecorator = SlideDecorator.None,
    modifier: Modifier = Modifier,
) {
    SlideWrapper(size, decorator, modifier) {
        AnimatedVisibility(true) {
            Box(Modifier.fillMaxSize()) {
                key(state, content) {
                    val scope = PreviewSlideScope(
                        states = persistentListOf(state),
                        transition = updateTransition(SlideState.Value<T>(state)),
                        animatedVisibilityScope = this@AnimatedVisibility,
                        sharedTransitionScope = this@SlideWrapper
                    )
                    content(scope)
                }
            }
        }
    }
}

@Composable
fun <T> PreviewSlide(
    slide: Slide<T>,
    index: Int,
    size: DpSize,
    decorator: SlideDecorator = SlideDecorator.None,
    modifier: Modifier = Modifier,
) {
    PreviewSlide(
        state = slide.states[index],
        content = slide.content,
        size = size,
        decorator = decorator,
        modifier = modifier
    )
}

@Composable
fun PreviewSlide(
    storyboard: Storyboard,
    frame: Storyboard.Frame,
    modifier: Modifier = Modifier,
) {
    PreviewSlide(
        slide = storyboard.slides[frame.slideIndex],
        index = frame.stateIndex,
        size = storyboard.size,
        decorator = storyboard.decorator,
        modifier = modifier,
    )
}

@Composable
private fun SlideWrapper(
    size: DpSize,
    decorator: SlideDecorator,
    modifier: Modifier = Modifier,
    content: @Composable SharedTransitionScope.() -> Unit,
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        FixedSize(size = size) {
            decorator.decorate {
                Surface {
                    SharedTransitionLayout {
                        content()
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> rememberScope(
    storyboard: Storyboard,
    node: SlideNode<T>,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
): StoryboardSlideScope<T> {
    val transition = rememberTransition(node.stateIndex)
    DisposableEffect(transition) {
        onDispose {
            // TODO fix a bug in Compose where the transition is not cleaned up properly.
            // Fixed in recent versions of Jetpack Compose; but not yet in Compose Multiplatform.
            @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
            transition.onDisposed()
        }
    }

    val stateTransition = transition.createChildTransition { node.states[it] }
    return remember(storyboard, node, stateTransition, animatedContentScope, sharedTransitionScope) {
        StoryboardSlideScope(
            storyboard = storyboard,
            states = node.slide.states,
            transition = stateTransition,
            animatedVisibilityScope = animatedContentScope,
            sharedTransitionScope = sharedTransitionScope
        )
    }
}
