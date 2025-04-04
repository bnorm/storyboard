package dev.bnorm.storyboard.easel

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.core.StoryState
import dev.bnorm.storyboard.easel.overlay.OverlayNavigation
import dev.bnorm.storyboard.easel.overlay.StoryOverlay
import dev.bnorm.storyboard.easel.overlay.StoryOverlayScope
import dev.bnorm.storyboard.ui.StoryScene

@Composable
fun EmbeddedStory(
    storyState: StoryState,
    overlay: @Composable StoryOverlayScope.() -> Unit = {
        OverlayNavigation(storyState)
    },
    modifier: Modifier = Modifier,
) {
    StoryOverlay(overlay, modifier) {
        StoryScene(storyState)
    }
}
