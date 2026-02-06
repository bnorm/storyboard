import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.application
import dev.bnorm.storyboard.easel.DesktopEasel

fun main() {
    application {
        MaterialTheme(colors = lightColors(background = Color.Gray)) {
            DesktopEasel { createStoryboard() }
        }
    }
}
