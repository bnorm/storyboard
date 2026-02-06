import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Transition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun <T : Comparable<T>> Transition<T>.RevealAfter(
    index: T,
    highlight: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val derivedModifier = when (highlight) {
        true -> modifier.highlight(this) { it == index }
        false -> modifier
    }

    this.AnimatedVisibility(
        visible = { it >= index },
        enter = fadeIn(), exit = fadeOut(),
        modifier = derivedModifier,
    ) {
        content()
    }
}

@Composable
fun <T> Modifier.highlight(state: Transition<T>, predicate: (T) -> Boolean): Modifier {
    if (!isTeleprompter) return Modifier

    val color = state.animateColor { if (predicate(it)) MaterialTheme.colors.primary else Color.Transparent }
    return drawWithContent {
        drawContent()

        val padding = 8.dp.toPx()
        drawRoundRect(
            color = color.value,
            topLeft = Offset(-padding, -padding),
            size = Size(size.width + padding * 2, size.height + padding * 2),
            cornerRadius = CornerRadius(padding, padding),
            style = Stroke(width = 2.dp.toPx()),
        )
    }
}
