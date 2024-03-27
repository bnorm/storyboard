package dev.bnorm.librettist.show

sealed class SlideState<out T> {
    // TODO are there better names than Entering and Exiting?
    //  doesn't really make sense when advancing backwards...
    data object Entering : SlideState<Nothing>()
    data object Exiting : SlideState<Nothing>()
    data class Index<T>(val value: T) : SlideState<T>()
}

operator fun <T : Comparable<T>> SlideState<T>.compareTo(other: SlideState<T>): Int {
    return when (this) {
        SlideState.Entering -> if (other == SlideState.Entering) 0 else -1
        SlideState.Exiting ->  if (other == SlideState.Exiting) 0 else 1
        is SlideState.Index -> when (other) {
            SlideState.Entering -> 1
            SlideState.Exiting -> -1
            is SlideState.Index -> value.compareTo(other.value)
        }
    }
}

operator fun <T : Comparable<T>> SlideState<T>.compareTo(other: T): Int {
    return when (this) {
        SlideState.Entering -> -1
        SlideState.Exiting ->  1
        is SlideState.Index -> value.compareTo(other)
    }
}

fun <R, T> SlideState<T>.map(transform: (T) -> R): SlideState<R> {
    return when (this) {
        SlideState.Entering -> SlideState.Entering
        SlideState.Exiting -> SlideState.Exiting
        is SlideState.Index -> SlideState.Index(transform(value))
    }
}

fun <T> SlideState<T>.toValue(entering: T, exiting: T): T {
    return when (this) {
        SlideState.Entering -> entering
        SlideState.Exiting -> exiting
        is SlideState.Index -> value
    }
}

/**
 * Converts the [SlideState] of an [Int] to just a raw `Int`; where [Int.MIN_VALUE] is the default
 * representation for [SlideState.Entering], and [Int.MAX_VALUE] is the default representation for
 * [SlideState.Exiting].
 */
fun SlideState<Int>.toInt(entering: Int = Int.MIN_VALUE, exiting: Int = Int.MAX_VALUE): Int {
    return toValue(entering, exiting)
}

/**
 * Converts the [SlideState] of a [Boolean] to just a raw `Boolean`; where `false` is the default
 * representation for [SlideState.Entering], and `true` is the default representation for
 * [SlideState.Exiting].
 */
fun SlideState<Boolean>.toBoolean(entering: Boolean = false, exiting: Boolean = true): Boolean {
    return toValue(entering, exiting)
}
