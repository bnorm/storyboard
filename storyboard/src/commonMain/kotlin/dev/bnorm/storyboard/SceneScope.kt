package dev.bnorm.storyboard

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Stable
@StoryboardBuilderDsl
public interface SceneScope<out T> {
    public val states: ImmutableList<T>
    public val transition: Transition<out Frame<T>>
}
