package dev.bnorm.storyboard.easel.assist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.*

@Composable
fun StoryAssistantWindow(
    assistantState: StoryAssistantState,
    modifier: Modifier = Modifier,
    menuBar: @Composable MenuBarScope.() -> Unit = {
        StoryAssistantMenu(assistantState)
    },
    windowState: WindowState = rememberWindowState(),
) {
    if (assistantState.visible) {
        Window(
            onCloseRequest = { assistantState.visible = false },
            state = windowState,
            title = "Notes",
        ) {
            MenuBar {
                menuBar()
            }

            StoryAssistant(assistantState, modifier)
        }
    }
}
