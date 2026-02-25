import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.MenuScope
import dev.bnorm.storyboard.easel.*

val isTeleprompter: Boolean
    @Composable
    @ReadOnlyComposable
    get() = LocalTeleprompter.current

private val LocalTeleprompter = compositionLocalOf { false }

@Composable
fun rememberTeleprompter(animatic: Animatic): TeleprompterWindow {
    return remember(animatic) { TeleprompterWindow(animatic) }
}

@Composable
fun TeleprompterOverlay(
    overlay: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier) {
        content()

        if (isTeleprompter) {
            Box(Modifier.matchParentSize()) {
                MaterialTheme(colors = lightColors()) {
                    overlay()
                }
            }
        }
    }
}

class TeleprompterWindow(
    val animatic: Animatic,
    val mode: SceneMode = SceneMode.Preview,
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
            Easel(
                animatic,
                mode,
                modifier = Modifier
                    .fillMaxSize()
                    .onStoryNavigation(animatic)
            )
        }
    }
}
