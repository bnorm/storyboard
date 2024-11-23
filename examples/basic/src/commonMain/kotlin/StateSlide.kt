import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.core.SlideState
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.easel.template.Body
import dev.bnorm.storyboard.easel.template.Header

class StateSlideState(
    val index: Int,
    val title: String,
)

fun StoryboardBuilder.StateSlide() = slide(
    StateSlideState(
        index = 0,
        title = "State",
    ),
    StateSlideState(
        index = 1,
        title = "State",
    ),
    StateSlideState(
        index = 2,
        title = "State",
    ),
    StateSlideState(
        index = 3,
        title = "State",
    ),
    StateSlideState(
        index = 4,
        title = "Hello, State!",
    ),
    StateSlideState(
        index = 5,
        title = "Hello, State!",
    ),
    StateSlideState(
        index = 6,
        title = "Hello, State!",
    ),
) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        val currentState = currentState
        val index = currentState.index

        Header { Text(currentState.title) }
        Divider(color = MaterialTheme.colors.primary)
        Body {
            state.AnimatedVisibility(
                visible = { it != SlideState.End },
                enter = fadeIn(), exit = fadeOut(),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Each slide has a sequence of states.")
                    if (index >= 1) Text("    • Slides transition through their states: '$index'")
                    if (index >= 2) Text("    • The state can be of whatever type you want.")
                    if (index >= 3) {
                        Row {
                            Text("    • This allows changing of slide content dynamically!")
                            if (index >= 4) Text(" (Like the slide title!)")
                        }
                    }
                    if (index >= 5) Text("    • The start and end of a slide have a special, temporary state.")
                    if (index >= 6) Text("        ◦ This helps with animations between slides!")
                }
            }
        }
    }
}
