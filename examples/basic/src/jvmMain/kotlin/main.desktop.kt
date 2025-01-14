 import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.window.application
import dev.bnorm.storyboard.easel.DesktopStoryboard

fun main() {
    application {
        MaterialTheme(colors = darkColors()) {
            DesktopStoryboard(BASIC_STORYBOARD)
        }
    }
}
