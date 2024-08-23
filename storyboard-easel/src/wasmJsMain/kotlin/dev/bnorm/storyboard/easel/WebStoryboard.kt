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

    CanvasBasedWindow(canvasElementId = canvasElementId, title = storyboard.name) {
        LaunchedSearchUpdate(storyboard)

        MaterialTheme(colors = darkColors()) {
            Storyboard(
                storyboard = storyboard,
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colors.background),
            )
        }
    }
}

@Composable
fun LaunchedSearchUpdate(storyboard: Storyboard) {
    val frame = storyboard.currentFrame
    LaunchedEffect(frame) {
        val index = storyboard.frames.binarySearch(frame)
        if (index < 0) return@LaunchedEffect

        val url = URL(window.location.toString())
        url.searchParams.set("frame", index.toString())
        window.history.pushState(null, "", url.toString())
    }
}