package dev.bnorm.storyboard

sealed class Frame<out T> {
    data object Start : Frame<Nothing>()
    data object End : Frame<Nothing>()

    class State<out T>(val state: T) : Frame<T>()
}

fun <T, R> Frame<T>.map(transform: (T) -> R): Frame<R> {
    return when (this) {
        Frame.Start -> Frame.Start
        Frame.End -> Frame.End
        is Frame.State -> Frame.State(transform(state))
    }
}

/**
 * Converts the [Frame] of type [T] to just a value of type `T`; where [start] is the default
 * representation for [Frame.Start], and [end] is the default representation for
 * [Frame.End].
 */
fun <T> Frame<T>.toState(start: T, end: T): T {
    return when (this) {
        Frame.Start -> start
        Frame.End -> end
        is Frame.State -> state
    }
}

/**
 * Converts the [Frame] of an [Int] to just a raw `Int`; where [Int.MIN_VALUE] is the default
 * representation for [Frame.Start], and [Int.MAX_VALUE] is the default representation for
 * [Frame.End].
 */
fun Frame<Int>.toInt(start: Int = Int.MIN_VALUE, end: Int = Int.MAX_VALUE): Int {
    return toState(start, end)
}

/**
 * Converts the [Frame] of a [Boolean] to just a raw `Boolean`; where `false` is the default
 * representation for [Frame.Start], and `true` is the default representation for
 * [Frame.End].
 */
fun Frame<Boolean>.toBoolean(start: Boolean = false, end: Boolean = true): Boolean {
    return toState(start, end)
}
