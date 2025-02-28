package dev.bnorm.storyboard.easel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.*
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.easel.export.ExportProgressPopup
import dev.bnorm.storyboard.easel.export.StoryboardPdfExporter
import dev.bnorm.storyboard.easel.notes.LocalStoryboardNotes
import dev.bnorm.storyboard.easel.notes.StoryboardNotes
import kotlinx.coroutines.launch

@Composable
fun ApplicationScope.DesktopStoryboard(storyboard: Storyboard) {
    val notes = remember { StoryboardNotes() }
    val state = rememberDesktopState(storyboard)

    val coroutineScope = rememberCoroutineScope()
    val exporter = remember { StoryboardPdfExporter(storyboard) }

    if (state == null) {
        // Need a window to keep the application from closing
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
                state.storyboard.placement = when (state.storyboard.placement) {
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
        state = state.storyboard,
        title = storyboard.title,
    ) {
        MenuBar { menuBar() }

        CompositionLocalProvider(LocalStoryboardNotes provides notes) {
            Storyboard(
                storyboard = storyboard,
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colors.background),
            )
        }

        exporter.status?.let { ExportProgressPopup(it) }
    }

    if (notes.visible) {
        Window(
            onCloseRequest = { notes.visible = false },
            state = state.notes,
            title = "Notes",
        ) {
            MenuBar { menuBar() }

            StoryboardNotes(
                storyboard = storyboard,
                notes = notes,
            )
        }
    }
}
