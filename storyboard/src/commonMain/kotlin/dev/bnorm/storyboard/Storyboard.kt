package dev.bnorm.storyboard

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Stable
public class Storyboard private constructor(
    public val title: String,
    public val description: String?,
    public val scenes: ImmutableList<Scene<*>>,
    public val format: SceneFormat,
    public val decorator: Decorator,
) {
    @Immutable
    public class Index(
        public val sceneIndex: Int,
        public val stateIndex: Int,
    ) : Comparable<Index> {
        // TODO require sceneIndex >= 0?
        // TODO require stateIndex >= 0?
        override fun compareTo(other: Index): Int {
            val cmp = compareValues(sceneIndex, other.sceneIndex)
            if (cmp != 0) return cmp
            return compareValues(stateIndex, other.stateIndex)
        }

        override fun toString(): String {
            return "$sceneIndex,$stateIndex"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Index

            if (sceneIndex != other.sceneIndex) return false
            if (stateIndex != other.stateIndex) return false

            return true
        }

        override fun hashCode(): Int {
            var result = sceneIndex
            result = 31 * result + stateIndex
            return result
        }
    }

    public companion object {
        public fun build(
            title: String,
            description: String? = null,
            format: SceneFormat = SceneFormat.Default,
            decorator: Decorator = Decorator.None,
            block: StoryboardBuilder.() -> Unit,
        ): Storyboard {
            // TODO it's possible for someone to use`Storyboard.build` during composition
            //  - this can potentially trigger recompositions if state is captured.
            //  - should we forcibly disable state reads for this function?
            //  - also seems to trigger an error with SeekableTransitionState being reused?
            val builder = StoryboardBuilderImpl()
            builder.block()
            return Storyboard(title, description, builder.build().toImmutableList(), format, decorator)
        }
    }

    public val indices: ImmutableList<Index> = scenes.flatMapIndexed { sceneIndex, scene ->
        List(scene.states.size) { stateIndex -> Index(sceneIndex, stateIndex) }
    }.toImmutableList()
}

private class StoryboardBuilderImpl : StoryboardBuilder {
    private val scenes = mutableListOf<Scene<*>>()

    override fun <T> scene(
        states: List<T>,
        enterTransition: SceneEnterTransition,
        exitTransition: SceneExitTransition,
        content: SceneContent<T>,
    ): Scene<T> {
        val scene = Scene(
            index = scenes.size,
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
