package dev.bnorm.storyboard.core

import androidx.compose.runtime.Composable

typealias SceneContent<T> = @Composable SceneScope<T>.() -> Unit
