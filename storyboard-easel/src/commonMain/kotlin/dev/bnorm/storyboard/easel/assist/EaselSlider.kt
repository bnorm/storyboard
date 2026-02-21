package dev.bnorm.storyboard.easel.assist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.easel.Animatic
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun EaselSlider(animatic: Animatic, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    val storyDistance = animatic.storyDistance
    val storyProgress = animatic.storyProgress
    var sceneSlider by remember { mutableStateOf(false) }
    var range by remember { mutableStateOf(0f..1f) }

    Row(modifier, verticalAlignment = Alignment.Bottom) {
        IconButton(
            onClick = {
                val start = (storyProgress - 2f).coerceAtLeast(0f)
                val end = (storyProgress + 2f).coerceAtMost(storyDistance)
                range = start..end
                sceneSlider = !sceneSlider
            },
            modifier = Modifier
                .padding(end = 16.dp)
                .pointerHoverIcon(PointerIcon.Hand)
                .background(MaterialTheme.colors.primary, shape = CircleShape)
        ) {
            Icon(
                // TODO could be cool to make this an animated vector
                if (sceneSlider) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "",
                modifier = Modifier.size(32.dp)
            )
        }

        Column {
            RangeSlider(
                value = if (!sceneSlider) 0f..storyProgress else range,
                valueRange = 0f..storyDistance,
                onValueChange = { range = it.start..it.endInclusive },
                enabled = sceneSlider,
                colors = SliderDefaults.colors(
                    thumbColor = Color.Red,
                    disabledThumbColor = Color.Transparent,
                    activeTrackColor = Color.Red,
                    disabledActiveTrackColor = Color.Green,
                    inactiveTrackColor = Color.DarkGray,
                    disabledInactiveTrackColor = Color.DarkGray,
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            AnimatedVisibility(visible = sceneSlider) {
                Slider(
                    value = storyProgress,
                    valueRange = range.start.roundToInt().toFloat()..range.endInclusive.roundToInt().toFloat(),
                    onValueChange = {
                        job?.cancel()
                        job = coroutineScope.launch {
                            animatic.seek(it / storyDistance)
                            job = null
                        }
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Green,
                        activeTrackColor = Color.Green,
                        inactiveTrackColor = Color.DarkGray,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
