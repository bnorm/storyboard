package dev.bnorm.storyboard.core

import dev.bnorm.storyboard.core.SlideState.*

sealed class SlideState<out T> {
    data object Start : SlideState<Nothing>()
    data object End : SlideState<Nothing>()

    class Value<out T>(val value: T) : SlideState<T>()
}

fun <R, T> SlideState<T>.map(transform: (T) -> R): SlideState<R> {
    return when (this) {
        Start -> Start
        End -> End
        is Value -> Value(transform(value))
    }
}

/**
 * Converts the [SlideState] of type [T] to just a value of type `T`; where [start] is the default
 * representation for [SlideState.Start], and [end] is the default representation for
 * [SlideState.End].
 */
fun <T> SlideState<T>.toState(start: T, end: T): T {
    return when (this) {
        Start -> start
        End -> end
        is Value -> value
    }
}

/**
 * Converts the [SlideState] of an [Int] to just a raw `Int`; where [Int.MIN_VALUE] is the default
 * representation for [SlideState.Start], and [Int.MAX_VALUE] is the default representation for
 * [SlideState.End].
 */
fun SlideState<Int>.toInt(start: Int = Int.MIN_VALUE, end: Int = Int.MAX_VALUE): Int {
    return toState(start, end)
}

/**
 * Converts the [SlideState] of a [Boolean] to just a raw `Boolean`; where `false` is the default
 * representation for [SlideState.Start], and `true` is the default representation for
 * [SlideState.End].
 */
fun SlideState<Boolean>.toBoolean(start: Boolean = false, end: Boolean = true): Boolean {
    return toState(start, end)
}
