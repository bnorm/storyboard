package dev.bnorm.storyboard.easel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.storyboard.core.AdvanceDirection
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

    val isTouchableDevice = window.navigator.maxTouchPoints > 0

    CanvasBasedWindow(canvasElementId = canvasElementId, title = storyboard.name) {
        LaunchedSearchUpdate(storyboard)

        MaterialTheme(colors = darkColors()) {
            Storyboard(
                storyboard = storyboard,
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colors.background),
            )

            if (isTouchableDevice) {
                Row {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { storyboard.advance(AdvanceDirection.Backward) }
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { storyboard.advance(AdvanceDirection.Forward) }
                    )
                }
            }
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