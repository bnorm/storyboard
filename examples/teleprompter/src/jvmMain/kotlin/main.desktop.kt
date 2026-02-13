import androidx.compose.ui.window.application
import dev.bnorm.storyboard.easel.DesktopEasel
import dev.bnorm.storyboard.easel.assist.rememberAssistantWindow
import dev.bnorm.storyboard.easel.rememberAnimatic

fun main() {
    application {
        val animatic = rememberAnimatic { createStoryboard() }
        DesktopEasel(
            animatic,
            windows = listOf(
                rememberAssistantWindow(animatic),
                rememberTeleprompter(animatic),
            )
        )
    }
}
