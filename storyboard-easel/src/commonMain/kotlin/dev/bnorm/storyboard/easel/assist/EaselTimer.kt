package dev.bnorm.storyboard.easel.assist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.draw.rotate
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
import dev.bnorm.storyboard.easel.sharedElement
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@Composable
fun EaselTimer(clock: Clock = Clock.System) {
    val timer = remember(clock) { Timer(clock) }
    val duration by timer.flow.collectAsState(initial = 0.seconds)

    fun Long.pad(): String = toString().padStart(2, padChar = '0')

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            "${duration.inWholeHours.pad()}h ${(duration.inWholeMinutes % 60).pad()}m ${(duration.inWholeSeconds % 60).pad()}s",
            fontSize = 32.sp,
            fontFamily = FontFamily.Monospace
        )

        TimerButtons(timer)
    }
}

@Stable
private class Timer(
    val clock: Clock = Clock.System,
) {
    sealed class State {
        class Running(val start: Instant) : State()
        class Paused(val duration: Duration) : State()
        object Stopped : State()
    }

    var state: State by mutableStateOf(State.Stopped)
        private set

    val flow: Flow<Duration> = snapshotFlow { state }.transformLatest { state ->
        when (state) {
            is State.Running -> {
                emit(clock.now() - state.start)
                var next = state.start + 1.seconds
                while (true) {
                    delay(next - clock.now())
                    emit(next - state.start)
                    next += 1.seconds
                }
            }

            is State.Paused -> emit(state.duration)
            State.Stopped -> emit(0.seconds)
        }
    }

    fun onPlayPause() {
        state = when (val s = state) {
            is State.Running -> State.Paused(duration = (clock.now() - s.start).inWholeSeconds.seconds)
            is State.Paused -> State.Running(start = clock.now() - s.duration)
            State.Stopped -> State.Running(start = clock.now())
        }
    }

    fun onReset() {
        state = when (state) {
            is State.Running -> State.Running(start = clock.now())
            is State.Paused -> State.Stopped
            State.Stopped -> State.Stopped
        }
    }
}

@Composable
private fun TimerButtons(timer: Timer) {
    val coroutineScope = rememberCoroutineScope()
    val spin = remember { Animatable(0f) }

    SharedTransitionLayout {
        val transition = updateTransition(timer.state)
        transition.createChildTransition { it is Timer.State.Stopped }.AnimatedContent { stopped ->
            if (!stopped) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    PlayPause(
                        pause = transition,
                        onClick = { timer.onPlayPause() },
                        modifier = Modifier
                            .sharedElement(rememberSharedContentState("play-pause")),
                    )
                    Reset(
                        onClick = {
                            coroutineScope.launch {
                                spin.snapTo(targetValue = 0f)
                                spin.animateTo(targetValue = -360f, animationSpec = tween(500, easing = EaseInOut))
                            }
                            timer.onReset()
                        },
                        modifier = Modifier
                            .sharedElement(rememberSharedContentState("reset"))
                            .rotate(spin.value),
                    )
                }
            } else {
                Box {
                    Reset(
                        onClick = {
                            coroutineScope.launch {
                                spin.snapTo(targetValue = 0f)
                                spin.animateTo(targetValue = -360f, animationSpec = tween(500, easing = EaseInOut))
                            }
                            timer.onReset()
                        },
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState("reset"),
                                boundsTransform = { _, _ -> tween(300, 200, easing = EaseOut) },
                            )
                            .rotate(spin.value),
                    )
                    PlayPause(
                        pause = transition,
                        onClick = { timer.onPlayPause() },
                        modifier = Modifier
                            .sharedElement(rememberSharedContentState("play-pause")),
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
        val fraction by transition.animateFloat(
            transitionSpec = { tween(300, easing = EaseInOut) }
        ) { if (it is Timer.State.Running) 1f else 0f }

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
