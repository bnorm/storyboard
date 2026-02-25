import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.Header

@OptIn(ExperimentalTransitionApi::class)
fun StoryboardBuilder.IntroScene() {
    scene(frameCount = 1) {
        Header {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (!isTeleprompter) {
                        Text("Welcome to the Teleprompter example!")
                        Text("Press F8 to open the teleprompter.")
                    } else {
                        Text("Teleprompter message is different!")
                    }
                }
            }
        }
    }
}
