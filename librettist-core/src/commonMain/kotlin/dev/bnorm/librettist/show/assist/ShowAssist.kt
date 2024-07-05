package dev.bnorm.librettist.show.assist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.librettist.ScaledBox
import dev.bnorm.librettist.ShowTheme
import dev.bnorm.librettist.show.*
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@Composable
fun ShowAssist(slideSize: DpSize, theme: ShowTheme, showState: ShowState, showAssistState: ShowAssistState) {
    MaterialTheme {
        Column {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                PresentationClock()
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                val indices = remember(showState) { showState.slides.toIndexes() }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Current Slide")
                    PreviewSlide(showState.index, showState, theme, slideSize)
                }
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Next Slide")
                    val index = indices.firstOrNull { it > showState.index }
                    if (index != null) {
                        PreviewSlide(index, showState, theme, slideSize)
                    }
                }
            }

            // TODO how can we do a preview of the next slide?
            //  - we would really like to have a preview of the next **advancement**
            //  - can we render the show like an export to create the previews without animations?

            if (showAssistState.tabs.isNotEmpty()) {
                var state by remember { mutableStateOf(0) }
                Scaffold(
                    topBar = {
                        TabRow(selectedTabIndex = state) {
                            showAssistState.tabs.forEachIndexed { index, tab ->
                                Tab(
                                    text = { Text(tab.name) },
                                    selected = state == index,
                                    onClick = { state = index }
                                )
                            }
                        }
                    },
                ) {
                    val tab = showAssistState.tabs[state]
                    tab.content()
                }
            }
        }
    }
}

@Composable
private fun PreviewSlide(
    index: Slide.Index,
    showState: ShowState,
    theme: ShowTheme,
    slideSize: DpSize,
    modifier: Modifier = Modifier,
) {
    val slide = showState.getSlide(index)
    if (slide != null) {
        PreviewSlide(theme, slideSize, modifier) {
            SharedTransitionLayout {
                AnimatedContent(Unit) {
                    SlideScope(
                        SlideState.Index(index.state),
                        this@AnimatedContent,
                        this@SharedTransitionLayout
                    ).slide()
                }
            }
        }
    }
}

@Composable
private fun PreviewSlide(
    theme: ShowTheme,
    slideSize: DpSize,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier) {
        CompositionLocalProvider(LocalShowAssistState provides null) {
            ShowTheme(theme) {
                ScaledBox(
                    targetSize = slideSize,
                    modifier = Modifier.fillMaxWidth()
                        .aspectRatio(slideSize.width / slideSize.height)
                        .background(MaterialTheme.colors.background)
                ) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        content()
                    }
                }
            }
        }
    }
}

@Composable
fun PresentationClock(timeSource: TimeSource = TimeSource.Monotonic) {
    var start by remember { mutableStateOf<TimeMark?>(null) }
    var display by remember { mutableStateOf("00h 00m 00s") }

    fun Long.pad(): String = toString().padStart(2, padChar = '0')

    LaunchedEffect(start) {
        val mark = start ?: return@LaunchedEffect
        while (true) {
            delay(250.milliseconds)
            val d = mark.elapsedNow()
            display = "${d.inWholeHours.pad()}h ${(d.inWholeMinutes % 60).pad()}m ${(d.inWholeSeconds % 60).pad()}s"
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(display, fontSize = 32.sp, fontFamily = FontFamily.Monospace)

        IconButton(
            onClick = {
                start = timeSource.markNow()
                display = "00h 00m 00s"
            },
            modifier = Modifier
                .padding(start = 16.dp)
                .background(MaterialTheme.colors.primary, shape = CircleShape)
        ) {
            Icon(Icons.Filled.PlayArrow, tint = MaterialTheme.colors.onPrimary, contentDescription = "")
        }
    }
}
