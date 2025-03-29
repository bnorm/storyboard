package dev.bnorm.storyboard.ui

import androidx.compose.runtime.compositionLocalOf
import dev.bnorm.storyboard.core.DisplayType

val LocalDisplayType = compositionLocalOf<DisplayType> { error("No DisplayType provided") }
