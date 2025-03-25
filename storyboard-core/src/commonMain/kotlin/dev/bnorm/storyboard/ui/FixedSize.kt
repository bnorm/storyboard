package dev.bnorm.storyboard.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize

@Composable
fun FixedSize(
    size: DpSize,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = contentAlignment,
    ) {
        val currentDensity = LocalDensity.current
        val scale = with(currentDensity) {
            minOf(
                constraints.maxWidth / size.width.toPx(),
                constraints.maxHeight / size.height.toPx(),
            )
        }

        CompositionLocalProvider(LocalDensity provides Density(currentDensity.density * scale)) {
            Box(modifier = Modifier.requiredSize(size)) {
                content()
            }
        }
    }
}
