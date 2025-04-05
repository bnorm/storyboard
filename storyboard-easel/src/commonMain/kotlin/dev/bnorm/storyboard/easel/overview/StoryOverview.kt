package dev.bnorm.storyboard.easel.overview

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import dev.bnorm.storyboard.core.Frame
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.easel.internal.aspectRatio
import dev.bnorm.storyboard.easel.internal.requestFocus
import dev.bnorm.storyboard.ui.ScenePreview

@Composable
fun StoryOverview(
    overview: StoryOverviewState,
    onExitOverview: (Storyboard.Index) -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.onOverviewNavigation(overview, onExitOverview, animatedVisibilityScope)) {
        val itemSize = calculateItemSize(
            viewSize = DpSize(maxWidth, maxHeight),
            itemSize = overview.storyState.storyboard.size
        )

        val horizontalPaddingDp = (maxWidth - itemSize.width).coerceAtLeast(0.dp) / 2
        val horizontalScrollOffset = with(LocalDensity.current) { horizontalPaddingDp.toPx() }

        val hIndex = overview.currentColumnIndex
        val hState = rememberLazyListState(hIndex, -horizontalScrollOffset.toInt())
        LaunchedEffect(hIndex, horizontalScrollOffset) {
            hState.animateScrollToItem(hIndex, -horizontalScrollOffset.toInt())
        }

        LazyRow(
            state = hState,
            modifier = Modifier.fillMaxSize(),
        ) {
            itemsIndexed(overview.columns) { columnIndex, column ->
                // TODO how to keep the user from scrolling to a start or end frame?
                val verticalPaddingDp = (maxHeight - itemSize.height).coerceAtLeast(0.dp) / 2
                val verticalScrollOffset = when (column.items.isEmpty()) {
                    true -> with(LocalDensity.current) { itemSize.height.toPx() / 2 }
                    false -> 0
                }

                val vIndex = column.currentItemIndex + if (column.start) 1 else 0
                val vState = rememberLazyListState(vIndex, -verticalScrollOffset.toInt())
                LaunchedEffect(vIndex, verticalPaddingDp, verticalScrollOffset) {
                    vState.animateScrollToItem(vIndex, -verticalScrollOffset.toInt())
                }

                LazyColumn(
                    state = vState,
                    userScrollEnabled = column.items.size > 1,
                    contentPadding = PaddingValues(vertical = verticalPaddingDp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    val isCurrentColumn = overview.currentColumnIndex == columnIndex

                    if (column.start) {
                        item("start") {
                            Item(itemSize) {
                                ScenePreview(
                                    storyboard = overview.storyState.storyboard,
                                    scene = column.scene,
                                    frame = Frame.Start,
                                    modifier = Modifier.alpha(0.25f),
                                )
                            }
                        }
                    }

                    itemsIndexed(column.items) { itemIndex, item ->
                        val isCurrentIndex = isCurrentColumn && column.currentItemIndex == itemIndex
                        val targetColor = if (isCurrentIndex) MaterialTheme.colors.secondary else Color.Transparent
                        val currentColor by animateColorAsState(targetColor)

                        Item(
                            size = itemSize,
                            borderColor = currentColor,
                            onClick = {
                                if (isCurrentIndex) {
                                    onExitOverview(item.index)
                                } else {
                                    if (column.items.size > 1) {
                                        overview.jumpTo(columnIndex, itemIndex)
                                    }
                                }
                            }
                        ) {
                            ScenePreview(
                                storyboard = overview.storyState.storyboard,
                                index = item.index,
                                modifier = when (isCurrentIndex) {
                                    false -> Modifier
                                    true -> with(sharedTransitionScope) {
                                        Modifier.sharedElement(
                                            rememberSharedContentState(OverviewCurrentIndex),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                        )
                                    }
                                }
                            )
                        }
                    }

                    if (column.end) {
                        item("end") {
                            Item(itemSize) {
                                ScenePreview(
                                    storyboard = overview.storyState.storyboard,
                                    scene = column.scene,
                                    frame = Frame.End,
                                    modifier = Modifier.alpha(0.25f),
                                )
                            }
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
private fun Item(
    size: DpSize,
    borderColor: Color = Color.Transparent,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Box(
        Modifier
            .size(size)
            // !!! Make sure this padding is set in the default parameter of `toItemSize` !!!
            .padding(4.dp)
            .border(2.dp, borderColor, RoundedCornerShape(6.dp))
            .padding(4.dp)
    ) {
        content()

        // Cover the scene content with a clickable modifier
        // to disable interaction while in overview.
        Box(
            modifier = Modifier.fillMaxSize()
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable(
                    interactionSource = null, indication = null, // disable ripple effect
                    onClick = { onClick() }
                )
        )
    }
}

internal object OverviewCurrentIndex

private fun calculateItemSize(
    viewSize: DpSize,
    itemSize: DpSize,
    scale: Float = 5f,
    itemPadding: Dp = 20.dp,
    min: Dp = 256.dp,
    max: Dp = 512.dp,
): DpSize {
    val aspectRatio = itemSize.aspectRatio

    fun fromWidth(itemWidth: Dp): DpSize = DpSize(
        height = itemPadding + itemWidth / aspectRatio,
        width = itemPadding + itemWidth,
    )

    fun fromHeight(itemHeight: Dp): DpSize = DpSize(
        height = itemPadding + itemHeight,
        width = itemPadding + itemHeight * aspectRatio,
    )

    val maxItemWidth = viewSize.width / scale - itemPadding
    val maxItemHeight = viewSize.height / scale - itemPadding

    return if (maxItemWidth / maxItemHeight < aspectRatio) {
        when (aspectRatio > 1f) {
            true -> fromWidth(itemWidth = maxItemWidth.coerceIn(min, max))
            false -> fromHeight(itemHeight = (maxItemWidth / aspectRatio).coerceIn(min, max))
        }
    } else {
        when (aspectRatio > 1f) {
            true -> fromWidth(itemWidth = (maxItemHeight * aspectRatio).coerceIn(min, max))
            false -> fromHeight(itemHeight = maxItemHeight.coerceIn(min, max))
        }
    }
}

@Composable
private fun Modifier.onOverviewNavigation(
    overview: StoryOverviewState,
    onExitOverview: (Storyboard.Index) -> Unit,
    animatedVisibilityScope: AnimatedContentScope,
): Modifier {
    fun handle(event: KeyEvent): Boolean {
        // Disable navigation until enter/exit animation is complete.
        if (animatedVisibilityScope.transition.isRunning) return false

        if (event.type == KeyEventType.KeyDown) {
            val currentColumnIndex = overview.currentColumnIndex
            when (event.key) {
                Key.Enter -> {
                    onExitOverview(overview.currentItem.index)
                    return true
                }

                Key.DirectionRight -> {
                    var index = currentColumnIndex + 1
                    while (index < overview.columns.size && overview.columns[index].items.isEmpty()) index += 1
                    overview.jumpTo(index)
                    return true
                }

                Key.DirectionLeft -> {
                    var index = currentColumnIndex - 1
                    while (index >= 0 && overview.columns[index].items.isEmpty()) index -= 1
                    overview.jumpTo(index)
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
