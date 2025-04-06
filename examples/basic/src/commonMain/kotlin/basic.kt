import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.Storyboard

fun createStoryboard(): Storyboard {
    return Storyboard.build(
        title = "Basic Storyboard",
        size = Storyboard.DEFAULT_SIZE,
        decorator = theme,
    ) {
        NavigationScene()
        StateScene()
        AnimationScene()
        CodeScene()
    }
}

private val theme = SceneDecorator { content ->
    val colors = darkColors(
        background = Color.Black,
        surface = Color(0xFF1E1F22),
        onBackground = Color(0xFFBCBEC4),
        primary = Color(0xFF7F51FF),
        primaryVariant = Color(0xFF7E53FE),
        secondary = Color(0xFFFDB60D),
    )

    val typography = Typography()

    MaterialTheme(colors, typography) {
        Surface {
            content()
        }
    }
}
