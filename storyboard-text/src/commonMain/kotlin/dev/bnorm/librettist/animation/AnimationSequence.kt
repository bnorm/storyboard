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

fun <T> AnimationSequence<T>.then(next: T): AnimationSequence<T> {
    val sequence = sequence { yieldAll(sequence); yield(next) }
    return copy(end = next, sequence = sequence)
}

fun <T> AnimationSequence<T>.then(next: AnimationSequence<T>): AnimationSequence<T> {
    val sequence = sequence { yieldAll(sequence); yieldAll(next.sequence) }
    return copy(end = next.end, sequence = sequence)
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
