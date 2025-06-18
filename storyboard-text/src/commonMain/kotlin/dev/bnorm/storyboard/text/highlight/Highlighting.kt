package dev.bnorm.storyboard.text.highlight

import androidx.compose.ui.text.AnnotatedString

interface Highlighting {
    fun style(text: String): AnnotatedString
}

fun String.style(
    highlighting: Highlighting,
): AnnotatedString = highlighting.style(this)
