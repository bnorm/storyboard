package dev.bnorm.storyboard.easel.internal

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize

@Composable
internal fun FixedSize(
    size: IntSize,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val measurePolicy = remember(size) { FixedSizeMeasurePolicy(size) }
    Layout(
        modifier = modifier,
        content = { Box { content() } },
        measurePolicy = measurePolicy,
    )
}

internal class FixedSizeMeasurePolicy(private val size: IntSize) : MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints,
    ): MeasureResult {
        // TODO we need a code path for width and/or height being infinite?
        val constantWidth = size.width
        val constantHeight = size.height
        val maxWidth = constraints.maxWidth
        val maxHeight = constraints.maxHeight

        val widthScale = maxWidth.toFloat() / constantWidth
        val heightScale = maxHeight.toFloat() / constantHeight

        val placeableScale: Float
        val layoutWidth: Int
        val layoutHeight: Int
        if (widthScale < heightScale) {
            placeableScale = widthScale
            layoutWidth = maxWidth
            layoutHeight = maxWidth * constantHeight / constantWidth
        } else {
            placeableScale = heightScale
            layoutWidth = maxHeight * constantWidth / constantHeight
            layoutHeight = maxHeight
        }

        val fixedConstraints = Constraints.fixed(size.width, size.height)
        val placeable = measurables[0].measure(fixedConstraints)
        return layout(layoutWidth, layoutHeight) {
            // Compensate for the placeable being scaled.
            val xDrift = (layoutWidth - constantWidth) / 2
            val yDrift = (layoutHeight - constantHeight) / 2

            // Place and scale the placeable
            placeable.placeWithLayer(xDrift, yDrift) {
                scaleX = placeableScale
                scaleY = placeableScale
            }
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int,
    ): Int {
        val constantWidth = size.width
        val constantHeight = size.height
        return height * constantWidth / constantHeight
    }

    override fun IntrinsicMeasureScope.minIntrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int,
    ): Int {
        val constantWidth = size.width
        val constantHeight = size.height
        return width * constantHeight / constantWidth
    }

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int,
    ): Int {
        return 0
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int,
    ): Int {
        return 0
    }
}
