package dev.bnorm.storyboard.easel

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import dev.bnorm.storyboard.core.Slide
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.easel.internal.requestFocus
import dev.bnorm.storyboard.ui.PreviewSlide
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun StoryboardOverview(
    storyboard: Storyboard,
    onExitOverview: () -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    val currentFrame = storyboard.currentFrame
    val overview = remember(storyboard) { StoryboardOverview.of(storyboard) }

    BoxWithConstraints(modifier = modifier.onOverviewNavigation(storyboard, overview)) {
        val boxWithConstraintsScope = this
        val itemSize = boxWithConstraintsScope.toItemSize(storyboard.size)
        val verticalPaddingDp = (boxWithConstraintsScope.maxHeight - itemSize.height).coerceAtLeast(0.dp) / 2
        val horizontalPaddingDp = (boxWithConstraintsScope.maxWidth - itemSize.width).coerceAtLeast(0.dp) / 2
        val horizontalScrollOffset = with(density) { horizontalPaddingDp.toPx() }

        val hIndex = remember(currentFrame.slideIndex) {
            overview.columns.binarySearch { compareValues(it.index, currentFrame.slideIndex) }.coerceAtLeast(0)
        }
        val hState = rememberLazyListState(hIndex, -horizontalScrollOffset.toInt())
        LaunchedEffect(currentFrame.slideIndex) { hState.animateScrollToItem(hIndex, -horizontalScrollOffset.toInt()) }

        LazyRow(
            state = hState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(overview.columns) { column ->
                val vIndex = rememberSaveable(column.slide.currentIndex) {
                    column.items.binarySearch { compareValues(it.frame.stateIndex, column.slide.currentIndex) }
                        .coerceAtLeast(0)
                }
                val vState = rememberLazyListState(vIndex)
                LaunchedEffect(vIndex) { vState.animateScrollToItem(vIndex) }

                LazyColumn(
                    state = vState,
                    userScrollEnabled = column.items.size > 1,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(vertical = verticalPaddingDp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    items(column.items) { item ->
                        val isCurrentFrame = currentFrame == item.frame
                        val alpha by animateFloatAsState(if (isCurrentFrame) 1f else 0f)
                        Box(
                            Modifier.height(itemSize.height)
                                .padding(4.dp)
                                .border(
                                    2.dp,
                                    MaterialTheme.colors.primary.copy(alpha = alpha),
                                    RoundedCornerShape(6.dp)
                                )
                                .padding(8.dp)
                                .aspectRatio(storyboard.size.width / storyboard.size.height)
                        ) {
                            val sharedElementModifier = when (isCurrentFrame) {
                                false -> Modifier
                                true -> with(receiver = sharedTransitionScope) {
                                    Modifier.sharedBounds(
                                        sharedContentState = rememberSharedContentState(key = OverviewCurrentSlide),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    )
                                }
                            }

                            PreviewSlide(
                                slide = column.slide,
                                index = item.frame.stateIndex,
                                size = storyboard.size,
                                decorator = storyboard.decorator,
                                modifier = sharedElementModifier
                            )

                            // Cover the slide content with a clickable modifier
                            // to disable interaction while in overview.
                            Box(
                                modifier = Modifier.fillMaxSize()
                                    .pointerHoverIcon(PointerIcon.Hand)
                                    .clickable(
                                        interactionSource = null, indication = null, // disable ripple effect
                                        onClick = {
                                            if (isCurrentFrame) onExitOverview()
                                            else storyboard.jumpTo(item.frame.slideIndex, item.frame.stateIndex)
                                        }
                                    )
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

private class StoryboardOverview private constructor(
    val columns: ImmutableList<Column>,
) {
    @Immutable
    class Column(
        val slide: Slide<*>,
        val index: Int,
        val items: ImmutableList<Item>,
    )

    @Immutable
    class Item(
        val frame: Storyboard.Frame,
    )

    companion object {
        fun of(storyboard: Storyboard): StoryboardOverview {
            return StoryboardOverview(
                columns = storyboard.slides
                    .mapIndexed { slideIndex, slide ->
                        Column(
                            slide = slide,
                            index = slideIndex,
                            items = slide.states
                                .mapIndexedNotNull { stateIndex, state ->
                                    if (state.transitional) null
                                    else Item(
                                        frame = Storyboard.Frame(slideIndex, stateIndex)
                                    )
                                }
                                .toImmutableList(),
                        )
                    }
                    .filter { it.items.isNotEmpty() }
                    .toImmutableList()
            )
        }
    }
}

internal val OverviewCurrentSlide = Any()

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

@Composable
private fun Modifier.onOverviewNavigation(storyboard: Storyboard, overview: StoryboardOverview): Modifier {
    // TODO handle transitional slides
    fun handle(event: KeyEvent): Boolean {
        val currentFrame = storyboard.currentFrame
        if (event.type == KeyEventType.KeyDown) {
            return when (event.key) {
                Key.DirectionRight -> {
                    val columnIndex = overview.columns.binarySearch { compareValues(it.index, currentFrame.slideIndex) }
                    val column = overview.columns[(columnIndex + 1).coerceIn(overview.columns.indices)]
                    storyboard.jumpTo(column.index)
                }

                Key.DirectionLeft -> {
                    val columnIndex = overview.columns.binarySearch { compareValues(it.index, currentFrame.slideIndex) }
                    val column = overview.columns[(columnIndex - 1).coerceIn(overview.columns.indices)]
                    storyboard.jumpTo(column.index)
                }

                Key.DirectionDown -> {
                    storyboard.jumpTo(currentFrame.slideIndex, currentFrame.stateIndex + 1)
                }

                Key.DirectionUp -> {
                    storyboard.jumpTo(currentFrame.slideIndex, currentFrame.stateIndex - 1)
                }

                else -> false
            }
        }

        return false
    }

    return requestFocus().onKeyEvent { handle(it) }
}
