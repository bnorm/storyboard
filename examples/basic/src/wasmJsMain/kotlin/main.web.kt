import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.storyboard.easel.WebStoryEasel

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val storyboard = createStoryboard()
    CanvasBasedWindow(canvasElementId = "ComposeTarget", title = storyboard.title) {
        MaterialTheme(colors = darkColors()) {
            WebStoryEasel(storyboard)
        }
    }
}
