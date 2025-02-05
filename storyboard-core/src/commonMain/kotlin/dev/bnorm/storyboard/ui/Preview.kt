package dev.bnorm.storyboard.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.core.*
import kotlinx.collections.immutable.persistentListOf

@Composable
fun <T> SlidePreview(
    content: SlideContent<T>,
    state: T,
    size: DpSize = Storyboard.DEFAULT_SIZE,
    decorator: SlideDecorator = SlideDecorator.None,
    modifier: Modifier = Modifier,
) {
    SlideWrapper(size, decorator, modifier.aspectRatio(size.width / size.height)) {
        AnimatedVisibility(true) {
            Box(Modifier.fillMaxSize()) {
                key(content, state) {
                    val scope = PreviewSlideScope(
                        states = persistentListOf(state),
                        state = updateTransition(SlideState.Value<T>(state)),
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
fun <T> SlidePreview(
    slide: Slide<T>,
    stateIndex: Int,
    size: DpSize = Storyboard.DEFAULT_SIZE,
    decorator: SlideDecorator = SlideDecorator.None,
    modifier: Modifier = Modifier,
) {
    SlideWrapper(size, decorator, modifier.aspectRatio(size.width / size.height)) {
        AnimatedVisibility(true) {
            Box(Modifier.fillMaxSize()) {
                key(slide, stateIndex) {
                    val scope = PreviewSlideScope(
                        states = slide.states,
                        state = updateTransition(SlideState.Value(slide.states[stateIndex])),
                        animatedVisibilityScope = this@AnimatedVisibility,
                        sharedTransitionScope = this@SlideWrapper
                    )
                    slide.content(scope)
                }
            }
        }
    }
}

@Composable
fun <T> SlidePreview(
    slide: Slide<T>,
    state: SlideState<Nothing>,
    size: DpSize = Storyboard.DEFAULT_SIZE,
    decorator: SlideDecorator = SlideDecorator.None,
    modifier: Modifier = Modifier,
) {
    SlideWrapper(size, decorator, modifier.aspectRatio(size.width / size.height)) {
        AnimatedVisibility(true) {
            Box(Modifier.fillMaxSize()) {
                key(slide, state) {
                    val scope = PreviewSlideScope(
                        states = slide.states,
                        state = updateTransition(state),
                        animatedVisibilityScope = this@AnimatedVisibility,
                        sharedTransitionScope = this@SlideWrapper
                    )
                    slide.content(scope)
                }
            }
        }
    }
}

@Composable
fun SlidePreview(
    storyboard: Storyboard,
    frame: Storyboard.Frame,
    modifier: Modifier = Modifier,
) {
    SlidePreview(
        slide = storyboard.slides[frame.slideIndex],
        stateIndex = frame.stateIndex,
        size = storyboard.size,
        decorator = storyboard.decorator,
        modifier = modifier,
    )
}

@Composable
fun StoryboardPreview(storyboard: Storyboard) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState())
            .background(Color.Transparent)
    ) {
        for (slide in storyboard.slides) {
            Text("State: Start")
            SlidePreview(slide, SlideState.Start, storyboard.size, storyboard.decorator)

            for (stateIndex in slide.states.indices) {
                Text("State: $stateIndex")
                SlidePreview(slide, stateIndex, storyboard.size, storyboard.decorator)
            }

            Text("State: End")
            SlidePreview(slide, SlideState.End, storyboard.size, storyboard.decorator)
        }
    }
}

@Composable
fun StoryboardPreview(
    name: String = "Preview",
    description: String? = null,
    size: DpSize = Storyboard.DEFAULT_SIZE,
    decorator: SlideDecorator = SlideDecorator.None,
    block: StoryboardBuilder.() -> Unit,
) {
    StoryboardPreview(Storyboard.build(name, description, size, decorator, block))
}
