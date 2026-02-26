import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.ComposeViewport
import dev.bnorm.storyboard.easel.WebEasel

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport("composeApp") {
        MaterialTheme(colors = lightColors(background = Color.Gray)) {
            WebEasel { createStoryboard() }
        }
    }
}
