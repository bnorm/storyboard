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
fun rememberEasel(
    window: Window,
    storyboard: () -> Storyboard,
): Easel {
    val storyboard = remember(storyboard) { storyboard() }
    val state = remember(window) {
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

        StoryState(initialIndex)
    }
    remember(storyboard) { storyboard().also { state.updateStoryboard(it) } }
    val transition = state.rememberTransition()
    val easel = remember(state, transition) { Easel(state, transition) }

    LaunchedWindowHistoryUpdate(easel, window)
    return easel
}

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
private fun LaunchedWindowHistoryUpdate(easel: Easel, window: Window) {
    val index = easel.currentIndex
    // TODO LaunchedEffect?
    //  - maybe remember/SideEffect would be better since this more of a side effect?
    LaunchedEffect(window, index) {
        val url = URL(window.location.toString())

        val index = easel.storyboard.indices.binarySearch(index)
        if (index >= 0) {
            url.searchParams.set("frame", index.toString())
        }

        window.history.replaceState(null, "", url.toString())
    }
}
