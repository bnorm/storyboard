package dev.bnorm.storyboard.easel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.*
import dev.bnorm.storyboard.easel.export.ExportProgressPopup
import dev.bnorm.storyboard.easel.export.StoryboardPdfExporter
import dev.bnorm.storyboard.easel.overlay.StoryOverlayScope

@Composable
fun StoryEaselWindow(
    storyState: StoryState,
    onCloseRequest: () -> Unit,
    modifier: Modifier = Modifier.Companion,
    windowState: WindowState = rememberWindowState(),
    menuBar: @Composable MenuBarScope.() -> Unit = {},
    overlay: @Composable StoryOverlayScope.() -> Unit = {},
    exporter: StoryboardPdfExporter,
) {
    Window(
        onCloseRequest = onCloseRequest,
        state = windowState,
        title = storyState.storyboard.title,
    ) {
        MenuBar { menuBar() }

        StoryEasel(
            storyState = storyState,
            overlay = {
                // Only display overlay navigation when *not* fullscreen.
                if (windowState.placement != WindowPlacement.Fullscreen) {
                    overlay()
                }
            },
            modifier = modifier.fillMaxSize()
                .background(MaterialTheme.colors.background),
        )

        exporter.status?.let { ExportProgressPopup(it) }
    }
}
