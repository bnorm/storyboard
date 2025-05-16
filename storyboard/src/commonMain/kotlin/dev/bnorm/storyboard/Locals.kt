package dev.bnorm.storyboard

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

public val LocalStoryboard: ProvidableCompositionLocal<Storyboard?> = compositionLocalOf { null }

public val LocalSceneMode: ProvidableCompositionLocal<SceneMode?> = compositionLocalOf { null }
