import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.application
import dev.bnorm.storyboard.easel.DesktopStoryEasel

fun main() {
    val storyboard = createStoryboard()
    application {
        MaterialTheme(colorScheme = lightColorScheme(background = Color.Gray)) {
            DesktopStoryEasel(storyboard)
        }
    }
}
