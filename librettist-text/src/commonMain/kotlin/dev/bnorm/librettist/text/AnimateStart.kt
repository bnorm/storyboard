package dev.bnorm.librettist.text

import de.cketti.codepoints.deluxe.appendCodePoint
import de.cketti.codepoints.deluxe.codePointIterator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun String.flowStart(): Sequence<String> = sequence {
    val value = StringBuilder()
    yield(value.toString())

    for (codePoint in this@flowStart.codePointIterator()) {
        value.appendCodePoint(codePoint)
        if (!codePoint.isWhitespace()) {
            yield(value.toString())
        }
    }

    yield(value.toString())
}
