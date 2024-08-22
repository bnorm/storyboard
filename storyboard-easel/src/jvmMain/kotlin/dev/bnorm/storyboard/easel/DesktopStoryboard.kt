package dev.bnorm.storyboard.easel

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.MenuBarScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.easel.notes.LocalStoryboardNotes
import dev.bnorm.storyboard.easel.notes.StoryboardNotes
import dev.bnorm.storyboard.easel.notes.StoryboardNotesWindow

fun DesktopStoryboard(storyboard: Storyboard) {
    application {
        val notes = remember { StoryboardNotes() }
        val state = rememberDesktopState(storyboard)

        if (state == null) {
            // Need a window to keep the application from closing
            Window(onCloseRequest = ::exitApplication, visible = false) {}
            return@application
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
        }

        MaterialTheme(colors = darkColors()) {
            CompositionLocalProvider(LocalStoryboardNotes provides notes) {
                StoryboardWindow(storyboard, { menuBar() }, state.storyboard)
            }

            StoryboardNotesWindow(storyboard, notes, { menuBar() }, state.notes)
        }
    }
}
