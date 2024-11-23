package dev.bnorm.storyboard.easel

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import dev.bnorm.storyboard.core.Slide
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.easel.internal.aspectRatio
import dev.bnorm.storyboard.easel.internal.requestFocus
import dev.bnorm.storyboard.ui.SlidePreview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun StoryboardOverview(
    storyboard: Storyboard,
    onExitOverview: (Storyboard.Frame) -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
) {
    val overview = remember(storyboard) { StoryboardOverview.of(storyboard) }
    StoryboardOverview(
        overview = overview,
        onExitOverview = onExitOverview,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        modifier = modifier,
    )
}

@Composable
fun StoryboardOverview(
    overview: StoryboardOverview,
    onExitOverview: (Storyboard.Frame) -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.onOverviewNavigation(overview, onExitOverview, animatedVisibilityScope)) {
        val boxWithConstraintsScope = this
        val itemSize = boxWithConstraintsScope.toItemSize(overview.storyboard.size)
        val verticalPaddingDp = (boxWithConstraintsScope.maxHeight - itemSize.height).coerceAtLeast(0.dp) / 2
        val horizontalPaddingDp = (boxWithConstraintsScope.maxWidth - itemSize.width).coerceAtLeast(0.dp) / 2
        val horizontalScrollOffset = with(LocalDensity.current) { horizontalPaddingDp.toPx() }

        val hIndex = overview.currentColumnIndex
        val hState = rememberLazyListState(hIndex, -horizontalScrollOffset.toInt())
        LaunchedEffect(hIndex) { hState.animateScrollToItem(hIndex, -horizontalScrollOffset.toInt()) }

        LazyRow(
            state = hState,
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(overview.columns) { columnIndex, column ->
                val isCurrentColumn = overview.currentColumnIndex == columnIndex
                val vIndex = column.currentItemIndex
                val vState = rememberLazyListState(vIndex)
                LaunchedEffect(vIndex) { vState.animateScrollToItem(vIndex) }

                LazyColumn(
                    state = vState,
                    userScrollEnabled = column.items.size > 1,
                    contentPadding = PaddingValues(vertical = verticalPaddingDp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    itemsIndexed(column.items) { itemIndex, item ->
                        val isCurrentItem = isCurrentColumn && column.currentItemIndex == itemIndex
                        val targetColor = if (isCurrentItem) MaterialTheme.colors.primary else Color.Transparent
                        val currentColor by animateColorAsState(targetColor)
                        Box(
                            Modifier.height(itemSize.height)
                                .padding(4.dp)
                                .border(2.dp, currentColor, RoundedCornerShape(6.dp))
                                .padding(8.dp)
                                .aspectRatio(overview.storyboard.size.aspectRatio)
                        ) {
                            val sharedElementModifier = when (isCurrentItem) {
                                false -> Modifier
                                true -> with(sharedTransitionScope) {
                                    Modifier.sharedElement(
                                        rememberSharedContentState(OverviewCurrentSlide),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    )
                                }
                            }

                            SlidePreview(
                                storyboard = overview.storyboard,
                                frame = item.frame,
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
                                            if (isCurrentItem) onExitOverview(item.frame)
                                            else overview.jumpTo(columnIndex, itemIndex)
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

class StoryboardOverview private constructor(
    val storyboard: Storyboard,
    val columns: ImmutableList<Column>,
    currentColumnIndex: Int,
) {
    var isVisible by mutableStateOf(false)

    var currentColumnIndex by mutableStateOf(currentColumnIndex)
    val currentItem: Item
        get() = columns[currentColumnIndex].let { it.items[it.currentItemIndex] }

    fun jumpTo(columnIndex: Int, itemIndex: Int) {
        val coercedColumnIndex = columnIndex.coerceIn(columns.indices)
        currentColumnIndex = coercedColumnIndex
        val column = columns[coercedColumnIndex]
        column.currentItemIndex = itemIndex.coerceIn(column.items.indices)
    }

    fun jumpTo(columnIndex: Int) {
        val coercedColumnIndex = columnIndex.coerceIn(columns.indices)
        currentColumnIndex = coercedColumnIndex
    }

    @Immutable
    class Column(
        val slide: Slide<*>,
        val index: Int,
        val items: ImmutableList<Item>,
        currentItemIndex: Int,
    ) {
        var currentItemIndex by mutableStateOf(currentItemIndex)
    }

    @Immutable
    class Item(
        val frame: Storyboard.Frame,
    )

    companion object {
        fun of(storyboard: Storyboard): StoryboardOverview {
            val currentFrame = storyboard.currentFrame

            val columns = storyboard.slides
                .mapIndexed { slideIndex, slide ->
                    val items = slide.states
                        .mapIndexedNotNull { stateIndex, _ ->
                            Item(
                                frame = Storyboard.Frame(slideIndex, stateIndex)
                            )
                        }
                        .toImmutableList()

                    Column(
                        slide = slide,
                        index = slideIndex,
                        items = items,
                        currentItemIndex = when {
                            currentFrame.slideIndex > slideIndex -> items.size - 1
                            currentFrame.slideIndex < slideIndex -> 0
                            else -> items.binarySearch { compareValues(it.frame.stateIndex, currentFrame.stateIndex) }
                                .coerceAtLeast(0)
                        },
                    )
                }
                .filter { it.items.isNotEmpty() }
                .toImmutableList()

            val columnIndex =
                columns.binarySearch { compareValues(it.index, currentFrame.slideIndex) }
                    .coerceAtLeast(0)

            return StoryboardOverview(
                storyboard = storyboard,
                columns = columns,
                currentColumnIndex = columnIndex,
            )
        }
    }
}

internal object OverviewCurrentSlide

private fun BoxWithConstraintsScope.toItemSize(
    size: DpSize,
    scale: Float = 1f / 5f,
    min: Dp = 150.dp,
    max: Dp = 250.dp,
): DpSize {
    val aspectRatio = size.aspectRatio
    val height = (scale * maxHeight).coerceIn(min, max)
    val width = (scale * maxWidth).coerceIn(min, max)

    return if (width / aspectRatio < height * aspectRatio) {
        DpSize(height = height, width = height * aspectRatio)
    } else {
        DpSize(height = width / aspectRatio, width = width)
    }
}

@Composable
private fun Modifier.onOverviewNavigation(
    overview: StoryboardOverview,
    onExitOverview: (Storyboard.Frame) -> Unit,
    animatedVisibilityScope: AnimatedContentScope,
): Modifier {
    // TODO handle transitional slides? render with alpha = 0.5f ?
    fun handle(event: KeyEvent): Boolean {
        // Disable navigation until enter/exit animation is complete
        if (animatedVisibilityScope.transition.isRunning) return false

        if (event.type == KeyEventType.KeyDown) {
            val currentColumnIndex = overview.currentColumnIndex
            when (event.key) {
                Key.Enter -> {
                    onExitOverview(overview.currentItem.frame)
                    return true
                }

                Key.DirectionRight -> {
                    overview.jumpTo(currentColumnIndex + 1)
                    return true
                }

                Key.DirectionLeft -> {
                    overview.jumpTo(currentColumnIndex - 1)
                    return true
                }

                Key.DirectionDown -> {
                    val column = overview.columns[currentColumnIndex]
                    overview.jumpTo(currentColumnIndex, column.currentItemIndex + 1)
                    return true
                }

                Key.DirectionUp -> {
                    val column = overview.columns[currentColumnIndex]
                    overview.jumpTo(currentColumnIndex, column.currentItemIndex - 1)
                    return true
                }

                else -> return false
            }
        }

        return false
    }

    return requestFocus().onKeyEvent { handle(it) }
}
