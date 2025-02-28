package dev.bnorm.storyboard.easel.notes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@Stable
class Timer(
    val timeSource: TimeSource = TimeSource.Monotonic,
) {
    enum class State {
        Stopped,
        Running,
        Paused,
    }

    var state: State by mutableStateOf(State.Stopped)
        private set

    private var start by mutableStateOf<TimeMark?>(null)
    private var duration by mutableStateOf(0.milliseconds)

    fun onPlayPause() {
        if (start == null) {
            start = timeSource.markNow() - duration
            state = State.Running
        } else {
            start?.let { duration = it.elapsedNow() }
            start = null
            state = State.Paused
        }
    }

    fun onReset() {
        duration = 0.milliseconds
        if (start != null) {
            start = timeSource.markNow()
        } else {
            state = State.Stopped
        }
    }

    suspend fun await() {
        // TODO there has to be a better way to achieve this state update...
        val mark = start ?: return
        while (true) {
            delay(250.milliseconds)
            duration = mark.elapsedNow()
        }
    }

    override fun toString(): String {
        fun Long.pad(): String = toString().padStart(2, padChar = '0')
        return "${duration.inWholeHours.pad()}h ${(duration.inWholeMinutes % 60).pad()}m ${(duration.inWholeSeconds % 60).pad()}s"
    }
}

@Composable
fun StoryboardTimer(timeSource: TimeSource = TimeSource.Monotonic) {
    val timer = remember(timeSource) { Timer(timeSource) }
    LaunchedEffect(timer.state) { timer.await() }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            timer.toString(),
            fontSize = 32.sp,
            fontFamily = FontFamily.Monospace
        )

        TimerButtons(timer)
    }
}

@Composable
private fun TimerButtons(timer: Timer) {
    SharedTransitionLayout {
        val transition = updateTransition(timer.state)
        transition.AnimatedContent {
            if (it != Timer.State.Stopped) {
                Row {
                    PlayPause(
                        pause = transition,
                        onClick = { timer.onPlayPause() },
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState("play-pause"),
                            animatedVisibilityScope = this@AnimatedContent
                        ),
                    )
                    Reset(
                        onClick = { timer.onReset() },
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState("reset"),
                            animatedVisibilityScope = this@AnimatedContent
                        ),
                    )
                }
            } else {
                Box {
                    Reset(
                        onClick = { timer.onReset() },
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState("reset"),
                            animatedVisibilityScope = this@AnimatedContent
                        ),
                    )
                    PlayPause(
                        pause = transition,
                        onClick = { timer.onPlayPause() },
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState("play-pause"),
                            animatedVisibilityScope = this@AnimatedContent
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayPause(
    pause: Transition<Timer.State>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .padding(start = 16.dp)
            .pointerHoverIcon(PointerIcon.Hand)
            .background(MaterialTheme.colors.primary, shape = CircleShape)
    ) {
        Icon(
            PlayPauseIcon(pause),
            tint = MaterialTheme.colors.onPrimary,
            contentDescription = "",
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun Reset(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .padding(start = 16.dp)
            .pointerHoverIcon(PointerIcon.Hand)
            .background(MaterialTheme.colors.primary, shape = CircleShape)
    ) {
        Icon(
            Icons.Filled.Refresh,
            tint = MaterialTheme.colors.onPrimary,
            contentDescription = "",
            modifier = Modifier.size(32.dp).scale(scaleX = -1f, scaleY = 1f)
        )
    }
}

@Composable
private fun PlayPauseIcon(transition: Transition<Timer.State>): VectorPainter {
    return rememberVectorPainter(
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
        autoMirror = false,
    ) { _, _ ->
        val fraction by transition.animateFloat { if (it == Timer.State.Running) 1f else 0f }

        Path(
            pathData = PathData {
                moveTo(6f, 5f)
                horizontalLineTo(6f + 4f * fraction)
                verticalLineTo(19f)
                horizontalLineTo(6f)
                close()

                moveTo(8f + 6f * fraction, 5f)
                lineTo(8f + 10f * fraction, 5f)
                lineTo(19f - 1f * fraction, 12f)
                lineTo(8f + 10f * fraction, 19f)
                lineTo(8f + 6f * fraction, 19f)
                close()
            },

            fill = SolidColor(Color.White)
        )
    }
}
