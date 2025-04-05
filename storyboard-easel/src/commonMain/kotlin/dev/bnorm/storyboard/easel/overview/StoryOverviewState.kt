package dev.bnorm.storyboard.easel.overview

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.bnorm.storyboard.core.Scene
import dev.bnorm.storyboard.core.StoryState
import dev.bnorm.storyboard.core.Storyboard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

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