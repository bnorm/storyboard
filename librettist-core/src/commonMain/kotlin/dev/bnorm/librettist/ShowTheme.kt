package dev.bnorm.librettist

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.SpanStyle

@Composable
fun ShowTheme(theme: ShowTheme, content: @Composable () -> Unit) {
    Highlighting(theme.code) {
        MaterialTheme(colors = theme.colors, typography = theme.typography) {
            content()
        }
    }
}

@Immutable
data class ShowTheme(
    val colors: Colors,
    val typography: Typography,
    val code: Highlighting,
) {
    companion object {
        val code: Highlighting
            @Composable
            get() = LocalHighlighting.current
    }
}

@Immutable
data class Highlighting(
    val simple: SpanStyle,
    val number: SpanStyle,
    val keyword: SpanStyle,
    val punctuation: SpanStyle,
    val annotation: SpanStyle,
    val comment: SpanStyle,
    val string: SpanStyle,
    val property: SpanStyle,
    val functionDeclaration: SpanStyle,
    val staticFunctionCall: SpanStyle,
    val extensionFunctionCall: SpanStyle,
    val typeParameters: SpanStyle,
)

val LocalHighlighting = staticCompositionLocalOf<Highlighting> {
    // TODO provide a default highlighting
    //  compatible with default material theme?
    error("Highlighting is not provided")
}

@Composable
fun Highlighting(highlighting: Highlighting, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalHighlighting provides highlighting) {
        content()
    }
}
