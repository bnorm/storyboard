package dev.bnorm.librettist.show

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

fun Modifier.onShowNavigation(showState: ShowState): Modifier {
    var keyHeld = false
    return onPreviewKeyEvent { event ->
        // TODO rate-limit holding down the key?
        when (event.type) {
            KeyEventType.KeyDown -> {
                val wasHeld = keyHeld
                keyHeld = true

                when (event.key) {
                    Key.DirectionRight,
                    Key.DirectionDown,
                    Key.Enter,
                    Key.Spacebar,
                        -> return@onPreviewKeyEvent showState.advance(AdvanceDirection.Forward, jump = wasHeld)

                    Key.DirectionLeft,
                    Key.DirectionUp,
                    Key.Backspace,
                        -> return@onPreviewKeyEvent showState.advance(AdvanceDirection.Backward, jump = wasHeld)
                }
            }

            KeyEventType.KeyUp -> {
                keyHeld = false
            }
        }

        return@onPreviewKeyEvent false
    }
}

fun Modifier.onOverviewNavigation(showState: ShowState): Modifier {
    return onPreviewKeyEvent { event ->
        val currentSlide = showState.currentIndex
        val indexes = showState.slides.toIndexes().groupBy { it.index }.entries.toList()

        if (event.type == KeyEventType.KeyDown) {
            val index = when (event.key) {
                Key.DirectionRight -> {
                    val i = indexes.binarySearch { compareValues(it.key, currentSlide.index) }
                    val next = indexes[(i + 1).coerceIn(indexes.indices)].key
                    Slide.Index(next, 0)
                }

                Key.DirectionLeft -> {
                    val i = indexes.binarySearch { compareValues(it.key, currentSlide.index) }
                    val next = indexes[(i - 1).coerceIn(indexes.indices)].key
                    Slide.Index(next, 0)
                }

                Key.DirectionDown -> {
                    val state = currentSlide.state
                    val next = (state + 1)
                    Slide.Index(currentSlide.index, next)
                }

                Key.DirectionUp -> {
                    val state = currentSlide.state
                    val next = (state - 1)
                    Slide.Index(currentSlide.index, next)
                }

                else -> return@onPreviewKeyEvent false
            }

            showState.jumpToSlide(index)
            return@onPreviewKeyEvent true
        }

        return@onPreviewKeyEvent false
    }
}

@Composable
fun MouseNavigationIndicators(showState: ShowState) {
    var first by remember { mutableStateOf(true) }
    var visibleIndicators by remember { mutableStateOf(true) }

    LaunchedEffect(showState.currentIndex) {
        if (first) {
            first = false
            return@LaunchedEffect
        }

        visibleIndicators = false
        delay(10.seconds)
        visibleIndicators = true
    }

    Row(modifier = Modifier.fillMaxSize().clip(RectangleShape)) {
        Box(
            modifier = Modifier.fillMaxHeight().weight(20f)
                .clickable(interactionSource = null, indication = null) {
                    showState.advance(AdvanceDirection.Backward)
                }
        ) {
            this@Row.AnimatedVisibility(
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

        Spacer(Modifier.weight(60f))

        Box(
            modifier = Modifier.fillMaxHeight().weight(20f)
                .clickable(interactionSource = null, indication = null) {
                    showState.advance(AdvanceDirection.Forward)
                }
        ) {
            this@Row.AnimatedVisibility(
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