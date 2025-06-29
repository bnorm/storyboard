package dev.bnorm.storyboard.layout.decorated

import androidx.annotation.FloatRange
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.translate

fun Surround(
    key: Any,
    shape: Shape,
    color: Color,
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, fromInclusive = false) scale: Float = 1.0f,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
    style: DrawStyle = Fill,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DrawScope.DefaultBlendMode,
): Decoration {
    return Decoration(modifier) { layout ->
        val rect = layout.getBoundingBox(key) ?: return@Decoration
        val size = rect.size * scale
        val offsetX = rect.topLeft.x + (rect.size.width - size.width) / 2f
        val offsetY = rect.topLeft.y + (rect.size.height - size.height) / 2f
        translate(offsetX, offsetY) {
            drawOutline(
                outline = shape.createOutline(size, layoutDirection, this),
                color = color,
                alpha = alpha,
                style = style,
                colorFilter = colorFilter,
                blendMode = blendMode,
            )
        }
    }
}
