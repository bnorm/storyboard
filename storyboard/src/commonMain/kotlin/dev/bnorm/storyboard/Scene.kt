package dev.bnorm.storyboard

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
public class Scene<T> internal constructor(
    public val index: Int,
    public val states: ImmutableList<T>,
    public val enterTransition: SceneEnterTransition,
    public val exitTransition: SceneExitTransition,
    public val content: SceneContent<T>,
) : Comparable<Scene<*>> {
    override fun compareTo(other: Scene<*>): Int {
        return compareValues(index, other.index)
    }
}
