package dev.bnorm.storyboard.easel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.core.StoryboardState
import kotlinx.browser.window
import org.w3c.dom.url.URL
import org.w3c.dom.url.URLSearchParams

@Composable
fun WebStoryboard(storyboard: Storyboard) {
    val storyboardState = remember(storyboard) {
        val params = URLSearchParams(window.location.search.toJsString())

        val frameIndex = params.get("frame")?.toIntOrNull()
        val initialFrame = if (frameIndex != null) {
            StoryboardState(storyboard)
            storyboard.frames[frameIndex.coerceIn(storyboard.frames.indices)]
        } else {
            Storyboard.Frame(0, 0)
        }

        StoryboardState(storyboard, initialFrame)
    }
    val overview = remember(storyboard) {
        val params = URLSearchParams(window.location.search.toJsString())

        StoryboardOverview.of(storyboardState).apply {
            if (params.get("overview").toBoolean()) {
                isVisible = true
            }
        }
    }

    LaunchedHistoryUpdate(storyboardState, overview)

    val overlayState = rememberOverlayState(
        initialVisibility = true,
    )

    Storyboard(
        storyboard = storyboardState,
        overview = overview,
        overlayState = overlayState,
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colors.background),
    )
}

@Composable
fun LaunchedHistoryUpdate(storyboard: StoryboardState, overview: StoryboardOverview) {
    val frame = storyboard.currentFrame
    val overviewVisible = overview.isVisible
    LaunchedEffect(frame, overviewVisible) {
        val url = URL(window.location.toString())

        val index = storyboard.storyboard.frames.binarySearch(frame)
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
