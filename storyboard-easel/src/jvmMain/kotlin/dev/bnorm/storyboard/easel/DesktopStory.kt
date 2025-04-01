package dev.bnorm.storyboard.easel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.*
import dev.bnorm.storyboard.core.ExperimentalStoryStateApi
import dev.bnorm.storyboard.core.StoryState
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.core.rememberStoryState
import dev.bnorm.storyboard.easel.export.ExportProgressPopup
import dev.bnorm.storyboard.easel.export.StoryboardPdfExporter
import dev.bnorm.storyboard.easel.notes.StoryNotes
import kotlinx.coroutines.launch

@OptIn(ExperimentalStoryStateApi::class)
@Composable
fun ApplicationScope.DesktopStory(storyboard: Storyboard) {
    val storyState = rememberStoryState()
    storyState.updateStoryboard(storyboard)
    DesktopStory(storyState)
}

/**
 * This function is designed to be used in combination with Compose Hot-Reload.
 * Make sure a [Storyboard] is already attached to the [StoryState] with [StoryState.updateStoryboard].
 * Each time the storyboard is changed, it can be updated on the state to not revert the story to the first index.
 */
@Composable
@ExperimentalStoryStateApi
fun ApplicationScope.DesktopStory(storyState: StoryState) {
    val notes = remember { StoryNotes() }
    val desktopState = rememberDesktopState(storyState.storyboard)

    val coroutineScope = rememberCoroutineScope()
    val exporter = remember { StoryboardPdfExporter(storyState.storyboard) }

    if (desktopState == null) {
        // Need a window to keep the application from closing.
        // TODO splash screen?
        Window(onCloseRequest = ::exitApplication, visible = false) {}
        return
    }

    @Composable
    fun MenuBarScope.menuBar() {
        Menu("Play") {
            Item(text = "Fullscreen", shortcut = KeyShortcut(Key.P, alt = true, meta = true)) {
                // TODO keynote seems to create a new window which fades in over the entire screen
                //  - is this a better experience then converting the window to full screen?
                //  - would *just* the overview be shown when not in full screen?
                desktopState.storyboard.placement = when (desktopState.storyboard.placement) {
                    WindowPlacement.Floating -> WindowPlacement.Fullscreen
                    WindowPlacement.Maximized -> WindowPlacement.Fullscreen
                    WindowPlacement.Fullscreen -> WindowPlacement.Floating // TODO go back to float or max?
                }
            }
        }
        Menu("Notes") {
            CheckboxItem(text = "Visible", checked = notes.visible, shortcut = KeyShortcut(Key.F2)) {
                notes.visible = it
            }
        }
        Menu("Export") {
            Item(
                text = "PDF",
                enabled = exporter.status == null,
                onClick = { coroutineScope.launch { exporter.export() } },
            )
        }
    }

    Window(
        onCloseRequest = ::exitApplication,
        state = desktopState.storyboard,
        title = storyState.storyboard.title,
    ) {
        MenuBar { menuBar() }

        Story(
            storyState = storyState,
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colors.background),
        )

        exporter.status?.let { ExportProgressPopup(it) }
    }

    if (notes.visible) {
        Window(
            onCloseRequest = { notes.visible = false },
            state = desktopState.notes,
            title = "Notes",
        ) {
            MenuBar { menuBar() }

            StoryNotes(
                storyState = storyState,
                notes = notes,
            )
        }
    }
}
