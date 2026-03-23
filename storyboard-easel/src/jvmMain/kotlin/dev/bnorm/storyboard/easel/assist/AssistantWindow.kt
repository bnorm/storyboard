package dev.bnorm.storyboard.easel.assist

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.MenuScope
import dev.bnorm.storyboard.ContentDecorator
import dev.bnorm.storyboard.easel.Animatic
import dev.bnorm.storyboard.easel.EaselWindow

@Composable
fun rememberAssistantWindow(animatic: Animatic, captions: List<Caption> = emptyList()): AssistantWindow {
    return remember(animatic, captions) { AssistantWindow(animatic, captions) }
}

class AssistantWindow(
    private val animatic: Animatic,
    initialCaptions: List<Caption> = emptyList(),
) : EaselWindow {
    private val captions = SnapshotStateList<Caption>().apply {
        addAll(initialCaptions)
    }

    override val name: String
        get() = "Assistant"

    override var visible: Boolean by mutableStateOf(false)

    override val decorator = ContentDecorator { content ->
        CompositionLocalProvider(
            value = LocalCaptions provides this.captions,
            content = content
        )
    }

    @Composable
    override fun MenuScope.Menu() {
        CheckboxItem(
            text = "Show",
            checked = visible,
            shortcut = KeyShortcut(Key.F2),
        ) {
            visible = it
        }
    }

    @Composable
    override fun Content() {
        EaselAssistant(animatic = animatic, captions = captions)
    }
}
