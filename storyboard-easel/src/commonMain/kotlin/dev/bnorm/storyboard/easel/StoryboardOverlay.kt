package dev.bnorm.storyboard.easel

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.core.AdvanceDirection
import dev.bnorm.storyboard.core.Storyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@Composable
fun StoryboardOverlay(
    storyboard: Storyboard,
    state: OverlayState,
) {
    AnimatedVisibility(
        visible = state.visible,
        enter = fadeIn(), exit = fadeOut()
    ) {
        val alpha by animateFloatAsState(if (state.inside) 0.75f else 0.25f)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha)
        ) {
            if (state.showNavigation) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .onPointerEnterExit(state)
                ) {
                    Row {
                        IconButton(
                            text = "Previous",
                            icon = Icons.AutoMirrored.Rounded.ArrowBack,
                            onClick = { storyboard.advance(AdvanceDirection.Backward) }
                        )
                        IconButton(
                            text = "Next",
                            icon = Icons.AutoMirrored.Rounded.ArrowForward,
                            onClick = { storyboard.advance(AdvanceDirection.Forward) }
                        )
                    }
                }
            }
        }
    }
}

class OverlayState(
    scope: CoroutineScope,
    initialVisibility: Boolean = false,
    val showNavigation: Boolean = true,
) {
    private var timer by mutableStateOf<TimeMark?>(null) // null means not running
    var inside by mutableStateOf(false)
        private set
    var visible by mutableStateOf(initialVisibility)
        private set

    init {
        scope.launch {
            snapshotFlow { timer }.collectLatest {
                if (it != null) {
                    delay(2_000)
                    visible = false
                }
            }
        }
    }

    fun enter() {
        inside = true
        visible = true
        timer = null
    }

    fun exit() {
        inside = false
        timer = TimeSource.Monotonic.markNow()
    }

    fun event() {
        if (!inside) {
            visible = true
            timer = TimeSource.Monotonic.markNow()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun Modifier.onPointerEnterExit(state: OverlayState): Modifier {
    return this
        .onPointerEvent(PointerEventType.Enter) { state.enter() }
        .onPointerEvent(PointerEventType.Exit) { state.exit() }
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun Modifier.onPointerMovePress(state: OverlayState?): Modifier {
    if (state == null) return this
    return this
        .onPointerEvent(PointerEventType.Move) { state.event() }
        .onPointerEvent(PointerEventType.Press) { state.event() }
}

@Composable
private fun IconButton(
    text: String,
    icon: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
        )
    }
}
