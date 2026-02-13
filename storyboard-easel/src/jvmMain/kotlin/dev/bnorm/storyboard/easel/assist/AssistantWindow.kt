package dev.bnorm.storyboard.easel.assist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.MenuScope
import dev.bnorm.storyboard.Decorator
import dev.bnorm.storyboard.easel.Animatic
import dev.bnorm.storyboard.easel.EaselWindow

@Composable
fun rememberAssistantWindow(animatic: Animatic, captions: List<Caption> = emptyList()): AssistantWindow {
    return remember(animatic, captions) { AssistantWindow(animatic, captions) }
}

class AssistantWindow(
    val animatic: Animatic,
    captions: List<Caption> = emptyList(),
) : EaselWindow {
    private val assistantState = StoryAssistantState(animatic, captions)

    override val name: String
        get() = "Assistant"

    override var visible: Boolean
        get() = assistantState.visible
        set(value) {
            assistantState.visible = value
        }

    override val decorator = Decorator { content ->
        CompositionLocalProvider(
            value = LocalCaptions provides assistantState.captions,
            content = content
        )
    }

    @Composable
    override fun MenuScope.Menu() {
        CheckboxItem(
            text = "Show",
            checked = assistantState.visible,
            shortcut = KeyShortcut(Key.F2),
        ) {
            assistantState.visible = it
        }
    }

    @Composable
    override fun Content() {
        StoryAssistant(assistantState)
    }
}
