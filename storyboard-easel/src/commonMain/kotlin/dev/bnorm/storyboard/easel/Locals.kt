package dev.bnorm.storyboard.easel

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import dev.bnorm.storyboard.Storyboard

val LocalStoryboard: ProvidableCompositionLocal<Storyboard?> = compositionLocalOf { null }

val LocalSceneMode: ProvidableCompositionLocal<SceneMode?> = compositionLocalOf { null }
