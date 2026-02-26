import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.bnorm.storyboard.easel.WebEasel
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val element = document.getElementById("ComposeTarget") as HTMLCanvasElement
    element.focus() // Focus is required for keyboard navigation.
    ComposeViewport(viewportContainer = element) {
        MaterialTheme(colors = darkColors()) {
            WebEasel { createStoryboard() }
        }
    }
}
