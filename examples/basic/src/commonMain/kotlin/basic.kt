import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.SceneFormat
import dev.bnorm.storyboard.Storyboard

fun createStoryboard(): Storyboard {
    return Storyboard.build(
        title = "Basic Storyboard",
        format = SceneFormat.Default,
        decorator = theme,
    ) {
        NavigationScene()
        StateScene()
        AnimationScene()
        CodeScene()
    }
}

private val theme = SceneDecorator { content ->
    val colorScheme = darkColorScheme(
        background = Color.Black,
        surface = Color(0xFF1E1F22),
        onBackground = Color(0xFFBCBEC4),
        primary = Color(0xFF7F51FF),
        secondary = Color(0xFFFDB60D),
    )

    MaterialTheme(colorScheme = colorScheme) {
        Surface {
            content()
        }
    }
}
