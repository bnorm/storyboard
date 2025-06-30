package dev.bnorm.storyboard.layout.decorated

import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultBlendMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.toIntSize
import androidx.compose.ui.unit.toOffset
import kotlin.math.absoluteValue

fun Line(
    startKey: Any,
    startAlignment: Alignment,
    endKey: Any,
    endAlignment: Alignment,
    color: Color,
    stroke: Stroke,
    modifier: Modifier = Modifier,
): Decoration = Decoration(modifier) { layout ->
    val startRect = layout.getBoundingBox(startKey) ?: return@Decoration
    val endRect = layout.getBoundingBox(endKey) ?: return@Decoration

    val startPosition = borderPosition(startRect, startAlignment, layoutDirection)
    val endPosition = borderPosition(endRect, endAlignment, layoutDirection)

    val path = Path().apply {
        moveTo(startPosition.x, startPosition.y)
        lineTo(endPosition.x, endPosition.y)
    }

    drawPath(
        path = path,
        color = color,
        style = stroke,
    )
}

fun CubicLine(
    startKey: Any,
    startAlignment: Alignment,
    endKey: Any,
    endAlignment: Alignment,
    color: Color,
    stroke: Stroke,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DefaultBlendMode,
    modifier: Modifier = Modifier,
): Decoration = Decoration(modifier) { layout ->
    val startRect = layout.getBoundingBox(startKey) ?: return@Decoration
    val endRect = layout.getBoundingBox(endKey) ?: return@Decoration

    val startPosition = borderPosition(startRect, startAlignment, layoutDirection)
    val endPosition = borderPosition(endRect, endAlignment, layoutDirection)

    // TODO what should the projection distance be?
    val bounds = (endPosition - startPosition)
    val distance = when {
        startAlignment is BiasAlignment && endAlignment is BiasAlignment -> when {
            startAlignment.horizontalBias == 0f && endAlignment.horizontalBias == 0f
                -> bounds.y.absoluteValue

            startAlignment.verticalBias == 0f && endAlignment.verticalBias == 0f
                -> bounds.x.absoluteValue

            else -> minOf(bounds.x.absoluteValue, bounds.y.absoluteValue)
        }

        else -> minOf(bounds.x.absoluteValue, bounds.y.absoluteValue)
    }
    val startProjection = startPosition.project(startPosition - startRect.center, distance / 2f)
    val endProjection = endPosition.project(endPosition - endRect.center, distance / 2f)

    val path = Path().apply {
        moveTo(startPosition.x, startPosition.y)
        cubicTo(
            startProjection.x, startProjection.y,
            endProjection.x, endProjection.y,
            endPosition.x, endPosition.y,
        )
    }

    drawPath(
        path = path,
        color = color,
        style = stroke,
        colorFilter = colorFilter,
        blendMode = blendMode,
    )
}

private fun Offset.project(vector: Offset, distance: Float): Offset {
    return this + vector * (distance / vector.getDistance())
}

private fun borderPosition(rect: Rect, alignment: Alignment, layoutDirection: LayoutDirection): Offset {
    val offset = alignment.align(IntSize.Zero, rect.size.toIntSize(), layoutDirection)
    return rect.topLeft + offset.toOffset()
}
