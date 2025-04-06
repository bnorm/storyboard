import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.storyboard.easel.WebStoryEasel

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val storyboard = createStoryboard()
    CanvasBasedWindow(canvasElementId = "ComposeTarget", title = storyboard.title) {
        MaterialTheme(colors = lightColors(background = Color.Gray)) {
            WebStoryEasel(storyboard)
        }
    }
}
