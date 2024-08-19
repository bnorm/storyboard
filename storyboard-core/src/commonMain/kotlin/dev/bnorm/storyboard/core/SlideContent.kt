package dev.bnorm.storyboard.core

import androidx.compose.runtime.Composable

typealias SlideContent<T> = @Composable SlideScope<T>.() -> Unit
