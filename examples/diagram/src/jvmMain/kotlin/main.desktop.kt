 import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.window.application
import dev.bnorm.storyboard.easel.DesktopStoryEasel

fun main() {
    val storyboard = createStoryboard()
    application {
        MaterialTheme(colors = darkColors()) {
            DesktopStoryEasel(storyboard)
        }
    }
}
