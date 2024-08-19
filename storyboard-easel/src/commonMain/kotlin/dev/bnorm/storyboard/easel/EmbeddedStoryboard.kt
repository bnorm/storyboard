package dev.bnorm.storyboard.easel

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.ui.StoryboardSlide

@Composable
fun EmbeddedStoryboard(storyboard: Storyboard, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val overlayState = remember(coroutineScope) { OverlayState(coroutineScope) }
    EmbeddedStoryboard(storyboard, overlayState, modifier)
}

@Composable
fun EmbeddedStoryboard(storyboard: Storyboard, overlayState: OverlayState?, modifier: Modifier = Modifier) {
    Box(modifier = Modifier.onPointerMovePress(overlayState)) {
        StoryboardSlide(storyboard, modifier = modifier)
        if (overlayState != null) StoryboardOverlay(storyboard, overlayState)
    }
}
