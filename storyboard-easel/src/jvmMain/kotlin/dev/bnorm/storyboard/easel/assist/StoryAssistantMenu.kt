package dev.bnorm.storyboard.easel.assist

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.MenuBarScope

@Composable
fun MenuBarScope.StoryAssistantMenu(assistantState: StoryAssistantState) {
    Menu("Assistant") {
        CheckboxItem(
            text = "Show",
            checked = assistantState.visible,
            shortcut = KeyShortcut(Key.F2),
        ) {
            assistantState.visible = it
        }
    }
}
