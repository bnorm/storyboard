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
    menuBar: (@Composable MenuBarScope.() -> Unit)? = null,
    windowState: WindowState = rememberWindowState(),
) {
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = title,
    ) {
        if (menuBar != null) MenuBar(menuBar)

        Storyboard(
            storyboard = storyboard,
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colors.background),
        )
    }
}
