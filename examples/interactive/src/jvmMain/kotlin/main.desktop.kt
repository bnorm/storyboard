import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.application
import dev.bnorm.storyboard.easel.DesktopStoryEasel

fun main() {
    val storyboard = createStoryboard()
    application {
        MaterialTheme(colors = lightColors(background = Color.Gray)) {
            DesktopStoryEasel(storyboard)
        }
    }
}
