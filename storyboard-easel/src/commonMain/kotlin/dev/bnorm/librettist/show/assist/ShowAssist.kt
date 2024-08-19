package dev.bnorm.librettist.show.assist

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.ui.PreviewSlide
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@Composable
fun ShowAssist(storyboard: Storyboard, showAssistState: ShowAssistState, modifier: Modifier = Modifier) {
    Column(modifier) {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            PresentationClock()
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            val frames = remember(storyboard) { storyboard.frames }
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Current Slide")
                PreviewSlide(storyboard.currentFrame, storyboard)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Next Slide")
                val frame = run {
                    val nextIndex = frames.binarySearch(storyboard.currentFrame) + 1
                    if (nextIndex in frames.indices) frames[nextIndex] else null
                }
                if (frame != null) {
                    PreviewSlide(frame, storyboard)
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

@Composable
private fun PreviewSlide(
    frame: Storyboard.Frame,
    storyboard: Storyboard,
    modifier: Modifier = Modifier,
) {
    PreviewSlide(
        slide = storyboard.slides[frame.slideIndex],
        index = frame.stateIndex,
        size = storyboard.size,
        decorator = storyboard.decorator,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(storyboard.size.width / storyboard.size.height),
    )
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
