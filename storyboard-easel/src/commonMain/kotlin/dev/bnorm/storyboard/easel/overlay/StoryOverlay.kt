package dev.bnorm.storyboard.easel.overlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
 import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import dev.bnorm.storyboard.SceneDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.TimeMark
import kotlin.time.TimeSource

fun StoryOverlayDecorator(
    overlay: @Composable StoryOverlayScope.() -> Unit,
): SceneDecorator = SceneDecorator { content ->
    StoryOverlay(
        overlay = overlay,
        content = content,
    )
}

@Composable
private fun StoryOverlay(
    overlay: @Composable StoryOverlayScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val state = remember(coroutineScope) { OverlayState(coroutineScope) }
    Box(modifier = modifier.onPointerMovePress(state)) {
        content()

        Box(Modifier.matchParentSize()) {
            AnimatedVisibility(
                visible = state.visible,
                enter = fadeIn(), exit = fadeOut()
            ) {
                val alpha by animateFloatAsState(if (state.inside) 0.75f else 0.25f)
                Box(Modifier.fillMaxSize().alpha(alpha)) {
                    val scope = remember(key1 = state, key2 = this) {
                        BoxStoryOverlayScope(
                            state = state,
                            boxScope = this
                        )
                    }
                    scope.overlay()
                }
            }
        }
    }
}

private class OverlayState(scope: CoroutineScope) {
    private var timer by mutableStateOf<TimeMark?>(null) // null means not running
    var inside by mutableStateOf(false)
        private set
    var visible by mutableStateOf(false)
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

private class BoxStoryOverlayScope(
    private val state: OverlayState,
    private val boxScope: BoxScope,
) : StoryOverlayScope, BoxScope by boxScope {
    override fun Modifier.overlayElement(): Modifier {
        return onPointerEnterExit(state)
    }
}

private fun Modifier.onPointerEnterExit(state: OverlayState): Modifier {
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

private fun Modifier.onPointerMovePress(state: OverlayState): Modifier {
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
