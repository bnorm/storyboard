package dev.bnorm.storyboard.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.rememberTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import dev.bnorm.storyboard.core.*

@Composable
fun StoryboardSlide(storyboard: Storyboard, modifier: Modifier = Modifier) {
    val holder = rememberSaveableStateHolder()
    SlideWrapper(storyboard.size, storyboard.decorator, modifier) {
        key(storyboard.node) { // Helps re-render content when the node forcefully changes.
            rememberTransition(storyboard.node).AnimatedContent(
                transitionSpec = {
                    targetState.enterTransition(storyboard.direction) togetherWith
                            initialState.exitTransition(storyboard.direction)
                }
            ) { slide ->
                @Composable
                fun <T> Content(slide: Slide<T>) {
                    val scope = remember(slide, storyboard, this@AnimatedContent, this@SlideWrapper) {
                        StoryboardSlideScope(slide, storyboard, this@AnimatedContent, this@SlideWrapper)
                    }
                    slide.content(scope)
                }

                holder.SaveableStateProvider(slide) {
                    Box(Modifier.fillMaxSize()) {
                        Content(slide)
                    }
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
                val scope = remember(state, content) {
                    PreviewSlideScope(state, this@AnimatedVisibility, this@SlideWrapper)
                }
                content(scope)
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
    PreviewSlide(slide.states[index].value, slide.content, size, decorator, modifier)
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
