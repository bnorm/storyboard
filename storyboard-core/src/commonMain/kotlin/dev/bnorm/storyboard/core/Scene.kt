package dev.bnorm.storyboard.core

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
class Scene<T>(
    val states: ImmutableList<T>,
    val enterTransition: (AdvanceDirection) -> EnterTransition,
    val exitTransition: (AdvanceDirection) -> ExitTransition,
    val content: SceneContent<T>,
)
