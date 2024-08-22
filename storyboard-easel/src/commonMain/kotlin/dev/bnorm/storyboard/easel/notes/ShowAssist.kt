package dev.bnorm.storyboard.easel.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.storyboard.core.AdvanceDirection
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.ui.PreviewSlide
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@Composable
fun StoryboardNotes(storyboard: Storyboard, notes: StoryboardNotes, modifier: Modifier = Modifier) {
    Column(modifier.padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            StoryboardClock()
        }

        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            val frames = remember(storyboard) { storyboard.frames }
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Current Slide")
                ClickablePreviewSlide(storyboard, storyboard.currentFrame)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Next Slide")
                val nextFrame = run {
                    val searchIndex = frames.binarySearch(storyboard.currentFrame)
                    val currentIndex = when {
                        searchIndex >= 0 -> searchIndex
                        else -> when (storyboard.direction) {
                            AdvanceDirection.Forward -> -(searchIndex - 1)
                            AdvanceDirection.Backward -> -searchIndex
                        }

                    }
                    val nextIndex = currentIndex + 1
                    if (nextIndex in frames.indices) frames[nextIndex] else null
                }
                if (nextFrame != null) {
                    ClickablePreviewSlide(storyboard, nextFrame)
                }
            }
        }

        // TODO how can we do a preview of the next slide?
        //  - we would really like to have a preview of the next **advancement**
        //  - can we render the show like an export to create the previews without animations?

        if (notes.tabs.isNotEmpty()) {
            var state by remember { mutableStateOf(0) }
            Scaffold(
                topBar = {
                    TabRow(selectedTabIndex = state) {
                        notes.tabs.forEachIndexed { index, tab ->
                            Tab(
                                text = { Text(tab.title) },
                                selected = state == index,
                                onClick = { state = index }
                            )
                        }
                    }
                },
            ) {
                val tab = notes.tabs[state]
                tab.content()
            }
        }
    }
}

@Composable
private fun ClickablePreviewSlide(
    storyboard: Storyboard,
    frame: Storyboard.Frame,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(storyboard.size.width / storyboard.size.height),
    ) {
        PreviewSlide(
            storyboard = storyboard,
            frame = frame,
        )

        // Cover the slide content with a clickable modifier
        // to disable interaction while in overview.
        Box(
            modifier = Modifier.fillMaxSize()
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable(
                    interactionSource = null, indication = null, // disable ripple effect
                    onClick = { storyboard.jumpTo(frame) }
                )
        )
    }
}

@Composable
fun StoryboardClock(timeSource: TimeSource = TimeSource.Monotonic) {
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
