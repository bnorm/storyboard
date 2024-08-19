package dev.bnorm.librettist.text

import androidx.compose.ui.text.AnnotatedString

fun AnnotatedString.annotatedLines(): List<AnnotatedString> {
    val delimiters = listOf("\r\n", "\n", "\r")
    return buildList {
        var previousIndex = 0
        var index = text.findAnyOf(delimiters)?.first ?: -1
        while (index != -1) {
            add(subSequence(previousIndex, index))
            previousIndex = index + 1
            index = text.findAnyOf(delimiters, previousIndex)?.first ?: -1
        }
        add(subSequence(previousIndex, length))
    }
}
