package dev.bnorm.storyboard.text.highlight

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle

interface Highlighter {
    val highlighting: Highlighting

    fun highlight(
        text: String,
        identifierStyle: (String) -> SpanStyle? = { null },
    ): AnnotatedString
}

val LocalHighlighter = staticCompositionLocalOf<Highlighter> {
    error("Highlighter is not provided")
}

class CacheableHighlighter(
    private val delegate: Highlighter,
) : Highlighter {
    // TODO caching policy of some kind?
    private val cache = mutableMapOf<String, AnnotatedString>()

    override val highlighting: Highlighting
        get() = delegate.highlighting

    override fun highlight(text: String, identifierStyle: (String) -> SpanStyle?): AnnotatedString {
        return cache.getOrPut(text) { delegate.highlight(text, identifierStyle) }
    }
}

class KotlinHighlighter(
    override val highlighting: Highlighting,
) : Highlighter {
    override fun highlight(
        text: String,
        identifierStyle: (String) -> SpanStyle?,
    ): AnnotatedString {
        return highlightKotlin(text, highlighting, identifierStyle)
    }
}
