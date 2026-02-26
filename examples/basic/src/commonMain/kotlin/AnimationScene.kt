import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.enter
import dev.bnorm.storyboard.layout.template.exit
import dev.bnorm.storyboard.layout.template.Header
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.toValue
import kotlin.math.roundToInt

fun StoryboardBuilder.AnimationScene() = scene(
    frameCount = 6,
    enterTransition = enter(end = SceneEnter(alignment = Alignment.CenterEnd)),
    exitTransition = exit(end = SceneExit(alignment = Alignment.CenterEnd)),
) {
    fun <T> quick(): SpringSpec<T> = spring(stiffness = Spring.StiffnessVeryLow)

    @OptIn(ExperimentalTransitionApi::class)
    val state = transition.createChildTransition { it.toValue() }
    val direction = rememberAdvanceDirection()

    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(16.dp)) {
        Header { Text("Animation") }
        Divider(color = MaterialTheme.colors.primary)
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val halfWidth = with(LocalDensity.current) { maxWidth.toPx().roundToInt() } / 2
        val halfHeight = with(LocalDensity.current) { maxWidth.toPx().roundToInt() } / 2


        state.AnimatedVisibility(
            visible = { it == 1 },
            enter = direction.enter(start = { fadeIn(quick()) }, end = { fadeIn() }),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center),
            content = { Text("Things can appear!", style = MaterialTheme.typography.h4) },
        )

        // TODO there seems to be a Compose bug where the first time it goes through the animation
        //  it remembers it and won't change to the opposite direction.
        //  - maybe not all of it though? only the direction it seems.
        state.AnimatedVisibility(
            visible = { it == 2 },
            enter = fadeIn() + direction.enter(
                start = { slideInHorizontally(quick()) { -halfWidth - it / 2 } },
                end = { slideInHorizontally { halfWidth + it / 2 } },
            ),
            exit = fadeOut() + direction.exit(
                start = { slideOutHorizontally { -halfWidth - it / 2 } },
                end = { slideOutHorizontally { halfWidth + it / 2 } },
            ),
            modifier = Modifier.align(Alignment.Center),
            content = { Text("Things can move!", style = MaterialTheme.typography.h4) },
        )

        state.AnimatedVisibility(
            visible = { it in 3..5 },
            enter = direction.enter(
                start = {
                    slideInVertically(
                        spring(
                            stiffness = Spring.StiffnessVeryLow,
                            visibilityThreshold = IntOffset.VisibilityThreshold,
                        )
                    ) { halfHeight + it / 2 }
                },
                end = { slideInVertically { halfHeight + it / 2 } },
            ) + fadeIn(),
            exit = slideOutVertically { halfHeight + it / 2 } + fadeOut(),
            modifier = Modifier.align(Alignment.Center),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("All using Compose animation!", style = MaterialTheme.typography.h4)

                state.AnimatedVisibility(
                    visible = { it >= 4 },
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                ) {
                    Row {
                        Text("With lots of configuration.", style = MaterialTheme.typography.h4)

                        state.AnimatedVisibility(
                            visible = { it >= 5 },
                            enter = fadeIn() + expandHorizontally(),
                            exit = fadeOut() + shrinkHorizontally(),
                        ) {
                            Text(" Like, a lot.", style = MaterialTheme.typography.h4)
                        }
                    }
                }
            }
        }
    }
}
