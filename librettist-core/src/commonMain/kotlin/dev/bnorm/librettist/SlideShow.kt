package dev.bnorm.librettist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.bnorm.librettist.show.*
import dev.bnorm.librettist.show.assist.LocalShowAssistState

val DEFAULT_SLIDE_SIZE = DpSize(1920.dp, 1080.dp)

@Composable
fun SlideShowDisplay(
    showState: ShowState,
    slideSize: DpSize,
    modifier: Modifier = Modifier,
) {
    ScaledBox(
        targetSize = slideSize,
        modifier = modifier.background(MaterialTheme.colors.background)
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            showState.Present()
        }
    }
}

@Composable
fun ScaledBox(
    targetSize: DpSize,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.layout { measurable, constraints ->
            val fixedSize = Constraints.fixed(targetSize.width.roundToPx(), targetSize.height.roundToPx())
            val scale = minOf(
                constraints.maxWidth.toDp() / targetSize.width,
                constraints.maxHeight.toDp() / targetSize.height,
            )

            val placeable = measurable.measure(fixedSize)
            layout(placeable.width, placeable.height) {
                placeable.placeWithLayer(0, 0, layerBlock = {
                    scaleX = scale
                    scaleY = scale
                })
            }
        }) {
            content()
        }
    }
}

@Composable
fun SlideShowOverview(
    showState: ShowState,
    slideSize: DpSize,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
) {
    val noopUriHandler = object : UriHandler {
        override fun openUri(uri: String) {
        }
    }
    CompositionLocalProvider(LocalShowAssistState provides null, LocalUriHandler provides noopUriHandler) {

        fun jumpToSlide(index: Slide.Index) {
            // TODO: use state.animateScrollToItem(selectedSlide) somehow to always keep selected slide visible (but not always at the top)
            showState.jumpToSlide(index)
        }

        LazyColumn(modifier = modifier, contentPadding = PaddingValues(8.dp), state = state) {
            items(showState.slides.toIndexes()) { index ->
                Box {
                    ScaledBox(
                        targetSize = slideSize,
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp)
                            .aspectRatio(slideSize.width / slideSize.height)
                            .background(MaterialTheme.colors.background)
                            .then(
                                if (showState.currentIndex == index) Modifier.border(2.dp, Color.Red)
                                else Modifier
                            )
                    ) {
                        Surface(modifier = Modifier.fillMaxSize()) {
                            val slide = showState.slides[index.index].content
                            SharedTransitionLayout {
                                AnimatedContent(Unit) {
                                    SlideScope(
                                        SlideState.Index(index.state),
                                        this@AnimatedContent,
                                        this@SharedTransitionLayout
                                    ).slide()
                                }
                            }
                        }
                    }

                    // Add this after slide content, so it covers all click able content
                    // TODO also cannot be within ScaledBox, as column scrolls very strangely
                    Box(modifier = Modifier.matchParentSize().clickable { jumpToSlide(index) })
                }
            }
        }
    }
}
