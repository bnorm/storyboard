package dev.bnorm.storyboard.easel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.storyboard.core.Storyboard
import kotlinx.browser.window
import org.w3c.dom.url.URL
import org.w3c.dom.url.URLSearchParams

@OptIn(ExperimentalComposeUiApi::class)
fun WebStoryboard(
    storyboard: Storyboard,
    canvasElementId: String = "ComposeTarget",
) {
    val params = URLSearchParams(window.location.search.toJsString())
    val frameIndex = params.get("frame")?.toIntOrNull()
    if (frameIndex != null) {
        storyboard.jumpTo(storyboard.frames[frameIndex.coerceIn(storyboard.frames.indices)])
    }

    val overview = StoryboardOverview.of(storyboard)

    CanvasBasedWindow(canvasElementId = canvasElementId, title = storyboard.name) {
        LaunchedSearchUpdate(storyboard, overview)

        val overlayState = rememberOverlayState(
            initialVisibility = true,
        )

        MaterialTheme(colors = darkColors()) {
            Storyboard(
                storyboard = storyboard,
                overview = overview,
                overlayState = overlayState,
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colors.background),
            )
        }
    }
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

        window.history.pushState(null, "", url.toString())
    }
}