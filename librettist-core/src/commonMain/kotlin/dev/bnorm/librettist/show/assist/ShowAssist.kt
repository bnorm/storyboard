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
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@Composable
fun ShowAssist(showAssistState: ShowAssistState) {
    MaterialTheme {
        Column {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                PresentationClock()
            }

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
fun PresentationClock(timeSource: TimeSource = TimeSource.Monotonic) {
    var start by remember { mutableStateOf<TimeMark?>(null) }
    var display by remember { mutableStateOf("00h 00m 00s") }

    fun Long.pad(): String = toString().padStart(2, padChar = '0')

    LaunchedEffect(start) {
        val mark = start ?: return@LaunchedEffect
        while (true) {
            delay(250.milliseconds)
            val d = mark.elapsedNow()
            display = "${d.inWholeHours.pad()}h ${d.inWholeMinutes.pad()}m ${d.inWholeSeconds.pad()}s"
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