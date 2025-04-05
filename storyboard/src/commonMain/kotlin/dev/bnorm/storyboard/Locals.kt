package dev.bnorm.storyboard

import androidx.compose.runtime.compositionLocalOf

val LocalStoryboard = compositionLocalOf<Storyboard?> { null }

val LocalDisplayType = compositionLocalOf<DisplayType?> { null }
