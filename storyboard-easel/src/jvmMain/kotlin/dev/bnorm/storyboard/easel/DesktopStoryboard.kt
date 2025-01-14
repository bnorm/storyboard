package dev.bnorm.storyboard.easel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.MenuBarScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.easel.notes.LocalStoryboardNotes
import dev.bnorm.storyboard.easel.notes.StoryboardNotes
import dev.bnorm.storyboard.easel.notes.StoryboardNotesWindow

@Composable
fun ApplicationScope.DesktopStoryboard(storyboard: Storyboard) {
    val notes = remember { StoryboardNotes() }
    val state = rememberDesktopState(storyboard)

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
    }

    CompositionLocalProvider(LocalStoryboardNotes provides notes) {
        StoryboardWindow(storyboard, { menuBar() }, state.storyboard)
    }

    StoryboardNotesWindow(storyboard, notes, { menuBar() }, state.notes)
}
