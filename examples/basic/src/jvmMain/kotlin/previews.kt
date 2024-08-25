import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import dev.bnorm.storyboard.ui.StoryboardPreview

@Preview
@Composable
fun BasicSlidePreview() {
    StoryboardPreview(decorator = BASIC_STORYBOARD.decorator) {
        NavigationSlide()
    }
}

@Preview
@Composable
fun CodeSlidePreview() {
    StoryboardPreview(decorator = BASIC_STORYBOARD.decorator) {
        CodeSlide()
    }
}

@Preview
@Composable
fun AnimationSlidePreview() {
    StoryboardPreview(decorator = BASIC_STORYBOARD.decorator) {
        AnimationSlide()
    }
}
