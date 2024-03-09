package dev.bnorm.librettist

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.librettist.show.*
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

@OptIn(ExperimentalComposeUiApi::class)
fun WebSlideShow(
    canvasElementId: String,
    theme: @Composable () -> ShowTheme,
    builder: ShowBuilder.() -> Unit,
) {
    // Pulled from Google Slides with 1 inch = 100 dp
    val slideSize = DpSize(1000.dp, 563.dp)

    val showState = ShowState(builder)

    fun handleKeyEvent(event: KeyEvent): Boolean {
        println(event)

        // TODO rate-limit holding down the key?
        if (event.type == KeyEventType.KeyDown) {
            val advancement = when (event.key) {
                Key.DirectionRight,
                Key.Enter,
                Key.Spacebar,
                -> Advancement(direction = Advancement.Direction.Forward)

                Key.DirectionLeft,
                Key.Backspace,
                -> Advancement(direction = Advancement.Direction.Backward)

                else -> null
            }
            if (advancement != null) {
                showState.advance(advancement)
                return true
            }
        }

        return false
    }

    CanvasBasedWindow(canvasElementId = canvasElementId) {
        val focusRequester = remember { FocusRequester() }
        Box(modifier = Modifier.focusRequester(focusRequester).focusTarget().onKeyEvent(::handleKeyEvent)) {
            SlideShow(
                showState = showState,
                showOverview = false,
                theme = theme(),
                targetSize = slideSize,
            )

            MouseNavigationIndicators(showState)
        }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@Composable
private fun MouseNavigationIndicators(showState: ShowState) {
    val interactionSource = remember { MutableInteractionSource() }
    var visible by remember { mutableStateOf(true) }
    var lastAdvancement by remember { mutableStateOf(TimeSource.Monotonic.markNow()) }

    DisposableEffect(Unit) {
        val handler: AdvancementListener = {
            visible = false
            lastAdvancement = TimeSource.Monotonic.markNow()
        }
        showState.addAdvancementListener(handler)
        onDispose { showState.removeAdvancementListener(handler) }
    }

    if (!visible) {
        LaunchedEffect(lastAdvancement) {
            delay(10.seconds)
            visible = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxHeight().width(200.dp).align(Alignment.CenterStart)
                .clickable(interactionSource, indication = null) {
                    showState.advance(Advancement(direction = Advancement.Direction.Backward))
                }
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = slideInHorizontally { -it },
                exit = slideOutHorizontally { -it },
            ) {
                Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray.copy(alpha = 0.25f))) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "",
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(125.dp).align(Alignment.Center),
                    )
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxHeight().width(200.dp).align(Alignment.CenterEnd)
                .clickable(interactionSource, indication = null) {
                    showState.advance(Advancement(direction = Advancement.Direction.Forward))
                }
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = slideInHorizontally { it },
                exit = slideOutHorizontally { it },
            ) {
                Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray.copy(alpha = 0.25f))) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "",
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(125.dp).align(Alignment.Center),
                    )
                }
            }
        }
    }
}
