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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import dev.bnorm.storyboard.core.Scene
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.core.StoryState
import dev.bnorm.storyboard.easel.internal.aspectRatio
import dev.bnorm.storyboard.easel.internal.requestFocus
import dev.bnorm.storyboard.ui.ScenePreview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

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

class StoryOverviewState private constructor(
    val storyState: StoryState,
    internal val columns: ImmutableList<SceneColumn>,
) {
    private var _isVisible = mutableStateOf(false)
    var isVisible: Boolean
        get() = _isVisible.value
        set(value) {
            // If the overview becomes visible, make sure it is on the correct frame.
            if (value == true) jumpToFrame()
            _isVisible.value = value
        }

    var currentColumnIndex by mutableStateOf(0)
    internal val currentItem: Item
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

    private fun jumpToFrame() {
        val currentFrame = storyState.currentIndex

        currentColumnIndex =
            columns.binarySearch { compareValues(it.index, currentFrame.sceneIndex) }
                .coerceAtLeast(0)

        val column = columns[currentColumnIndex]
        column.currentItemIndex =
            column.items.binarySearch { compareValues(it.index.stateIndex, currentFrame.stateIndex) }
                .coerceAtLeast(0)
    }

    @Immutable
    internal class SceneColumn(
        val scene: Scene<*>,
        val index: Int,
        val items: ImmutableList<Item>,
    ) {
        var currentItemIndex by mutableStateOf(0)
    }

    @Immutable
    internal class Item(
        val index: Storyboard.Index,
    )

    companion object {
        fun of(storyState: StoryState): StoryOverviewState {
            val columns = storyState.storyboard.scenes
                .mapIndexed { sceneIndex, scene ->
                    val items = scene.states
                        .mapIndexed { stateIndex, _ ->
                            Item(index = Storyboard.Index(sceneIndex, stateIndex))
                        }
                        .toImmutableList()

                    SceneColumn(
                        scene = scene,
                        index = sceneIndex,
                        items = items,
                    )
                }
                .filter { it.items.isNotEmpty() }
                .toImmutableList()

            return StoryOverviewState(
                storyState = storyState,
                columns = columns,
            )
        }
    }
}

internal object OverviewCurrentIndex

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
