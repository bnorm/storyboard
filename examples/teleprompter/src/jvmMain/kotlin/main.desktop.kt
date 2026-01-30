import androidx.compose.ui.window.application
import dev.bnorm.storyboard.easel.DesktopEasel
import dev.bnorm.storyboard.easel.assist.rememberAssistantWindow
import dev.bnorm.storyboard.easel.rememberEasel

fun main() {
    application {
        val easel = rememberEasel { createStoryboard() }
        DesktopEasel(
            easel,
            windows = listOf(
                rememberAssistantWindow(easel),
                rememberTeleprompter(easel),
            )
        )
    }
}
