import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import dev.bnorm.storyboard.ContentDecorator
import dev.bnorm.storyboard.SceneFormat
import dev.bnorm.storyboard.Storyboard

fun createStoryboard(): Storyboard {
    return Storyboard.build(
        title = "Diagram Storyboard",
        format = SceneFormat.Default,
        decorator = theme,
    ) {
        ConnectedTree()
    }
}

private val theme = ContentDecorator { content ->
    val colors = lightColors()
    val typography = Typography()
    MaterialTheme(colors, typography) {
        Surface {
            content()
        }
    }
}
