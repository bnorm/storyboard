package dev.bnorm.librettist.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import org.apache.commons.text.diff.ListComparator
import org.apache.commons.text.diff.ReplacementsFinder

data class MagicTextDiff(
    val before: AnnotatedString,
    val after: AnnotatedString,
    val key: Any? = null, // Key used within SharedTransitionScope; must be unique within a list of diffs!
) {
    init {
        // If the key is non-null, the before and after text must be equal. (style may be different)
        require(key == null || before.text == after.text) { "'$before' != '$after'" }
    }
}

// Tokenize an AnnotatedString into a list of words.
fun AnnotatedString.toWords(): List<AnnotatedString> {
    return buildList {
        var offset = 0
        for (i in this@toWords.indices) {
            val char = this@toWords[i]
            if (char.isLetterOrDigit() || char == '_') {
                continue
            } else {
                if (i > offset) add(subSequence(offset, i))
                add(subSequence(i, i + 1))
                offset = i + 1
            }
        }
        if (offset < length) add(subSequence(offset, length))
    }
}

// Calculate diff of tokenized AnnotatedStrings.
fun diff(before: List<AnnotatedString>, after: List<AnnotatedString>): List<MagicTextDiff> {
    var index = 1
    fun nextKey() = "stable:${index++}"

    var beforeOffset = 0
    var afterOffset = 0
    return buildList {
        ListComparator(before.map { it.text }, after.map { it.text }).script.visit(
            ReplacementsFinder { skipped, from, to ->
                if (skipped > 0) {
                    add(
                        MagicTextDiff(
                            before = before.merge(beforeOffset, beforeOffset + skipped),
                            after = after.merge(afterOffset, afterOffset + skipped),
                            key = nextKey()
                        )
                    )
                    beforeOffset += skipped
                    afterOffset += skipped
                }

                add(
                    MagicTextDiff(
                        before = before.merge(beforeOffset, beforeOffset + from.size),
                        after = after.merge(afterOffset, afterOffset + to.size),
                        key = null,
                    )
                )
                beforeOffset += from.size
                afterOffset += to.size
            }
        )

        if (beforeOffset < before.size) {
            add(
                MagicTextDiff(
                    before = before.merge(beforeOffset),
                    after = after.merge(afterOffset),
                    key = nextKey()
                )
            )
        }
    }
}

private fun List<AnnotatedString>.merge(
    fromIndex: Int = 0,
    toIndex: Int = size,
): AnnotatedString = buildAnnotatedString {
    for (str in subList(fromIndex, toIndex)) {
        append(str)
    }
}
