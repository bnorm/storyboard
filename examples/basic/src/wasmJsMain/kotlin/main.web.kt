import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.storyboard.easel.WebStory

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget", title = BASIC_STORYBOARD.title) {
        MaterialTheme(colors = darkColors()) {
            WebStory(BASIC_STORYBOARD)
        }
    }
}
