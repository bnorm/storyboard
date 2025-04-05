package dev.bnorm.storyboard.easel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.core.ExperimentalStoryStateApi
import dev.bnorm.storyboard.core.StoryState
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.easel.overlay.OverlayNavigation
import dev.bnorm.storyboard.easel.overlay.StoryOverlayScope
import dev.bnorm.storyboard.easel.overview.StoryOverviewState
import kotlinx.browser.window
import org.w3c.dom.url.URL
import org.w3c.dom.url.URLSearchParams

@OptIn(ExperimentalStoryStateApi::class)
@Composable
fun WebStory(storyboard: Storyboard) {
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

    WebStory(storyState)
}

@Composable
fun WebStory(
    storyState: StoryState,
    overlay: @Composable StoryOverlayScope.() -> Unit = {
        OverlayNavigation(storyState)
    },
) {
    val overview = remember(storyState.storyboard) { StoryOverviewState.of(storyState) }

    // TODO should we be exposing overview as a param?
    //  - we don't support this on desktop...
    remember {
        val params = URLSearchParams(window.location.search.toJsString())
        if (params.get("overview").toBoolean()) {
            overview.isVisible = true
        }
    }

    LaunchedWindowHistoryUpdate(storyState, overview)

    Story(
        storyState = storyState,
        storyOverviewState = overview,
        overlay = {
            // TODO if this is a mobile device, prefer touch navigation
            overlay()
        },
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colors.background),
    )
}

@Composable
private fun LaunchedWindowHistoryUpdate(storyState: StoryState, storyOverviewState: StoryOverviewState) {
    val frame = storyState.currentIndex
    val overviewVisible = storyOverviewState.isVisible
    // TODO LaunchedEffect?
    //  - maybe remember would be better since this more of a side effect?
    LaunchedEffect(frame, overviewVisible) {
        val url = URL(window.location.toString())

        val index = storyState.storyboard.indices.binarySearch(frame)
        if (index >= 0) {
            url.searchParams.set("frame", index.toString())
        }

        if (overviewVisible) {
            url.searchParams.set("overview", "true")
        } else {
            url.searchParams.delete("overview")
        }

        window.history.replaceState(null, "", url.toString())
    }
}
