import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.SceneScope
import kotlin.time.Duration

@Composable
fun SceneScope<*>.StateTimer(
    duration: Duration,
    warnings: List<Int> = listOf(10, 5, 3, 2, 1),
    modifier: Modifier = Modifier
) {
    key(duration) {
        Box(modifier.fillMaxSize()) {
            val percent = remember(transition.currentState) { Animatable(0f) }
            LaunchedEffect(transition.currentState) {
                percent.animateTo(1f, animationSpec = tween(duration.inWholeMilliseconds.toInt(), easing = LinearEasing))
            }

            // Progress bar.
            Box(
                Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(percent.value)
                    .height(8.dp)
                    .background(Color.White)
            )

            // Count down warnings.
            val remainingMs = (1f - percent.value) * duration.inWholeMilliseconds
            for (warning in warnings) {
                AnimatedVisibility(
                    visible = (remainingMs - warning * 1_000).toInt() in 1..500,
                    enter = fadeIn(tween(300)), exit = fadeOut(tween(300)) + scaleOut(tween(1_000)),
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    Text(warning.toString(), color = Color.White, style = MaterialTheme.typography.h1)
                }
            }
        }
    }
}
