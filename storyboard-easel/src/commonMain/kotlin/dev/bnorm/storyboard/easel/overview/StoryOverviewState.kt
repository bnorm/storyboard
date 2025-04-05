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
    internal val columns: ImmutableList<SceneColumn<*>>,
) {
    private var _isVisible = mutableStateOf(false)
    var isVisible: Boolean
        get() = _isVisible.value
        set(value) {
            // If the overview becomes visible, make sure it is on the correct frame.
            if (value == true) jumpToFrame()
            _isVisible.value = value
        }

    internal var currentColumnIndex by mutableStateOf(0)
    internal val currentItem: StateItem<*>
        get() = columns[currentColumnIndex].let { it.items[it.currentItemIndex] }

    internal fun jumpTo(columnIndex: Int, itemIndex: Int) {
        val coercedColumnIndex = columnIndex.coerceIn(columns.indices)
        currentColumnIndex = coercedColumnIndex
        val column = columns[coercedColumnIndex]
        column.currentItemIndex = itemIndex.coerceIn(column.items.indices)
    }

    internal fun jumpTo(columnIndex: Int) {
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
    internal class SceneColumn<T>(
        val scene: Scene<*>,
        val index: Int,
        val start: Boolean,
        val end: Boolean,
        val items: ImmutableList<StateItem<T>>,
    ) {
        var currentItemIndex by mutableStateOf(0)
    }


    @Immutable
    internal class StateItem<T>(
        val index: Storyboard.Index,
        val state: T,
    )

    companion object {
        fun of(storyState: StoryState): StoryOverviewState {
            val lastSceneIndex = storyState.storyboard.scenes.lastIndex

            fun <T> SceneColumn(scene: Scene<T>, sceneIndex: Int): SceneColumn<T> {
                return SceneColumn(
                    scene = scene,
                    index = sceneIndex,
                    start = sceneIndex > 0,
                    end = sceneIndex < lastSceneIndex,
                    items = scene.states.mapIndexed { stateIndex, state ->
                        StateItem(
                            index = Storyboard.Index(sceneIndex, stateIndex),
                            state = state,
                        )
                    }.toImmutableList(),
                )
            }

            val columns = storyState.storyboard.scenes
                .mapIndexed { sceneIndex, scene -> SceneColumn(scene, sceneIndex) }
                .toImmutableList()

            return StoryOverviewState(
                storyState = storyState,
                columns = columns,
            )
        }
    }
}