import androidx.compose.animation.*
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.movableContentOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.core.SlideDecorator
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.core.StoryboardBuilder

val BASIC_STORYBOARD by lazy {
    Storyboard.build(
        name = "Basic Example Storyboard",
        size = Storyboard.DEFAULT_SIZE,
        decorator = theme,
    ) {
        BasicSlide()
    }
}

private fun StoryboardBuilder.BasicSlide() {
    slide(states = listOf(0, 1, 2, 3, 4)) {
        @OptIn(ExperimentalTransitionApi::class)
        val revealIndex = transition.createChildTransition { it.toState() }

        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ProvideTextStyle(MaterialTheme.typography.h1) {
                Text("Basic Slide")
            }

            Spacer(Modifier.fillMaxWidth().height(2.dp).background(MaterialTheme.colors.primary))

            ProvideTextStyle(MaterialTheme.typography.body1) {
                Text("After slide gains focus, advance with arrow keys.")

                ProvideTextStyle(MaterialTheme.typography.body2) {
                    revealIndex.RevealEach {
                        // Reveal items starting with the second state.
                        item(1) {
                            val currentState = this@slide.transition.currentState.toState()
                            val targetState = this@slide.transition.targetState.toState()
                            val transition = when {
                                currentState == targetState -> "$currentState"
                                else -> "$currentState > $targetState"
                            }
                            Text(" * Slides have state: $transition")
                        }

                        item(2) { Text(" * Slides can be advanced with the mouse.") }
                        // Skip a state to indicate mouse navigation.
                        item(4) { Text(" * Press 'Esc' to see overview! (arrows to navigate and 'Enter' to open slide)") }
                    }
                }
            }

            // Align text to the bottom end of the slide.
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                revealIndex.AnimatedVisibility(
                    visible = { it >= 3 },
                    enter = fadeIn() + slideInVertically { 2 * it },
                    exit = fadeOut() + slideOutVertically { 2 * it },
                ) {
                    Text("Down here!", style = MaterialTheme.typography.h6)
                }
            }
        }
    }
}

private val theme = SlideDecorator { content ->
    val colors = darkColors(
        background = Color.Black,
        surface = Color(0xFF1E1F22),
        onBackground = Color(0xFFBCBEC4),
        primary = Color(0xFF7F51FF),
        primaryVariant = Color(0xFF7E53FE),
        secondary = Color(0xFFFDB60D),
    )

    val typography = Typography().run {
        copy(
            h1 = h4.copy(fontWeight = FontWeight.Light),
            h2 = h5.copy(fontWeight = FontWeight.Light),
            h3 = h6.copy(fontWeight = FontWeight.Normal),
            h4 = h6.copy(fontWeight = FontWeight.Normal),
            h5 = h6.copy(fontWeight = FontWeight.Normal),
        )
    }

    MaterialTheme(colors, typography) {
        content()
    }
}

interface RevealBuilder {
    fun item(
        index: Int? = null,
        enterTransition: () -> EnterTransition = { fadeIn() + slideInVertically { -it } },
        exitTransition: () -> ExitTransition = { fadeOut() + slideOutVertically { -it } },
        content: @Composable AnimatedVisibilityScope.() -> Unit,
    )
}

@Composable
fun Transition<Int>.RevealEach(
    block: @DisallowComposableCalls RevealBuilder.() -> Unit,
) {
    val items = mutableListOf<@Composable () -> Unit>()
    object : RevealBuilder {
        var count = 0
        override fun item(
            index: Int?,
            enterTransition: () -> EnterTransition,
            exitTransition: () -> ExitTransition,
            content: @Composable AnimatedVisibilityScope.() -> Unit,
        ) {
            val itemIndex = index ?: count
            count++

            items.add(movableContentOf {
                AnimatedVisibility(
                    visible = { it >= itemIndex },
                    enter = enterTransition(),
                    exit = exitTransition(),
                    content = content,
                )
            })
        }
    }.block()

    for (item in items) {
        item()
    }
}
