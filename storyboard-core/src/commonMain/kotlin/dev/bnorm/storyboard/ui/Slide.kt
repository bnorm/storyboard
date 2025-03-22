package dev.bnorm.storyboard.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import dev.bnorm.storyboard.core.AdvanceDirection
import dev.bnorm.storyboard.core.SlideDecorator
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.core.StoryboardSlideScope
import dev.bnorm.storyboard.core.internal.SlideNode

@Composable
fun StoryboardSlide(storyboard: Storyboard, modifier: Modifier = Modifier) {
    // TODO maybe this should be somewhere else...
    LaunchedEffect(storyboard) {
        storyboard.handleEvents()
    }

    val holder = rememberSaveableStateHolder()
    ProvideStoryboard(storyboard) {
        SlideWrapper(storyboard.size, storyboard.decorator, modifier) {
            rememberTransition(storyboard.node).AnimatedContent(
                transitionSpec = {
                    val direction = when {
                        targetState > initialState -> AdvanceDirection.Forward
                        else -> AdvanceDirection.Backward
                    }
                    targetState.slide.enterTransition(direction) togetherWith
                            initialState.slide.exitTransition(direction)
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

@Composable
private fun <T> rememberScope(
    storyboard: Storyboard,
    node: SlideNode<T>,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
): StoryboardSlideScope<T> {
    val transition = rememberTransition(node.stateIndex)
    val stateTransition = transition.createChildTransition { node.states[it] }
    return remember(storyboard, node, stateTransition, animatedContentScope, sharedTransitionScope) {
        StoryboardSlideScope(
            storyboard = storyboard,
            states = node.slide.states,
            state = stateTransition,
            animatedVisibilityScope = animatedContentScope,
            sharedTransitionScope = sharedTransitionScope
        )
    }
}
