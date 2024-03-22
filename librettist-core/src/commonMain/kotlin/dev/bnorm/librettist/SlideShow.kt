package dev.bnorm.librettist

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.updateTransition
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.bnorm.librettist.show.ShowState
import dev.bnorm.librettist.show.SlideScope
import dev.bnorm.librettist.show.advancements
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
            val slide = showState.currentSlide
            val transition = updateTransition(showState.advancement)
            SlideScope(transition).slide()
        }
    }
}

@Composable
fun ScaledBox(
    targetSize: DpSize,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
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
    CompositionLocalProvider(LocalShowAssistState provides null) {

        // TODO: use state.animateScrollToItem(selectedSlide) somehow to always keep selected slide visible (but not always at the top)
        LazyColumn(modifier = modifier, contentPadding = PaddingValues(8.dp), state = state) {
            items(showState.slides.advancements) { (index, advancement) ->
                val slide = remember(index) { showState.slides[index].content }
                val transition = rememberTransition(MutableTransitionState(advancement))

                ScaledBox(
                    targetSize = slideSize,
                    modifier = Modifier.fillMaxWidth()
                        .padding(8.dp)
                        .aspectRatio(slideSize.width / slideSize.height)
                        .background(MaterialTheme.colors.background)
                        .clickable { showState.jumpToSlide(index, advancement) }
                        .then(
                            if (index == showState.index && advancement == showState.advancement)
                                Modifier.border(2.dp, Color.Red)
                            else Modifier
                        )
                ) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        SlideScope(transition).slide()
                    }
                }
            }
        }
    }
}
