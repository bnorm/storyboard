package dev.bnorm.storyboard.text.magic

import androidx.compose.ui.text.AnnotatedString

// Tokenize an AnnotatedString into a list of words.
fun AnnotatedString.toWords(): List<AnnotatedString> {
    return buildList {
        var last = 0
        while (last < length) {
            val (i, word) = text.findAnyOf(SYMBOL_WORDS, startIndex = last) ?: break
            if (i > last) addAll(subSequence(last, i).split())
            add(subSequence(i, i + word.length))
            last = i + word.length
        }
        if (last < length) addAll(subSequence(last, length).split())
    }
}

private val SYMBOL_WORDS = setOf(
    "&&",
    "||",
    "==",
    "!=",
    "===",
    "!==",
    ">=",
    "<=",
    "!!",
    "?:",
    "::",
    "->",
    "\"\"\"",
)

private fun AnnotatedString.split(): List<AnnotatedString> {
    return buildList {
        var offset = 0
        for (i in this@split.indices) {
            val char = this@split[i]
            if (char.isLetterOrDigit() || char == '_') {
                continue
            } else {
                if (i > offset) add(this@split.subSequence(offset, i))
                add(this@split.subSequence(i, i + 1))
                offset = i + 1
            }
        }
        if (offset < this@split.length) add(this@split.subSequence(offset, this@split.length))
    }
}
