package dev.bnorm.storyboard.easel.notes

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.*
import dev.bnorm.storyboard.core.Storyboard

@Composable
fun StoryboardNotesWindow(
    storyboard: Storyboard,
    notes: StoryboardNotes,
    menuBar: (@Composable MenuBarScope.() -> Unit)? = null,
    windowState: WindowState = rememberWindowState(),
) {
    if (!notes.visible) return

    Window(
        onCloseRequest = { notes.visible = false },
        state = windowState,
        title = "Notes",
    ) {
        if (menuBar != null) MenuBar(menuBar)

        StoryboardNotes(
            storyboard = storyboard,
            notes = notes,
        )
    }
}