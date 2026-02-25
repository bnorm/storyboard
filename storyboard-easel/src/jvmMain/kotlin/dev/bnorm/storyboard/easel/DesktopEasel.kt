package dev.bnorm.storyboard.easel

import androidx.compose.runtime.*
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.*
import dev.bnorm.storyboard.ContentDecorator
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.assist.rememberAssistantWindow
import dev.bnorm.storyboard.easel.export.ExportMenu
import dev.bnorm.storyboard.easel.export.ExportProgressPopup
import dev.bnorm.storyboard.easel.export.StoryboardPdfExporter
import dev.bnorm.storyboard.easel.overlay.EaselOverlay
import dev.bnorm.storyboard.easel.overlay.EaselOverlayScope
import dev.bnorm.storyboard.easel.overlay.OverlayNavigation
import dev.bnorm.storyboard.easel.overview.EaselOverview
import dev.bnorm.storyboard.plus

@Composable
fun ApplicationScope.DesktopEasel(
    storyboard: () -> Storyboard
) {
    DesktopEasel(rememberAnimatic(storyboard))
}

@Composable
fun ApplicationScope.DesktopEasel(
    animatic: Animatic,
    overlay: @Composable EaselOverlayScope.() -> Unit = {
        OverlayNavigation(animatic)
    },
    windows: List<EaselWindow> = listOf(
        rememberAssistantWindow(animatic)
    ),
) {
    val exporter = remember(animatic.storyboard) { StoryboardPdfExporter() }

    val storyWindowState = rememberWindowState(fileName = "Story")
    var storyWindow by remember { mutableStateOf<ComposeWindow?>(null) }

    @Composable
    fun FrameWindowScope.Toolbar() {
        MenuBar {
            DesktopMenu(animatic.storyboard, storyWindowState, storyWindow)
            ExportMenu(exporter, animatic.storyboard)
            for (window in windows) {
                key(window.name) {
                    Menu(window.name) {
                        with(window) { Menu() }
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
        title = "${animatic.storyboard.title} - Story",
    ) {
        storyWindow = window

        Toolbar()

        val decorator = remember(windows) {
            windows.fold(ContentDecorator.None) { acc, sidecar -> acc + sidecar.decorator }
        }

        decorator.decorate {
            EaselOverview(animatic) {
                EaselOverlay(
                    overlay = {
                        // Only display overlay navigation when *not* fullscreen.
                        if (storyWindowState.placement != WindowPlacement.Fullscreen) {
                            overlay()
                        }
                    }
                ) {
                    Easel(animatic)
                }
            }
        }

        exporter.status?.let { ExportProgressPopup(it) }
    }

    for (window in windows) {
        key(window.name) {
            val state = rememberWindowState(fileName = window.name)
            if (state != null && window.visible) {
                Window(
                    title = "${animatic.storyboard.title} - ${window.name}",
                    state = state,
                    onCloseRequest = { window.visible = false },
                ) {
                    Toolbar()
                    window.Content()
                }
            }
        }
    }
}
