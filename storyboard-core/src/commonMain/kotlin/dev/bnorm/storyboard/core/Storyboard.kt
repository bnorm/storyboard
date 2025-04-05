package dev.bnorm.storyboard.core

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Stable
class Storyboard private constructor(
    val title: String,
    val description: String?,
    val scenes: ImmutableList<Scene<*>>,
    val size: DpSize = DEFAULT_SIZE,
    val decorator: SceneDecorator = SceneDecorator.None,
) {
    @Immutable
    data class Index(
        val sceneIndex: Int,
        val stateIndex: Int,
    ) : Comparable<Index> {
        // TODO require sceneIndex >= 0?
        // TODO require stateIndex >= 0?
        override fun compareTo(other: Index): Int {
            val cmp = compareValues(sceneIndex, other.sceneIndex)
            if (cmp != 0) return cmp
            return compareValues(stateIndex, other.stateIndex)
        }
    }

    companion object {
        val DEFAULT_SIZE = DpSize(960.dp, 540.dp)

        fun build(
            title: String,
            description: String? = null,
            size: DpSize = DEFAULT_SIZE,
            decorator: SceneDecorator = SceneDecorator.None,
            block: StoryboardBuilder.() -> Unit,
        ): Storyboard {
            val builder = StoryboardBuilderImpl()
            builder.block()
            return Storyboard(title, description, builder.build().toImmutableList(), size, decorator)
        }
    }

    val indices: ImmutableList<Index> = scenes.flatMapIndexed { sceneIndex, scene ->
        List(scene.states.size) { stateIndex -> Index(sceneIndex, stateIndex) }
    }.toImmutableList()
}

private class StoryboardBuilderImpl : StoryboardBuilder {
    private val scenes = mutableListOf<Scene<*>>()

    override fun <T> scene(
        states: List<T>,
        enterTransition: (AdvanceDirection) -> EnterTransition,
        exitTransition: (AdvanceDirection) -> ExitTransition,
        content: SceneContent<T>,
    ): Scene<T> {
        val scene = Scene(
            states = states.toImmutableList(),
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            content = content,
        )
        scenes.add(scene)
        return scene
    }

    fun build(): List<Scene<*>> {
        return scenes
    }
}
