package dev.bnorm.storyboard

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Stable
@StoryboardBuilderDsl
interface SceneScope<T> {
    val states: ImmutableList<T>
    val transition: Transition<out Frame<T>>
}
