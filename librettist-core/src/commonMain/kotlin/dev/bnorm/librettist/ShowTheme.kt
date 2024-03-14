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

private val LocalShowTheme = staticCompositionLocalOf<ShowTheme> {
    error("ShowTheme is not provided")
}

// TODO reduce to just CodeStyle and let MaterialTheme take care of the rest?
//  This way it could be moved to librettist-text
//  How is the theme provided then? some kind of slide decorator?
@Immutable
data class ShowTheme(
    val colors: Colors,
    val typography: Typography,
    val code: CodeStyle,
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
        val property: SpanStyle,
        val functionDeclaration: SpanStyle,
        val staticFunctionCall: SpanStyle,
        val extensionFunctionCall: SpanStyle,
        val typeParameters: SpanStyle,
    )

    companion object {
        val colors: Colors
            @Composable
            get() = LocalShowTheme.current.colors

        val typography: Typography
            @Composable
            get() = LocalShowTheme.current.typography

        val code: CodeStyle
            @Composable
            get() = LocalShowTheme.current.code
    }
}
