package dev.bnorm.librettist.show

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import dev.bnorm.librettist.slide.SlidePreview

@Composable
fun ShowOverview(
    showState: ShowState,
    slideSize: DpSize,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    val currentSlide = showState.currentIndex
    val indexes = remember(showState) {
        showState.slides.toIndexes().groupBy { it.index }.entries.toList()
    }

    BoxWithConstraints(modifier = modifier) {
        val boxWithConstraintsScope = this
        val itemSize = boxWithConstraintsScope.toItemSize(slideSize)
        val verticalPaddingDp = (boxWithConstraintsScope.maxHeight - itemSize.height).coerceAtLeast(0.dp) / 2

        val hState = remember {
            val horizontalPaddingDp = (boxWithConstraintsScope.maxWidth - itemSize.width).coerceAtLeast(0.dp) / 2
            val horizontalScrollOffset = with(density) { horizontalPaddingDp.toPx() }.toInt()
            val index = indexes.binarySearch { compareValues(it.key, currentSlide.index) }
            LazyListState(index, -horizontalScrollOffset)
        }
        LaunchedEffect(currentSlide.index) {
            val index = indexes.binarySearch { compareValues(it.key, currentSlide.index) }
            if (index < 0) return@LaunchedEffect
            val horizontalPaddingDp = (boxWithConstraintsScope.maxWidth - itemSize.width).coerceAtLeast(0.dp) / 2
            val horizontalScrollOffset = with(density) { horizontalPaddingDp.toPx() }.toInt()
            hState.animateScrollToItem(index, -horizontalScrollOffset)
        }

        LazyRow(
            state = hState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(indexes) { (index, states) ->
                Box {
                    val vState = rememberLazyListState(if (currentSlide.index == index) currentSlide.state else 0)
                    if (currentSlide.index == index && states.size > 1) {
                        LaunchedEffect(currentSlide) {
                            vState.animateScrollToItem(currentSlide.state)
                        }
                    }

                    LazyColumn(
                        state = vState,
                        userScrollEnabled = states.size > 1,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(vertical = verticalPaddingDp),
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        items(states) { state ->
                            OverviewSlidePreview(
                                showState = showState,
                                index = state,
                                slideSize = slideSize,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope,
                                modifier = Modifier.height(itemSize.height),
                                onClick = { showState.jumpToSlide(state) },
                            )
                        }
                    }
                }
            }
        }

        HorizontalScrollbar(
            adapter = rememberScrollbarAdapter(hState),
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}

@Composable
private fun OverviewSlidePreview(
    showState: ShowState,
    index: Slide.Index,
    slideSize: DpSize,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val isCurrentSlide = showState.currentIndex == index
    val alpha by animateFloatAsState(if (isCurrentSlide) 1f else 0f)
    Box(
        modifier
            .padding(4.dp)
            .border(2.dp, MaterialTheme.colors.primary.copy(alpha = alpha), RoundedCornerShape(6.dp))
            .padding(8.dp)
            .aspectRatio(slideSize.width / slideSize.height)
    ) {
        with(sharedTransitionScope) {
            val sharedElementModifier = when (isCurrentSlide) {
                false -> Modifier
                true -> Modifier.sharedElement(
                    state = rememberSharedContentState(key = "slide:current"),
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            }

            SlidePreview(
                slide = remember(showState, index) { showState.slides[index.index] },
                state = index.state,
                slideSize = slideSize,
                modifier = sharedElementModifier.matchParentSize()
            )
        }

        // Add this after slide content, so it covers all slide clickable content
        // TODO also cannot be within ScaledBox, as column scrolls very strangely
        Box(
            Modifier.fillMaxSize()
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable(
                    indication = null, // disable ripple effect
                    interactionSource = null,
                    onClick = onClick
                )
        )
    }
}

private fun BoxWithConstraintsScope.toItemSize(
    size: DpSize,
    scale: Float = 1f / 5f,
    min: Dp = 150.dp,
    max: Dp = 250.dp,
): DpSize {
    val aspectRatio = size.width / size.height
    val height = (scale * maxHeight).coerceIn(min, max)
    val width = (scale * maxWidth).coerceIn(min, max)

    return if (width / aspectRatio < height * aspectRatio) {
        DpSize(height = height, width = height * aspectRatio)
    } else {
        DpSize(height = width / aspectRatio, width = width)
    }
}

fun Modifier.onOverviewNavigation(showState: ShowState): Modifier {
    return onPreviewKeyEvent { event ->
        val currentSlide = showState.currentIndex
        val indexes = showState.slides.toIndexes().groupBy { it.index }.entries.toList()

        if (event.type == KeyEventType.KeyDown) {
            val index = when (event.key) {
                Key.DirectionRight -> {
                    val i = indexes.binarySearch { compareValues(it.key, currentSlide.index) }
                    val next = indexes[(i + 1).coerceIn(indexes.indices)].key
                    Slide.Index(next, 0)
                }

                Key.DirectionLeft -> {
                    val i = indexes.binarySearch { compareValues(it.key, currentSlide.index) }
                    val next = indexes[(i - 1).coerceIn(indexes.indices)].key
                    Slide.Index(next, 0)
                }

                Key.DirectionDown -> {
                    val state = currentSlide.state
                    val next = (state + 1)
                    Slide.Index(currentSlide.index, next)
                }

                Key.DirectionUp -> {
                    val state = currentSlide.state
                    val next = (state - 1)
                    Slide.Index(currentSlide.index, next)
                }

                else -> return@onPreviewKeyEvent false
            }

            showState.jumpToSlide(index)
            return@onPreviewKeyEvent true
        }

        return@onPreviewKeyEvent false
    }
}
