import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.application
import dev.bnorm.storyboard.easel.DesktopStory

fun main() {
    application {
        val storyboard = createStoryboard()
        MaterialTheme(colors = lightColors(background = Color.Gray)) {
            DesktopStory(storyboard)
        }
    }
}
