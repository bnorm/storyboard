import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.MenuScope
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.easel.Easel
import dev.bnorm.storyboard.easel.EaselWindow
import dev.bnorm.storyboard.easel.SceneMode
import dev.bnorm.storyboard.easel.Story
import dev.bnorm.storyboard.easel.onStoryNavigation

@Composable
fun rememberTeleprompter(easel: Easel): TeleprompterWindow {
    return remember(easel) { TeleprompterWindow(easel) }
}

class TeleprompterWindow(
    val easel: Easel,
    val mode: SceneMode = SceneMode.Preview,
    override val decorator: SceneDecorator = SceneDecorator.None,
) : EaselWindow {
    override val name: String get() = "Teleprompter"
    override var visible by mutableStateOf(false)

    @Composable
    override fun MenuScope.Menu() {
        CheckboxItem(
            text = "Show",
            checked = visible,
            shortcut = KeyShortcut(Key.F8),
        ) {
            visible = it
        }
    }

    @Composable
    override fun Content() {
        CompositionLocalProvider(LocalTeleprompter provides true) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .onStoryNavigation(easel)
            ) {
                Story(easel, mode, decorator)
            }
        }
    }
}
