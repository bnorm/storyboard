package dev.bnorm.storyboard.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.DpSize

@Composable
fun FixedSize(
    size: DpSize,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    // TODO i think there is a bug in the scale code of Compose...
    //   - until the desktop window is resized, clickable regions for buttons are incorrectly placed.
    //   - this is reproducible with a BoxWithConstraints and scale modifier as well.
    //   - going to the overview and back seems to fix it.
    Box(modifier = modifier.layout { measurable, constraints ->
        val scale = minOf(
            constraints.maxWidth / size.width.toPx(),
            constraints.maxHeight / size.height.toPx(),
        )

        val fixedConstraints = Constraints.fixed(size.width.roundToPx(), size.height.roundToPx())
        val placeable = measurable.measure(fixedConstraints)
        layout(placeable.width, placeable.height) {
            placeable.placeWithLayer(0, 0) {
                scaleX = scale
                scaleY = scale
            }
        }
    }) {
        content()
    }
}
