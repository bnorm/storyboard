import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.Body
import dev.bnorm.storyboard.easel.template.Header
import dev.bnorm.storyboard.easel.template.RevealEach
import dev.bnorm.storyboard.toState

@OptIn(ExperimentalTransitionApi::class)
fun StoryboardBuilder.TextFieldScene() {
    scene(stateCount = 3) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Header { Text("Text Fields") }
            HorizontalDivider(color = MaterialTheme.colorScheme.primary, thickness = 4.dp)
            Body {
                Column(
                    Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    val text = rememberTextFieldState("World")

                    RevealEach(transition.createChildTransition { it.toState() }) {
                        item { Text("• Text fields can also be used directly in scenes.") }
                        item {
                            TextField(
                                value = text.text.toString(),
                                onValueChange = { text.setTextAndPlaceCursorAtEnd(it) },
                                label = { Text("Name") })
                        }
                        item { Text("• Hello, ${text.text}!") }
                    }
                }
            }
        }
    }
}