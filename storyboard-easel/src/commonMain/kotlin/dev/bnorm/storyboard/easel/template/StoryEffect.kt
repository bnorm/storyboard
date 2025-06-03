package dev.bnorm.storyboard.easel.template

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import dev.bnorm.storyboard.easel.SceneMode
import dev.bnorm.storyboard.easel.LocalSceneMode
import kotlinx.coroutines.CoroutineScope

/** A [LaunchedEffect] which only runs if the current [SceneMode] is [Story][SceneMode.Story]. */
@Composable
@NonRestartableComposable
fun StoryEffect(key1: Any?, block: suspend CoroutineScope.() -> Unit) {
    val sceneMode = LocalSceneMode.current
    if (sceneMode == SceneMode.Story) {
        LaunchedEffect(key1, block)
    }
}

/** A [LaunchedEffect] which only runs if the current [SceneMode] is [Story][SceneMode.Story]. */
@Composable
@NonRestartableComposable
fun StoryEffect(key1: Any?, key2: Any?, block: suspend CoroutineScope.() -> Unit) {
    val sceneMode = LocalSceneMode.current
    if (sceneMode == SceneMode.Story) {
        LaunchedEffect(key1, key2, block)
    }
}

/** A [LaunchedEffect] which only runs if the current [SceneMode] is [Story][SceneMode.Story]. */
@Composable
@NonRestartableComposable
fun StoryEffect(key1: Any?, key2: Any?, key3: Any?, block: suspend CoroutineScope.() -> Unit) {
    val sceneMode = LocalSceneMode.current
    if (sceneMode == SceneMode.Story) {
        LaunchedEffect(key1, key2, key3, block)
    }
}

/** A [LaunchedEffect] which only runs if the current [SceneMode] is [Story][SceneMode.Story]. */
@Composable
@NonRestartableComposable
fun StoryEffect(vararg keys: Any?, block: suspend CoroutineScope.() -> Unit) {
    val sceneMode = LocalSceneMode.current
    if (sceneMode == SceneMode.Story) {
        LaunchedEffect(keys = keys, block)
    }
}
