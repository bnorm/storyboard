import androidx.compose.material.Surface
import dev.bnorm.storyboard.ContentDecorator
import dev.bnorm.storyboard.Storyboard

fun createStoryboard(): Storyboard {
    return Storyboard.build(
        title = "Interactive Storyboard",
        decorator = theme,
    ) {
        ButtonScene()
        TextFieldScene()
        HttpClientScene()
        AppScene()
        NextScene()
    }
}

private val theme = ContentDecorator { content ->
    Surface {
        content()
    }
}
