import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.MenuScope
import dev.bnorm.storyboard.Decorator
import dev.bnorm.storyboard.easel.Animatic
import dev.bnorm.storyboard.easel.EaselWindow
import dev.bnorm.storyboard.easel.SceneMode
import dev.bnorm.storyboard.easel.Easel
import dev.bnorm.storyboard.easel.onStoryNavigation

val isTeleprompter: Boolean
    @Composable
    @ReadOnlyComposable
    get() = LocalTeleprompter.current

private val LocalTeleprompter = compositionLocalOf { false }

@Composable
fun rememberTeleprompter(animatic: Animatic): TeleprompterWindow {
    return remember(animatic) { TeleprompterWindow(animatic) }
}

class TeleprompterWindow(
    val animatic: Animatic,
    val mode: SceneMode = SceneMode.Preview,
    override val decorator: Decorator = Decorator.None,
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
                    .onStoryNavigation(animatic)
            ) {
                Easel(animatic, mode, decorator)
            }
        }
    }
}
