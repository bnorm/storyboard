package dev.bnorm.storyboard.easel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import dev.bnorm.storyboard.Storyboard
import org.w3c.dom.Window
import org.w3c.dom.url.URL
import org.w3c.dom.url.URLSearchParams

@Composable
@OptIn(ExperimentalWasmJsInterop::class)
fun rememberAnimatic(
    window: Window,
    storyboard: () -> Storyboard,
): Animatic {
    val storyboard = remember(storyboard) { storyboard() }
    val initialIndex = remember(window) {
        val params = URLSearchParams(window.location.search.toJsString())

        // TODO what to rename frame to here?
        //  - not really a frame or index
        //  - separate values for scene and state?
        val frameIndex = params.get("frame")?.toIntOrNull()
        if (frameIndex != null) {
            storyboard.indices[frameIndex.coerceIn(storyboard.indices.indices)]
        } else {
            Storyboard.Index(0, 0)
        }
    }
    val animatic = rememberAnimatic(initialIndex) { storyboard }

    LaunchedWindowHistoryUpdate(animatic, window)
    return animatic
}

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
private fun LaunchedWindowHistoryUpdate(animatic: Animatic, window: Window) {
    val index = animatic.currentIndex
    // TODO LaunchedEffect?
    //  - maybe remember/SideEffect would be better since this more of a side effect?
    LaunchedEffect(window, index) {
        val url = URL(window.location.toString())

        val index = animatic.storyboard.indices.binarySearch(index)
        if (index >= 0) {
            url.searchParams.set("frame", index.toString())
        }

        window.history.replaceState(null, "", url.toString())
    }
}
