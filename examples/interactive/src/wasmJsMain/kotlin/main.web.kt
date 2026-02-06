import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.storyboard.easel.WebEasel
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val storyboard = createStoryboard()
    val element = document.getElementById("ComposeTarget") as HTMLCanvasElement
    element.focus() // Focus is required for keyboard navigation.
    CanvasBasedWindow(canvasElementId = element.id, title = storyboard.title) {
        MaterialTheme(colors = lightColors(background = Color.Gray)) {
            WebEasel { storyboard }
        }
    }
}
