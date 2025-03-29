package dev.bnorm.storyboard.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import dev.bnorm.storyboard.core.DisplayType
import kotlinx.coroutines.CoroutineScope

/** A [LaunchedEffect] which only runs if the current scene is displayed in a story. */
@Composable
@NonRestartableComposable
fun StoryEffect(key1: Any?, block: suspend CoroutineScope.() -> Unit) {
    val displayType = LocalDisplayType.current
    if (displayType == DisplayType.Story) {
        LaunchedEffect(key1, block)
    }
}

/** A [LaunchedEffect] which only runs if the current scene is displayed in a story. */
@Composable
@NonRestartableComposable
fun StoryEffect(key1: Any?, key2: Any?, block: suspend CoroutineScope.() -> Unit) {
    val displayType = LocalDisplayType.current
    if (displayType == DisplayType.Story) {
        LaunchedEffect(key1, key2, block)
    }
}

/** A [LaunchedEffect] which only runs if the current scene is displayed in a story. */
@Composable
@NonRestartableComposable
fun StoryEffect(key1: Any?, key2: Any?, key3: Any?, block: suspend CoroutineScope.() -> Unit) {
    val displayType = LocalDisplayType.current
    if (displayType == DisplayType.Story) {
        LaunchedEffect(key1, key2, key3, block)
    }
}

/** A [LaunchedEffect] which only runs if the current scene is displayed in a story. */
@Composable
@NonRestartableComposable
fun StoryEffect(vararg keys: Any?, block: suspend CoroutineScope.() -> Unit) {
    val displayType = LocalDisplayType.current
    if (displayType == DisplayType.Story) {
        LaunchedEffect(keys = keys, block)
    }
}
