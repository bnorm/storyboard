import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.storyboard.easel.WebStoryEasel
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val storyboard = createStoryboard()
    val element = document.getElementById("ComposeTarget") as HTMLCanvasElement
    element.focus() // Focus is required for keyboard navigation.
    CanvasBasedWindow(canvasElementId = element.id, title = storyboard.title) {
        MaterialTheme(colorScheme = lightColorScheme(background = Color.Gray)) {
            WebStoryEasel(storyboard)
        }
    }
}
