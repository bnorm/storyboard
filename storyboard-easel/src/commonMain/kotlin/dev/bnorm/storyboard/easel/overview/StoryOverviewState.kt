package dev.bnorm.storyboard.easel.overview

import androidx.compose.runtime.*
import dev.bnorm.storyboard.Scene
import dev.bnorm.storyboard.Storyboard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Stable
class StoryOverviewState internal constructor(
    internal val columns: List<SceneColumn<*>>,
) {
    internal var currentColumnIndex by mutableStateOf(0)
    internal val currentItem: StateItem<*>
        get() = columns[currentColumnIndex].let { it.items[it.currentItemIndex] }

    fun jumpToIndex(index: Storyboard.Index) {
        // TODO this can probably be optimized since we don't hide scenes anymore...
        currentColumnIndex =
            columns.binarySearch { compareValues(it.index, index.sceneIndex) }
                .coerceAtLeast(0)

        val column = columns[currentColumnIndex]
        column.currentItemIndex =
            column.items.binarySearch { compareValues(it.index.stateIndex, index.stateIndex) }
                .coerceAtLeast(0)
    }

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
        internal fun of(storyboard: Storyboard): StoryOverviewState {
            val lastSceneIndex = storyboard.scenes.lastIndex

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

            val columns = storyboard.scenes
                .mapIndexed { sceneIndex, scene -> SceneColumn(scene, sceneIndex) }
                .toImmutableList()

            return StoryOverviewState(
                columns = columns,
            )
        }
    }
}
