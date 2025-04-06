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
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.assist.SceneCaption
import dev.bnorm.storyboard.easel.template.Body
import dev.bnorm.storyboard.easel.template.Header
import dev.bnorm.storyboard.scene
import dev.bnorm.storyboard.toState

class StateSceneState(
    val index: Int,
    val title: String,
)

fun StoryboardBuilder.StateScene() = scene(
    StateSceneState(
        index = 0,
        title = "State",
    ),
    StateSceneState(
        index = 1,
        title = "State",
    ),
    StateSceneState(
        index = 2,
        title = "State",
    ),
    StateSceneState(
        index = 3,
        title = "State",
    ),
    StateSceneState(
        index = 4,
        title = "Hello, State!",
    ),
    StateSceneState(
        index = 5,
        title = "Hello, State!",
    ),
    StateSceneState(
        index = 6,
        title = "Hello, State!",
    ),
) {
    val currentState = frame.currentState.toState()
    val index = currentState.index
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Header { Text(currentState.title) }
        Divider(color = MaterialTheme.colors.primary)
        Body {
            frame.AnimatedVisibility(
                visible = { it != Frame.End },
                enter = fadeIn(), exit = fadeOut(),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Each scene has a sequence of states.")
                    if (index >= 1) Text("    • Scenes transition through their states: '$index'")
                    if (index >= 2) Text("    • The state can be of whatever type you want.")
                    if (index >= 3) {
                        Row {
                            Text("    • This allows changing of scene content dynamically!")
                            if (index >= 4) Text(" (Like the scene title!)")
                        }
                    }
                    if (index >= 5) Text("    • The start and end of a scene have a special, temporary state.")
                    if (index >= 6) Text("        ◦ This helps with animations between scenes!")
                }
            }
        }
    }

    SceneCaption {
        when (index) {
            in 0..3 -> Text("Nice to meeting you!")
            else -> Text("How are you today?")
        }
    }
}
