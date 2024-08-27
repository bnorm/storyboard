package dev.bnorm.storyboard.text.highlight

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle

enum class Language {
    Kotlin,
}

fun String.highlight(
    highlighting: Highlighting,
    language: Language,
    identifierStyle: (String) -> SpanStyle? = { null },
): AnnotatedString {
    when (language) {
        Language.Kotlin -> return highlightKotlin(this, highlighting, identifierStyle)
    }
}
