package dev.bnorm.librettist.text

import de.cketti.codepoints.deluxe.appendCodePoint
import de.cketti.codepoints.deluxe.codePointIterator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun String.flowMiddleOut(charDelay: Duration = 50.milliseconds): Flow<String> {
    val codePoints = Iterable { codePointIterator() }.toList()
    return flow {
        var leftIndex = (codePoints.size - 1) / 2
        var rightIndex = codePoints.size / 2

        emit("")
        while (leftIndex >= 0) {
            delay(charDelay)
            emit(buildString {
                for (i in leftIndex..rightIndex) {
                    appendCodePoint(codePoints[i])
                }
            })

            leftIndex--
            rightIndex++
        }
    }
}
