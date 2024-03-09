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
    CompositionLocalProvider(LocalShowTheme provides theme) {
        MaterialTheme(colors = theme.colors, typography = theme.typography) {
            content()
        }
    }
}

val LocalShowTheme = staticCompositionLocalOf<ShowTheme> {
    error("ShowTheme is not provided")
}

@Immutable
data class ShowTheme(
    val colors: Colors,
    val typography: Typography,
    val code: CodeStyle
) {
    @Immutable
    data class CodeStyle(
        val simple: SpanStyle,
        val number: SpanStyle,
        val keyword: SpanStyle,
        val punctuation: SpanStyle,
        val annotation: SpanStyle,
        val comment: SpanStyle,
        val string: SpanStyle,
    )
}
