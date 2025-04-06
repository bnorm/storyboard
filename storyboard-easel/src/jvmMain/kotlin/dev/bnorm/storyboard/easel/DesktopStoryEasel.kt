package dev.bnorm.storyboard.easel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentWithReceiverOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.*
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.assist.StoryAssistantMenu
import dev.bnorm.storyboard.easel.assist.StoryAssistantState
import dev.bnorm.storyboard.easel.assist.StoryAssistantWindow
import dev.bnorm.storyboard.easel.export.ExportMenu
import dev.bnorm.storyboard.easel.export.ExportProgressPopup
import dev.bnorm.storyboard.easel.export.StoryboardPdfExporter
import dev.bnorm.storyboard.easel.overlay.OverlayNavigation
import dev.bnorm.storyboard.easel.overlay.StoryOverlayScope

@OptIn(ExperimentalStoryStateApi::class)
@Composable
fun ApplicationScope.DesktopStoryEasel(storyboard: Storyboard) {
    val storyState = rememberStoryState()
    storyState.updateStoryboard(storyboard)
    DesktopStoryEasel(storyState)
}

/**
 * This function is designed to be used in combination with Compose Hot-Reload.
 * Make sure a [Storyboard] is already attached to the [StoryState] with [StoryState.updateStoryboard].
 * Each time the storyboard is changed, it can be updated on the state to not revert the story to the first index.
 */
@Composable
@ExperimentalStoryStateApi
fun ApplicationScope.DesktopStoryEasel(
    storyState: StoryState,
    overlay: @Composable StoryOverlayScope.() -> Unit = {
        OverlayNavigation(storyState)
    },
) {
    val desktopState = rememberDesktopState(storyState.storyboard)
    val assistantState = remember(storyState) { StoryAssistantState(storyState) }
    val exporter = remember(storyState.storyboard) { StoryboardPdfExporter() }

    if (desktopState == null) {
        // Need a window to keep the application from closing.
        // TODO splash screen?
        Window(onCloseRequest = ::exitApplication, visible = false) {}
        return
    }

    val menuBar = movableContentWithReceiverOf<MenuBarScope> {
        DesktopMenu(desktopState.story) {
            StoryAssistantMenu(assistantState)
            ExportMenu(exporter, storyState)
        }
    }

    StoryEaselWindow(
        storyState = storyState,
        onCloseRequest = ::exitApplication,
        windowState = desktopState.story,
        menuBar = menuBar,
        overlay = overlay,
        exporter = exporter,
    )

    StoryAssistantWindow(
        assistantState = assistantState,
        menuBar = menuBar,
        windowState = desktopState.assistant,
    )
}

@Composable
private fun StoryEaselWindow(
    storyState: StoryState,
    onCloseRequest: () -> Unit,
    modifier: Modifier = Modifier,
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


@Composable
fun MenuBarScope.DesktopMenu(
    windowState: WindowState,
    content: @Composable MenuBarScope.() -> Unit,
) {
    Menu("Play") {
        Item(text = "Fullscreen", shortcut = KeyShortcut(Key.P, alt = true, meta = true)) {
            // TODO keynote seems to create a new window which fades in over the entire screen
            //  - is this a better experience then converting the window to full screen?
            //  - would *just* the overview be shown when not in full screen?
            windowState.placement = when (windowState.placement) {
                WindowPlacement.Floating -> WindowPlacement.Fullscreen
                WindowPlacement.Maximized -> WindowPlacement.Fullscreen
                WindowPlacement.Fullscreen -> WindowPlacement.Floating // TODO go back to float or max?
            }
        }
    }
    content()
}
