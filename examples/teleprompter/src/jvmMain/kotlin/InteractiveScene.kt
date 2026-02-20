import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.Body
import dev.bnorm.storyboard.easel.template.Header
import dev.bnorm.storyboard.toState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalTransitionApi::class, FlowPreview::class)
fun StoryboardBuilder.InteractiveScene() {
    val initialText = "Sample"
    val textFieldState = TextFieldState(initialText)

    scene(stateCount = 4) {
        val state = transition.createChildTransition { it.toState() }
        TeleprompterOverlay(
            overlay = {
                Input(textFieldState, Modifier.align(Alignment.BottomEnd).padding(32.dp))
            },
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Header { Text("Interactivity") }
                Divider(color = MaterialTheme.colors.primary, thickness = 4.dp)
                Body {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        val input by snapshotFlow { textFieldState.text }
                            .debounce(1.seconds)
                            .collectAsState(initialText)

                        with(state) {
                            RevealAfter(index = 1) {
                                Text("• Scene can be edited from the teleprompter.")
                            }
                            RevealAfter(index = 2) {
                                Text("• For example: $input")
                            }
                            RevealAfter(index = 3) {
                                Text("• Point 3.")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Input(state: TextFieldState, modifier: Modifier) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp,
        color = MaterialTheme.colors.surface,
        border = BorderStroke(2.dp, MaterialTheme.colors.primaryVariant),
        modifier = modifier.width(256.dp),
    ) {
        Box(Modifier.padding(16.dp)) {
            TextField(state)
        }
    }
}
