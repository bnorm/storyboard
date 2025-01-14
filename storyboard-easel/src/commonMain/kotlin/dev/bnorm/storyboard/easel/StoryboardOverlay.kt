package dev.bnorm.storyboard.easel

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.core.AdvanceDirection
import dev.bnorm.storyboard.core.Storyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.TimeMark
import kotlin.time.TimeSource

interface SlideOverlayScope : BoxScope {
    fun Modifier.overlayElement(): Modifier
}

@Composable
fun StoryboardOverlay(
    storyboard: Storyboard,
    state: OverlayState,
    content: @Composable SlideOverlayScope.() -> Unit = {
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .overlayElement()
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
    },
) {
    AnimatedVisibility(
        visible = state.visible,
        enter = fadeIn(), exit = fadeOut()
    ) {
        // TODO keep overlay only ever over the slide? aspectRatio(storyboard.size.aspectRatio)
        val alpha by animateFloatAsState(if (state.inside) 0.75f else 0.25f)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha)
        ) {
            val scope = remember(state) {
                object : SlideOverlayScope, BoxScope by this {
                    override fun Modifier.overlayElement(): Modifier {
                        return onPointerEnterExit(state)
                    }
                }
            }

            scope.content()
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

@Composable
fun rememberOverlayState(
    initialVisibility: Boolean = false,
    showNavigation: Boolean = true,
): OverlayState {
    val coroutineScope = rememberCoroutineScope()
    val state = remember(coroutineScope) {
        OverlayState(
            scope = coroutineScope,
            initialVisibility = initialVisibility,
            showNavigation = showNavigation,
        )
    }
    return state
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun Modifier.onPointerEnterExit(state: OverlayState): Modifier {
    return pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent(PointerEventPass.Main)
                when (event.type) {
                    PointerEventType.Enter -> state.enter()
                    PointerEventType.Exit -> state.exit()
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun Modifier.onPointerMovePress(state: OverlayState?): Modifier {
    if (state == null) return this
    return pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent(PointerEventPass.Main)
                when (event.type) {
                    PointerEventType.Move -> state.event()
                    PointerEventType.Press -> state.event()
                }
            }
        }
    }
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
        enabled = enabled,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier
                .pointerHoverIcon(PointerIcon.Hand),
        )
    }
}
