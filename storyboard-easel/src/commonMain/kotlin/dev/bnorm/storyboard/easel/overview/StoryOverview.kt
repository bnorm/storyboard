package dev.bnorm.storyboard.easel.overview

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.ScenePreview
import dev.bnorm.storyboard.easel.StoryState
import dev.bnorm.storyboard.easel.internal.requestFocus
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement

// TODO keep assistant up-to-date?
// TODO disable assistant navigation while in overview?
@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun StoryOverview(
    storyState: StoryState,
    storyOverviewState: StoryOverviewState,
    onExitOverview: (Storyboard.Index) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    fun onClick(columnIndex: Int, itemIndex: Int, item: StoryOverviewState.StateItem<*>) {
        if (item == storyOverviewState.currentItem) {
            onExitOverview(item.index)
        }
        storyOverviewState.jumpTo(columnIndex, itemIndex)
    }

    BoxWithConstraints(modifier = modifier.onOverviewNavigation(storyOverviewState, onExitOverview)) {
        val viewSize = DpSize(maxWidth, maxHeight)
        val itemSize = calculateItemSize(
            viewSize = viewSize,
            itemSize = storyState.storyboard.size
        )

        val hState = rememberOverviewSceneScroll(viewSize, itemSize, storyOverviewState)

        LazyRow(
            state = hState,
            modifier = Modifier.fillMaxSize(),
        ) {
            itemsIndexed(storyOverviewState.columns) { columnIndex, column ->
                val isCurrentColumn = columnIndex == storyOverviewState.currentColumnIndex
                val verticalPaddingDp = (maxHeight - itemSize.height).coerceAtLeast(0.dp) / 2
                val vState = rememberOverviewStateScroll(itemSize, column)

                // TODO how to keep the user from scrolling to a start or end frame?
                //  - invisible padding item with different content padding?
                OverviewLazyColumn(
                    state = vState,
                    storyboard = storyState.storyboard,
                    column = column,
                    verticalPaddingDp = verticalPaddingDp,
                    itemSize = itemSize,
                    isCurrentColumn = isCurrentColumn,
                    onClick = { itemIndex, item ->
                        onClick(
                            columnIndex = columnIndex,
                            itemIndex = itemIndex,
                            item = item
                        )
                    },
                )
            }
        }

        val currentIndex = storyOverviewState.currentItem.index
        Surface(
            modifier = Modifier.align(Alignment.BottomStart).alpha(0.75f).padding(32.dp),
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                text = currentIndex.toString(),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(8.dp),
            )
        }

        LaunchedEffect(currentIndex) {
            storyState.jumpTo(currentIndex)
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
private fun rememberOverviewSceneScroll(
    viewSize: DpSize,
    itemSize: DpSize,
    overview: StoryOverviewState,
): LazyListState {
    val horizontalPaddingDp = (viewSize.width - itemSize.width).coerceAtLeast(0.dp) / 2
    val horizontalScrollOffset = with(LocalDensity.current) { horizontalPaddingDp.toPx() }

    val hIndex = overview.currentColumnIndex
    val hState = rememberLazyListState(hIndex, -horizontalScrollOffset.toInt())
    LaunchedEffect(hIndex, horizontalScrollOffset) {
        hState.animateScrollToItem(hIndex, -horizontalScrollOffset.toInt())
    }

    return hState
}

@Composable
private fun rememberOverviewStateScroll(
    itemSize: DpSize,
    column: StoryOverviewState.SceneColumn<*>,
): LazyListState {
    val verticalScrollOffset = when (column.items.isEmpty()) {
        true -> with(LocalDensity.current) { itemSize.height.toPx() / 2 }
        false -> 0
    }

    val vIndex = column.currentItemIndex + if (column.start) 1 else 0
    val vState = rememberLazyListState(vIndex, -verticalScrollOffset.toInt())
    LaunchedEffect(vIndex, verticalScrollOffset) {
        vState.animateScrollToItem(vIndex, -verticalScrollOffset.toInt())
    }

    return vState
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun OverviewLazyColumn(
    state: LazyListState,
    storyboard: Storyboard,
    column: StoryOverviewState.SceneColumn<*>,
    verticalPaddingDp: Dp,
    itemSize: DpSize,
    isCurrentColumn: Boolean,
    onClick: (Int, StoryOverviewState.StateItem<*>) -> Unit,
) {

    LazyColumn(
        state = state,
        userScrollEnabled = column.items.size > 1,
        contentPadding = PaddingValues(vertical = verticalPaddingDp),
        modifier = Modifier.fillMaxHeight()
    ) {
        if (column.start) {
            item("start") {
                Item(itemSize) {
                    ScenePreview(
                        storyboard = storyboard,
                        scene = column.scene,
                        frame = Frame.Start,
                        modifier = Modifier.alpha(0.25f),
                    )
                }
            }
        }

        itemsIndexed(column.items) { itemIndex, item ->
            val isCurrentIndex = isCurrentColumn && column.currentItemIndex == itemIndex
            val currentColor by animateColorAsState(
                when {
                    isCurrentIndex -> MaterialTheme.colors.secondary
                    else -> Color.Transparent
                }
            )

            Item(
                size = itemSize,
                borderColor = currentColor,
                onClick = { onClick(itemIndex, item) }
            ) {
                ScenePreview(
                    storyboard = storyboard,
                    index = item.index,
                    modifier = when (isCurrentIndex) {
                        false -> Modifier
                        true -> Modifier.sharedElement(rememberSharedContentState(OverviewCurrentItemKey))
                    }
                )
            }
        }

        if (column.end) {
            item("end") {
                Item(itemSize) {
                    ScenePreview(
                        storyboard = storyboard,
                        scene = column.scene,
                        frame = Frame.End,
                        modifier = Modifier.alpha(0.25f),
                    )
                }
            }
        }
    }
}

@Composable
private fun Item(
    size: DpSize,
    borderColor: Color = Color.Transparent,
    onClick: (() -> Unit)? = null,
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
                .pointerHoverIcon(if (onClick != null) PointerIcon.Hand else PointerIcon.Default)
                .clickable(
                    interactionSource = null, indication = null, // disable ripple effect
                    onClick = { onClick?.invoke() }
                )
        )
    }
}

private fun calculateItemSize(
    viewSize: DpSize,
    itemSize: DpSize,
    scale: Float = 5f,
    itemPadding: Dp = 20.dp,
    min: Dp = 256.dp,
    max: Dp = 512.dp,
): DpSize {
    val aspectRatio = itemSize.width / itemSize.height

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
context(animatedVisibilityScope: AnimatedVisibilityScope)
private fun Modifier.onOverviewNavigation(
    overview: StoryOverviewState,
    onExitOverview: (Storyboard.Index) -> Unit,
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
