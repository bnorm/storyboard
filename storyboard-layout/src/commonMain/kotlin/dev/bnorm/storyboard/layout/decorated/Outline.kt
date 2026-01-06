package dev.bnorm.storyboard.layout.decorated

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

fun Outline(
    key: Any,
    color: Color,
    stroke: Stroke,
    modifier: Modifier = Modifier,
): Decoration {
    return Decoration(modifier) { layout ->
        val rect = layout.getBoundingBox(key) ?: return@Decoration
        drawRect(color, rect.topLeft, rect.size, style = stroke)
    }
}

fun Outline(
    key: Any,
    brush: Brush,
    stroke: Stroke,
    modifier: Modifier = Modifier,
): Decoration {
    return Decoration(modifier) { layout ->
        val rect = layout.getBoundingBox(key) ?: return@Decoration
        drawRect(brush, rect.topLeft, rect.size, style = stroke)
    }
}
