package dev.bnorm.librettist.text

import de.cketti.codepoints.deluxe.appendCodePoint
import de.cketti.codepoints.deluxe.codePointIterator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun String.flowStart(charDelay: Duration = 50.milliseconds): Flow<String> = flow {
    val value = StringBuilder()
    emit(value.toString())

    for (codePoint in this@flowStart.codePointIterator()) {
        value.appendCodePoint(codePoint)
        if (!codePoint.isWhitespace()) {
            delay(charDelay)
            emit(value.toString())
        }
    }

    emit(value.toString())
}
