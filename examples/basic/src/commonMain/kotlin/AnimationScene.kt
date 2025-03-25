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
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.easel.enter
import dev.bnorm.storyboard.easel.exit
import dev.bnorm.storyboard.easel.template.Header
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import kotlin.math.roundToInt

fun StoryboardBuilder.AnimationScene() = scene(
    stateCount = 6,
    enterTransition = enter(end = SceneEnter(alignment = Alignment.CenterEnd)),
    exitTransition = exit(end = SceneExit(alignment = Alignment.CenterEnd)),
) {
    @OptIn(ExperimentalTransitionApi::class)
    val state = frame.createChildTransition { it.toState() }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(16.dp)) {
        Header { Text("Animation") }
        Divider(color = MaterialTheme.colors.primary)
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val halfWidth = with(LocalDensity.current) { maxWidth.toPx().roundToInt() } / 2
        val halfHeight = with(LocalDensity.current) { maxWidth.toPx().roundToInt() } / 2

        state.AnimatedVisibility(
            visible = { it == 1 },
            enter = enter(
                start = { fadeIn(spring(stiffness = Spring.StiffnessVeryLow)) },
                end = { fadeIn() },
            ),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center),
            content = { Text("Things can appear!", style = MaterialTheme.typography.h4) },
        )

        state.AnimatedVisibility(
            visible = { it == 2 },
            enter = enter(
                start = {
                    slideInHorizontally(
                        spring(
                            stiffness = Spring.StiffnessVeryLow,
                            visibilityThreshold = IntOffset.VisibilityThreshold,
                        )
                    ) { -halfWidth - it / 2 }
                },
                end = { slideInHorizontally { halfWidth + it / 2 } },
            ) + fadeIn(),
            exit = exit(
                start = { slideOutHorizontally { -halfWidth - it / 2 } },
                end = { slideOutHorizontally { halfWidth + it / 2 } },
            ) + fadeOut(),
            modifier = Modifier.align(Alignment.Center),
            content = { Text("Things can move!", style = MaterialTheme.typography.h4) },
        )

        state.AnimatedVisibility(
            visible = { it in 3..5 },
            enter = enter(
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
