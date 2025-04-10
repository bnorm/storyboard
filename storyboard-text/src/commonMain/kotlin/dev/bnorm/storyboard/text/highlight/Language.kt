package dev.bnorm.storyboard.text.highlight

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle

enum class Language {
    Kotlin,
}

fun String.highlight(
    codeStyle: CodeStyle,
    language: Language,
    scope: CodeScope = CodeScope.File,
    identifierStyle: (String) -> SpanStyle? = { null },
): AnnotatedString {
    when (language) {
        Language.Kotlin -> return highlightKotlin(this, codeStyle, scope, identifierStyle)
    }
}
