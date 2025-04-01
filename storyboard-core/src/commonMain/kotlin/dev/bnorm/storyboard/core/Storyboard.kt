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

    internal val frames = buildList {
        require(scenes.isNotEmpty()) { "cannot build frames for empty list of scenes" }

        val first = scenes.first()
        val last = scenes.last()
        require(first.states.isNotEmpty() && last.states.isNotEmpty()) { "first and last scene must have states" }

        var frameIndex = 0
        fun <T> MutableList<StoryState.StateFrame<*>>.addStates(scene: StoryState.StateScene<T>) {
            for ((stateIndex, state) in scene.scene.states.withIndex()) {
                val index = StoryState.StateFrame(
                    frameIndex = frameIndex++,
                    scene = scene,
                    frame = Frame.State(state),
                    storyboardIndex = Index(scene.sceneIndex, stateIndex),
                )
                add(index)
            }
        }

        for ((sceneIndex, scene) in scenes.withIndex()) {
            val slide = StoryState.StateScene(sceneIndex, scene)
            if (scene != first) add(
                StoryState.StateFrame(
                    frameIndex++,
                    slide,
                    Frame.Start,
                    Index(sceneIndex, -1)
                )
            )
            addStates(slide)
            if (scene != last) add(StoryState.StateFrame(frameIndex++, slide, Frame.End, Index(sceneIndex, -2)))
        }
    }

    internal val framesByIndex = frames.filter { it.storyboardIndex.stateIndex >= 0 }.associateBy { it.storyboardIndex }
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
