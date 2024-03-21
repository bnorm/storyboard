package dev.bnorm.librettist.text

import de.cketti.codepoints.deluxe.appendCodePoint
import de.cketti.codepoints.deluxe.codePointIterator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun String.flowMiddleOut(): Sequence<String> {
    val codePoints = Iterable { codePointIterator() }.toList()
    return sequence {
        var leftIndex = (codePoints.size - 1) / 2
        var rightIndex = codePoints.size / 2

        yield("")
        while (leftIndex >= 0) {
            yield(buildString {
                for (i in leftIndex..rightIndex) {
                    appendCodePoint(codePoints[i])
                }
            })

            leftIndex--
            rightIndex++
        }
    }
}
