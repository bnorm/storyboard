package dev.bnorm.storyboard

import androidx.compose.runtime.compositionLocalOf

val LocalStoryboard = compositionLocalOf<Storyboard?> { null }

val LocalSceneMode = compositionLocalOf<SceneMode?> { null }
