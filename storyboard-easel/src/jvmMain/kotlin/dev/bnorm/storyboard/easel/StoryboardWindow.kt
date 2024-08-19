package dev.bnorm.storyboard.easel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.window.*
import dev.bnorm.storyboard.core.Storyboard

@Composable
fun ApplicationScope.StoryboardWindow(
    storyboard: Storyboard,
    title: String = "Untitled",
    windowState: WindowState = rememberWindowState(),
) {
    fun handleKeyEvent(event: KeyEvent): Boolean {
        if (event.type == KeyEventType.KeyUp) {
            when (event.key) {
                Key.F -> if (event.isCtrlPressed && event.isMetaPressed) {
                    windowState.placement = when (windowState.placement) {
                        WindowPlacement.Floating -> WindowPlacement.Fullscreen
                        WindowPlacement.Maximized -> WindowPlacement.Fullscreen
                        WindowPlacement.Fullscreen -> WindowPlacement.Floating // TODO go back to float or max?
                    }
                    return true
                }
            }
        }

        return false
    }

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = title,
        onPreviewKeyEvent = ::handleKeyEvent,
    ) {
        Storyboard(
            storyboard = storyboard,
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colors.background),
        )
    }
}
