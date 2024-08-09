package dev.bnorm.librettist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.bnorm.librettist.show.AdvanceDirection
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.ShowState
import dev.bnorm.librettist.show.toIndexes
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun EmbeddedSlideShow(
    theme: ShowTheme,
    slideSize: DpSize = DEFAULT_SLIDE_SIZE,
    showIndicators: Boolean = true,
    startSlide: Int = 0,
    modifier: Modifier = Modifier,
    builder: ShowBuilder.() -> Unit,
) {
    val showState = remember(builder, startSlide) {
        ShowState(builder).also { state ->
            val indices = state.slides.toIndexes()
            state.jumpToSlide(indices[startSlide.coerceIn(0, indices.size - 1)])
        }
    }

    ShowTheme(theme) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .focusRequester(rememberFocusRequester()).focusTarget()
                .onShowNavigation(showState)
        ) {
            SlideShowDisplay(
                showState = showState,
                slideSize = slideSize,
                modifier = Modifier.fillMaxSize()
            )

            if (showIndicators) {
                MouseNavigationIndicators(showState)
            }
        }
    }
}

@Composable
private fun MouseNavigationIndicators(showState: ShowState) {
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
