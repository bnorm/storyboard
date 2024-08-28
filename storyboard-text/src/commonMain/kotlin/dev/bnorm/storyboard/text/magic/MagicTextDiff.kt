package dev.bnorm.storyboard.text.magic

import androidx.compose.ui.text.AnnotatedString
import io.github.petertrr.diffutils.algorithm.myers.MyersDiff
import io.github.petertrr.diffutils.diff
import io.github.petertrr.diffutils.patch.ChangeDelta
import io.github.petertrr.diffutils.patch.DeleteDelta
import io.github.petertrr.diffutils.patch.EqualDelta
import io.github.petertrr.diffutils.patch.InsertDelta

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

private val EMPTY_ANNOTATED_STRING = AnnotatedString("")

// Calculate diff of tokenized AnnotatedStrings.
internal fun diff(before: List<AnnotatedString>, after: List<AnnotatedString>): List<MagicTextDiff> {
    var index = 1
    fun nextKey() = "stable:${index++}"

    return buildList {

        // Calculate diff based on just the text value of the string tokens.
        val deltas = diff(
            source = before.map { it.text },
            target = after.map { it.text },
            algorithm = MyersDiff(),
            includeEqualParts = true,
        ).deltas

        // Reconstitute the diff using the annotated strings.
        deltas.forEach { delta ->
            val beforeDiff = List(delta.source.lines.size) { i -> before[delta.source.position + i] }
            val afterDiff = List(delta.target.lines.size) { i -> after[delta.target.position + i] }

            fun delete() {
                for (element in beforeDiff) {
                    add(MagicTextDiff(before = element, after = EMPTY_ANNOTATED_STRING))
                }
            }

            fun insert() {
                for (element in afterDiff) {
                    add(MagicTextDiff(before = EMPTY_ANNOTATED_STRING, after = element))
                }
            }

            when (delta) {
                is DeleteDelta<*> -> {
                    require(afterDiff.isEmpty())
                    delete()
                }

                is InsertDelta<*> -> {
                    require(beforeDiff.isEmpty())
                    insert()
                }

                is ChangeDelta<*> -> {
                    // Treat a change like a delete and insert since there isn't any difference.
                    delete()
                    insert()
                }

                is EqualDelta<*> -> {
                    require(beforeDiff.size == afterDiff.size)
                    for (i in beforeDiff.indices) {
                        add(MagicTextDiff(before = beforeDiff[i], after = afterDiff[i], key = nextKey()))
                    }
                }
            }
        }
    }
}
