import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.Body
import dev.bnorm.storyboard.easel.template.Header
import dev.bnorm.storyboard.toState

@OptIn(ExperimentalTransitionApi::class)
fun StoryboardBuilder.AnnotationScene() {
    scene(stateCount = 4) {
        Box {
            val isTeleprompter = isTeleprompter
            val state = transition.createChildTransition { it.toState() }
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Header { Text("Text Fields") }
                Divider(color = MaterialTheme.colors.primary, thickness = 4.dp)
                Body {

                    @Composable
                    fun Modifier.highlight(index: Int): Modifier {
                        val color = transition.animateColor {
                            if (isTeleprompter && index == it.toState()) MaterialTheme.colors.primary else Color.Transparent
                        }
                        return border(2.dp, color.value)
                    }

                    Column(Modifier.padding(vertical = 8.dp, horizontal = 8.dp)) {
                        with(state) {
                            RevealAfter(index = 1) {
                                Text(
                                    "• Text fields can also be used directly in scenes.",
                                    modifier = Modifier.highlight(1).padding(6.dp)
                                )
                            }
                            RevealAfter(index = 2) {
                                Text(
                                    "• Text fields can also be used directly in scenes.",
                                    modifier = Modifier.highlight(2).padding(6.dp)
                                )
                            }
                            RevealAfter(index = 3) {
                                Text(
                                    "• Text fields can also be used directly in scenes.",
                                    modifier = Modifier.highlight(3).padding(6.dp)
                                )
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
                val message = when (val index = state.currentState) {
                    0 -> """
                        This is the opening note about the teleprompter.
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

@Composable
private fun <T : Comparable<T>> Transition<T>.RevealAfter(
    index: T,
    content: @Composable () -> Unit,
) {
    this.AnimatedVisibility(
        visible = { it >= index },
        enter = fadeIn(), exit = fadeOut(),
    ) {
        content()
    }
}
