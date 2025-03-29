package dev.bnorm.storyboard.easel

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.core.StoryState
import dev.bnorm.storyboard.ui.StoryScene

@Composable
fun EmbeddedStory(storyState: StoryState, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val overlayState = remember(coroutineScope) { OverlayState(coroutineScope) }
    Box(modifier = modifier.onPointerMovePress(overlayState)) {
        StoryScene(storyState)
        StoryOverlay(storyState, overlayState)
    }
}
