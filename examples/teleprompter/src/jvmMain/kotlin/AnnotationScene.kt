import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.Body
import dev.bnorm.storyboard.easel.template.Header
import dev.bnorm.storyboard.toState

@OptIn(ExperimentalTransitionApi::class)
fun StoryboardBuilder.AnnotationScene() {
    scene(stateCount = 4) {
        Box(Modifier.fillMaxSize()) {
            val state = transition.createChildTransition { it.toState() }
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Header { Text("Highlighting") }
                Divider(color = MaterialTheme.colors.primary, thickness = 4.dp)
                Body {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        with(state) {
                            RevealAfter(index = 1, highlight = true) {
                                Text("• In the teleprompter window, things can be highlighted when they first appear.")
                            }
                            RevealAfter(index = 2, highlight = true) {
                                Text("• This can help a speaker focus on the right content.")
                            }
                            RevealAfter(index = 3, highlight = true) {
                                Text("• Or highlight specifics a speaker should talk about.")
                            }
                        }
                    }
                }
            }

            if (isTeleprompter) {
                Notes(state, Modifier.align(Alignment.TopEnd).padding(32.dp))
            }
        }
    }
}

@Composable
private fun Notes(state: Transition<Int>, modifier: Modifier) {
    MaterialTheme(colors = lightColors()) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            elevation = 8.dp,
            color = MaterialTheme.colors.surface,
            border = BorderStroke(2.dp, MaterialTheme.colors.primaryVariant),
            modifier = modifier.width(256.dp),
        ) {
            Box(Modifier.padding(16.dp)) {
                val message = when (state.currentState) {
                    0 -> """
                        This is an opening note about this scene.
                    """.trimIndent()

                    1 -> """
                        This first bullet point is very important.
                    """.trimIndent()

                    2 -> """
                        This second bullet point, not so much.
                    """.trimIndent()

                    3 -> """
                        Now the third bullet point, don't even get me started.
                    """.trimIndent()

                    else -> "! error !"
                }
                Text(message)
            }
        }
    }
}
