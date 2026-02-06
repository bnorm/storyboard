package dev.bnorm.storyboard.easel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.*
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.assist.rememberAssistantWindow
import dev.bnorm.storyboard.easel.export.ExportMenu
import dev.bnorm.storyboard.easel.export.ExportProgressPopup
import dev.bnorm.storyboard.easel.export.StoryboardPdfExporter
import dev.bnorm.storyboard.easel.overlay.OverlayNavigation
import dev.bnorm.storyboard.easel.overlay.StoryOverlayDecorator
import dev.bnorm.storyboard.easel.overlay.StoryOverlayScope
import dev.bnorm.storyboard.plus

@Composable
fun ApplicationScope.DesktopEasel(
    storyboard: () -> Storyboard
) {
    val easel = rememberEasel(storyboard)
    DesktopEasel(easel)
}

@Composable
fun ApplicationScope.DesktopEasel(
    easel: Easel,
    overlay: @Composable StoryOverlayScope.() -> Unit = {
        OverlayNavigation(easel)
    },
    windows: List<EaselWindow> = listOf(rememberAssistantWindow(easel)),
) {
    val exporter = remember(easel.storyboard) { StoryboardPdfExporter() }

    val storyWindowState = rememberWindowState(fileName = "Story")
    var storyWindow by remember { mutableStateOf<ComposeWindow?>(null) }

    @Composable
    fun FrameWindowScope.Toolbar() {
        MenuBar {
            DesktopMenu(easel.storyboard, storyWindowState, storyWindow)
            ExportMenu(exporter, easel.storyboard)
            for (sidecar in windows) {
                key(sidecar.name) {
                    Menu(sidecar.name) {
                        with(sidecar) { Menu() }
                    }
                }
            }
        }
    }

    if (storyWindowState == null) {
        // Need a window to keep the application from closing.
        // TODO splash screen?
        Window(onCloseRequest = ::exitApplication, visible = false) {}
        return
    }

    Window(
        onCloseRequest = ::exitApplication,
        state = storyWindowState,
        title = "${easel.storyboard.title} - Story",
    ) {
        storyWindow = window

        Toolbar()

        val decorator = remember(windows) {
            windows.fold(SceneDecorator.None) { acc, sidecar -> acc + sidecar.decorator }
        }

        StoryEasel(
            easel = easel,
            decorator = decorator + StoryOverlayDecorator {
                // Only display overlay navigation when *not* fullscreen.
                if (storyWindowState.placement != WindowPlacement.Fullscreen) {
                    overlay()
                }
            },
        )

        exporter.status?.let { ExportProgressPopup(it) }
    }

    for (sidecar in windows) {
        key(sidecar.name) {
            val state = rememberWindowState(fileName = sidecar.name)
            if (state != null && sidecar.visible) {
                Window(
                    title = "${easel.storyboard.title} - ${sidecar.name}",
                    state = state,
                    onCloseRequest = { sidecar.visible = false },
                ) {
                    Toolbar()
                    sidecar.Content()
                }
            }
        }
    }
}
