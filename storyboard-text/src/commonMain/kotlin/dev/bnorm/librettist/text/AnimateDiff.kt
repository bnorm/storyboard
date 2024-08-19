package dev.bnorm.librettist.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import dev.bnorm.librettist.animation.AnimationSequence
import org.apache.commons.text.diff.AnnotatedStringsComparator
import org.apache.commons.text.diff.ReplacementsFinder
import org.apache.commons.text.diff.StringsComparator

private data class Diff(
    val skipped: Int,
    val from: List<Char>,
    val to: List<Char>,
)

private data class AnnotatedDiff(
    val skipped: Int,
    val from: List<AnnotatedString>,
    val to: List<AnnotatedString>,
)

// TODO: diff based on words?
// TODO: write our own diff algorithm? (work is all public...) base it on code points instead! YES!
// TODO: how to handle code points? https://github.com/cketti/kotlin-codepoints

fun String.flowDiff(other: String): Sequence<String> {
    val diffs = buildList {
        StringsComparator(this@flowDiff, other).script.visit(ReplacementsFinder { skipped, from, to ->
            add(Diff(skipped, from.toList(), to.toList()))
        })
    }

    return sequence {
        var value = this@flowDiff
        yield(value)

        var offset = 0
        for ((skipped, from, to) in diffs) {
            offset += skipped
            val start = value.substring(0, offset)
            val end = value.substring(offset + from.size)

            for (i in from.indices.reversed()) {
                if (from[i].isWhitespace()) continue

                value = buildString {
                    append(start)
                    for (j in 0..<i) {
                        append(from[j])
                    }
                    append(end)
                }
                yield(value)
            }

            for (i in to.indices) {
                if (to[i].isWhitespace()) continue

                value = buildString {
                    append(start)
                    for (j in 0..i) {
                        append(to[j])
                    }
                    append(end)
                }
                offset++
                yield(value)
            }
        }
    }
}


fun AnnotatedString.flowDiff(other: AnnotatedString): Sequence<AnnotatedString> {
    val diffs = buildList {
        AnnotatedStringsComparator(this@flowDiff, other).script.visit(ReplacementsFinder { skipped, from, to ->
            add(AnnotatedDiff(skipped, from.toList(), to.toList()))
        })
    }

    return sequence {
        var value = this@flowDiff
        yield(value)

        var offset = 0
        for ((skipped, from, to) in diffs) {
            offset += skipped
            val start = value.subSequence(0, offset)
            val end = value.subSequence(offset + from.size, value.length)

            for (i in from.indices.reversed()) {
                if (from[i].text.isBlank()) continue

                value = buildAnnotatedString {
                    append(start)
                    for (j in 0..<i) {
                        append(from[j])
                    }
                    append(end)
                }
                yield(value)
            }

            for (i in to.indices) {
                if (to[i].text.isBlank()) continue

                value = buildAnnotatedString {
                    append(start)
                    for (j in 0..i) {
                        append(to[j])
                    }
                    append(end)
                }
                offset++
                yield(value)
            }
        }
    }
}

fun AnimationSequence<String>.thenDiff(next: String): AnimationSequence<String> {
    val nextSequence = end.flowDiff(next)
    val sequence = sequence { yieldAll(sequence); yieldAll(nextSequence) }
    return copy(end = next, sequence = sequence)
}


fun AnimationSequence<AnnotatedString>.thenDiff(next: AnnotatedString): AnimationSequence<AnnotatedString> {
    val nextSequence = end.flowDiff(next)
    val sequence = sequence { yieldAll(sequence); yieldAll(nextSequence) }
    return copy(end = next, sequence = sequence)
}
