package dev.bnorm.librettist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.bnorm.librettist.show.Advancement
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.ShowState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

@Composable
fun EmbeddedSlideShow(
    theme: ShowTheme,
    slideSize: DpSize = DEFAULT_SLIDE_SIZE,
    showIndicators: Boolean = true,
    builder: ShowBuilder.() -> Unit,
) {
    val showState = remember(builder) { ShowState(builder) }
    var visibleIndicators by remember(showIndicators) { mutableStateOf(showIndicators) }
    var lastAdvancement by remember { mutableStateOf(TimeSource.Monotonic.markNow()) }

    if (showIndicators && !visibleIndicators) {
        LaunchedEffect(lastAdvancement) {
            delay(10.seconds)
            visibleIndicators = true
        }
    }

    fun advance(advancement: Advancement): Boolean {
        if (showState.advance(advancement)) {
            visibleIndicators = false
            lastAdvancement = TimeSource.Monotonic.markNow()
            return true
        } else {
            return false
        }
    }

    fun handleKeyEvent(event: KeyEvent): Boolean {
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
            if (advancement != null && advance(advancement)) {
                return true
            }
        }

        return false
    }

    val focusRequester = remember { FocusRequester() }
    ShowTheme(theme) {
        Box(modifier = Modifier.focusRequester(focusRequester).focusTarget().onKeyEvent(::handleKeyEvent)) {
            SlideShowDisplay(
                showState = showState,
                slideSize = slideSize,
                modifier = Modifier.fillMaxSize()
            )

            MouseNavigationIndicators(onAdvancement = { advance(it) }, visibleIndicators)
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun MouseNavigationIndicators(onAdvancement: (Advancement) -> Unit, visibleIndicators: Boolean = true) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxHeight().width(200.dp).align(Alignment.CenterStart)
                .clickable(interactionSource, indication = null) {
                    onAdvancement(Advancement(direction = Advancement.Direction.Backward))
                }
        ) {
            AnimatedVisibility(
                visible = visibleIndicators,
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
                    onAdvancement(Advancement(direction = Advancement.Direction.Forward))
                }
        ) {
            AnimatedVisibility(
                visible = visibleIndicators,
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
