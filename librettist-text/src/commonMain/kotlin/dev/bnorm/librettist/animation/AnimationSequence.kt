package dev.bnorm.librettist.animation

// TODO hook into SeekableTransitionState and KeyframesSpec?
data class AnimationSequence<T>(
    val start: T,
    val end: T,
    val sequence: Sequence<T>,
)

fun <T> startAnimation(start: T): AnimationSequence<T> {
    return AnimationSequence(start, start, sequenceOf(start))
}
