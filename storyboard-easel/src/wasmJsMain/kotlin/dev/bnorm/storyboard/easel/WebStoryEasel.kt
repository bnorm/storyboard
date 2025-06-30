package dev.bnorm.storyboard.easel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.overlay.OverlayNavigation
import dev.bnorm.storyboard.easel.overlay.StoryOverlayScope
import kotlinx.browser.window
import org.w3c.dom.url.URL
import org.w3c.dom.url.URLSearchParams

@OptIn(ExperimentalStoryStateApi::class)
@Composable
fun WebStoryEasel(storyboard: Storyboard) {
    val storyState = remember(storyboard) {
        val params = URLSearchParams(window.location.search.toJsString())

        // TODO what to rename frame to here?
        //  - not really a frame or index
        //  - separate values for scene and state?
        val frameIndex = params.get("frame")?.toIntOrNull()
        val initialIndex = if (frameIndex != null) {
            storyboard.indices[frameIndex.coerceIn(storyboard.indices.indices)]
        } else {
            Storyboard.Index(0, 0)
        }

        StoryState(initialIndex).also {
            it.updateStoryboard(storyboard)
        }
    }

    WebStoryEasel(storyState)
}

@Composable
fun WebStoryEasel(
    storyState: StoryState,
    overlay: @Composable StoryOverlayScope.() -> Unit = {
        OverlayNavigation(storyState)
    },
) {
    LaunchedWindowHistoryUpdate(storyState)

    StoryEasel(
        storyState = storyState,
        overlay = {
            // TODO if this is a mobile device, prefer touch navigation
            overlay()
        },
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    )
}

@Composable
private fun LaunchedWindowHistoryUpdate(storyState: StoryState) {
    val frame = storyState.currentIndex
    // TODO LaunchedEffect?
    //  - maybe remember would be better since this more of a side effect?
    LaunchedEffect(frame) {
        val url = URL(window.location.toString())

        val index = storyState.storyboard.indices.binarySearch(frame)
        if (index >= 0) {
            url.searchParams.set("frame", index.toString())
        }

        window.history.replaceState(null, "", url.toString())
    }
}
