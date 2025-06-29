import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.window.application
import dev.bnorm.storyboard.easel.DesktopEasel

fun main() {
    application {
        MaterialTheme(colors = darkColors()) {
            DesktopEasel { createStoryboard() }
        }
    }
}
