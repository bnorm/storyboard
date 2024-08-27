package dev.bnorm.storyboard.text.magic

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
        fun add(
            beforeSize: Int,
            afterSize: Int,
            keyed: Boolean,
        ) {
            val minSize = minOf(beforeSize, afterSize)
            val maxSize = maxOf(beforeSize, afterSize)
            for (i in 0..<minSize) {
                add(
                    MagicTextDiff(
                        before = before[beforeOffset + i],
                        after = after[afterOffset + i],
                        key = if (keyed) nextKey() else null,
                    )
                )
            }

            if (maxSize > minSize) {
                val empty = AnnotatedString("")
                if (maxSize == beforeSize) {
                    for (i in minSize..<maxSize) {
                        add(
                            MagicTextDiff(
                                before = before[beforeOffset + i],
                                after = empty,
                                key = null,
                            )
                        )
                    }
                } else {
                    for (i in minSize..<maxSize) {
                        add(
                            MagicTextDiff(
                                before = empty,
                                after = after[afterOffset + i],
                                key = null,
                            )
                        )
                    }
                }
            }

            beforeOffset += beforeSize
            afterOffset += afterSize
        }

        ListComparator(before.map { it.text }, after.map { it.text }).script.visit(
            ReplacementsFinder { skipped, from, to ->
                if (skipped > 0) {
                    add(
                        beforeSize = skipped,
                        afterSize = skipped,
                        keyed = true,
                    )
                }

                add(
                    beforeSize = from.size,
                    afterSize = to.size,
                    keyed = false,
                )
            }
        )

        add(
            beforeSize = before.size - beforeOffset,
            afterSize = after.size - afterOffset,
            keyed = true,
        )
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
