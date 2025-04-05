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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
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
        val boxWithConstraintsScope = this
        val itemSize = boxWithConstraintsScope.toItemSize(overview.storyState.storyboard.size)
        val verticalPaddingDp = (boxWithConstraintsScope.maxHeight - itemSize.height).coerceAtLeast(0.dp) / 2
        val horizontalPaddingDp = (boxWithConstraintsScope.maxWidth - itemSize.width).coerceAtLeast(0.dp) / 2
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
                val isCurrentColumn = overview.currentColumnIndex == columnIndex
                val vIndex = column.currentItemIndex
                val vState = rememberLazyListState(vIndex)
                LaunchedEffect(vIndex, verticalPaddingDp) { vState.animateScrollToItem(vIndex) }

                LazyColumn(
                    state = vState,
                    userScrollEnabled = column.items.size > 1,
                    contentPadding = PaddingValues(vertical = verticalPaddingDp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    itemsIndexed(column.items) { itemIndex, item ->
                        val isCurrentIndex = isCurrentColumn && column.currentItemIndex == itemIndex
                        val targetColor = if (isCurrentIndex) MaterialTheme.colors.primary else Color.Transparent
                        val currentColor by animateColorAsState(targetColor)
                        Box(
                            Modifier.height(itemSize.height)
                                .padding(4.dp)
                                .border(2.dp, currentColor, RoundedCornerShape(6.dp))
                                .padding(8.dp)
                                .aspectRatio(overview.storyState.storyboard.size.aspectRatio)
                        ) {
                            val sharedElementModifier = when (isCurrentIndex) {
                                false -> Modifier
                                true -> with(sharedTransitionScope) {
                                    Modifier.sharedElement(
                                        rememberSharedContentState(OverviewCurrentIndex),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    )
                                }
                            }

                            ScenePreview(
                                storyboard = overview.storyState.storyboard,
                                index = item.index,
                                modifier = sharedElementModifier
                            )

                            // Cover the scene content with a clickable modifier
                            // to disable interaction while in overview.
                            Box(
                                modifier = Modifier.fillMaxSize()
                                    .pointerHoverIcon(PointerIcon.Hand)
                                    .clickable(
                                        interactionSource = null, indication = null, // disable ripple effect
                                        onClick = {
                                            if (isCurrentIndex) onExitOverview(item.index)
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

internal object OverviewCurrentIndex

private fun BoxWithConstraintsScope.toItemSize(
    size: DpSize,
    scale: Float = 1f / 5f,
    min: Dp = 250.dp,
    max: Dp = 500.dp,
): DpSize {
    val aspectRatio = size.aspectRatio
    return if ((maxWidth / maxHeight) < aspectRatio) {
        if (aspectRatio > 1f) {
            val width = (scale * maxWidth).coerceIn(min, max)
            DpSize(height = width / aspectRatio, width = width)
        } else {
            val height = (scale * maxWidth / aspectRatio).coerceIn(min, max)
            DpSize(height = height, width = height * aspectRatio)
        }
    } else {
        if (aspectRatio > 1f) {
            val width = (scale * maxHeight * aspectRatio).coerceIn(min, max)
            DpSize(height = width / aspectRatio, width = width)
        } else {
            val height = (scale * maxHeight).coerceIn(min, max)
            DpSize(height = height, width = height * aspectRatio)
        }
    }
}

@Composable
private fun Modifier.onOverviewNavigation(
    overview: StoryOverviewState,
    onExitOverview: (Storyboard.Index) -> Unit,
    animatedVisibilityScope: AnimatedContentScope,
): Modifier {
    // TODO handle transitional scenes? render with alpha = 0.5f ?
    fun handle(event: KeyEvent): Boolean {
        // Disable navigation until enter/exit animation is complete
        if (animatedVisibilityScope.transition.isRunning) return false

        if (event.type == KeyEventType.KeyDown) {
            val currentColumnIndex = overview.currentColumnIndex
            when (event.key) {
                Key.Enter -> {
                    onExitOverview(overview.currentItem.index)
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
