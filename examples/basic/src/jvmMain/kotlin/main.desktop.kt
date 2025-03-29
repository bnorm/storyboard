 import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.window.application
import dev.bnorm.storyboard.easel.DesktopStory

fun main() {
    application {
        MaterialTheme(colors = darkColors()) {
            DesktopStory(BASIC_STORYBOARD)
        }
    }
}
