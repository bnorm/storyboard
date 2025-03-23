import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.application
import dev.bnorm.storyboard.easel.DesktopStoryboard

fun main() {
    application {
        val storyboard = createStoryboard()
        MaterialTheme(colors = lightColors(background = Color.Gray)) {
            DesktopStoryboard(storyboard)
        }
    }
}
