package dev.bnorm.librettist

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.*
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
)

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
) {
    companion object {
        val current: Highlighting
            @Composable
            get() = LocalHighlighting.current
    }
}

private val LocalHighlighting = staticCompositionLocalOf<Highlighting> {
    // TODO provide a default highlighting
    //  compatible with default material theme?
    error("Highlighting is not provided")
}

private val LocalHighlightedCache = staticCompositionLocalOf<MutableMap<Any, Any?>> {
    error("HighlightedCache is not provided")
}

@Composable
fun Highlighting(highlighting: Highlighting, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalHighlighting provides highlighting) {
        CompositionLocalProvider(LocalHighlightedCache provides mutableMapOf()) {
            content()
        }
    }
}

/**
 * Caches the value created by [content] using [key].
 * [key] must be globally unique or multiple remembered values will conflict.
 * Values are associated with the current [highlighting][Highlighting], and will be recomputed if highlighting changes.
 *
 * The result is also [remembered][remember] based on the current highlighting and [key].
 */
@Composable
fun <T> rememberHighlighted(key: Any, content: (Highlighting) -> T): T {
    val highlighting = LocalHighlighting.current
    val cache = LocalHighlightedCache.current
    return remember(highlighting, key) {
        @Suppress("UNCHECKED_CAST")
        cache.getOrPut(key) { content(highlighting) } as T
    }
}
