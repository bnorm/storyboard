package dev.bnorm.storyboard

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
class Scene<T> internal constructor(
    val states: ImmutableList<T>,
    val enterTransition: (AdvanceDirection) -> EnterTransition,
    val exitTransition: (AdvanceDirection) -> ExitTransition,
    val content: SceneContent<T>,
)
