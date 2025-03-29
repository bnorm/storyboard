package dev.bnorm.storyboard.ui

import androidx.compose.runtime.compositionLocalOf
import dev.bnorm.storyboard.core.DisplayType
import dev.bnorm.storyboard.core.StoryState
import dev.bnorm.storyboard.core.Storyboard

val LocalStoryboard = compositionLocalOf<Storyboard?> { null }

val LocalDisplayType = compositionLocalOf<DisplayType?> { null }

// TODO should we be providing access to this within a scene?
val LocalStoryState = compositionLocalOf<StoryState?> { null }
