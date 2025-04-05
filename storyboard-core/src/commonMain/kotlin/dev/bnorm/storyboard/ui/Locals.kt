package dev.bnorm.storyboard.ui

import androidx.compose.runtime.compositionLocalOf
import dev.bnorm.storyboard.core.DisplayType
import dev.bnorm.storyboard.core.Storyboard

val LocalStoryboard = compositionLocalOf<Storyboard?> { null }

val LocalDisplayType = compositionLocalOf<DisplayType?> { null }
