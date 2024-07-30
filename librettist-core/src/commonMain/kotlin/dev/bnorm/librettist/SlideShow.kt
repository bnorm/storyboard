package dev.bnorm.librettist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.bnorm.librettist.show.ShowState

val DEFAULT_SLIDE_SIZE = DpSize(1920.dp, 1080.dp)

@Composable
fun SlideShowDisplay(
    showState: ShowState,
    slideSize: DpSize,
    modifier: Modifier = Modifier,
) {
    ScaledBox(
        targetSize = slideSize,
        modifier = modifier
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            showState.Present()
        }
    }
}

@Composable
fun ScaledBox(
    targetSize: DpSize,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.layout { measurable, constraints ->
            val fixedSize = Constraints.fixed(targetSize.width.roundToPx(), targetSize.height.roundToPx())
            val scale = minOf(
                constraints.maxWidth.toDp() / targetSize.width,
                constraints.maxHeight.toDp() / targetSize.height,
            )

            val placeable = measurable.measure(fixedSize)
            layout(placeable.width, placeable.height) {
                placeable.placeWithLayer(0, 0, layerBlock = {
                    scaleX = scale
                    scaleY = scale
                })
            }
        }) {
            content()
        }
    }
}
