package dev.bnorm.librettist.animation

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class AnimationSequence<T>(
    val start: T,
    val end: T,
    val sequence: Sequence<T>,
) {
    fun toList(): ImmutableList<T> {
        return sequence.deduplicate().toImmutableList()
    }
}

fun <T> startAnimation(start: T): AnimationSequence<T> {
    return AnimationSequence(start, start, sequenceOf(start))
}

fun <T> Sequence<T>.deduplicate(): Sequence<T> {
    val upstream = this
    return sequence {
        var prev: Any? = Any()
        for (it in upstream) {
            if (it != prev) {
                yield(it)
            }
            prev = it
        }
    }
}
