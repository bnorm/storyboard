package dev.bnorm.storyboard.easel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.core.Storyboard
import kotlinx.browser.window
import org.w3c.dom.url.URL
import org.w3c.dom.url.URLSearchParams

@Composable
fun WebStoryboard(storyboard: Storyboard) {
    val overview = remember(storyboard) {
        val params = URLSearchParams(window.location.search.toJsString())

        val frameIndex = params.get("frame")?.toIntOrNull()
        if (frameIndex != null) {
            storyboard.jumpTo(storyboard.frames[frameIndex.coerceIn(storyboard.frames.indices)])
        }

        StoryboardOverview.of(storyboard).apply {
            if (params.get("overview").toBoolean()) {
                isVisible = true
            }
        }
    }

    LaunchedSearchUpdate(storyboard, overview)

    val overlayState = rememberOverlayState(
        initialVisibility = true,
    )

    Storyboard(
        storyboard = storyboard,
        overview = overview,
        overlayState = overlayState,
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colors.background),
    )
}

@Composable
fun LaunchedSearchUpdate(storyboard: Storyboard, overview: StoryboardOverview) {
    val frame = storyboard.currentFrame
    val overviewVisible = overview.isVisible
    LaunchedEffect(frame, overviewVisible) {
        val url = URL(window.location.toString())

        val index = storyboard.frames.binarySearch(frame)
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
