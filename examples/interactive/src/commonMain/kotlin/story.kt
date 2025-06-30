import androidx.compose.material3.Surface
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.Storyboard

fun createStoryboard(): Storyboard {
    return Storyboard.build(
        title = "Notes Storyboard",
        decorator = theme,
    ) {
        ButtonScene()
        TextFieldScene()
        HttpClientScene()
        AppScene()
        NextScene()
    }
}

private val theme = SceneDecorator { content ->
    Surface {
        content()
    }
}
