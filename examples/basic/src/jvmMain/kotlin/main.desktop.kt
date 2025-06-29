import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.window.application
import dev.bnorm.storyboard.easel.DesktopStoryEasel

fun main() {
    val storyboard = createStoryboard()
    application {
        MaterialTheme(colorScheme = darkColorScheme()) {
            DesktopStoryEasel(storyboard)
        }
    }
}
