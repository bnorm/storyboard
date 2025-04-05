package dev.bnorm.storyboard

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable

typealias SceneContent<T> =
        @Composable
        context(AnimatedVisibilityScope, SharedTransitionScope)
        SceneScope<T>.() -> Unit
