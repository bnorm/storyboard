package dev.bnorm.storyboard

public sealed class Frame<out T> {
    public data object Start : Frame<Nothing>() {
        override fun toString(): String = "Frame.Start"
    }

    public data object End : Frame<Nothing>() {
        override fun toString(): String = "Frame.End"
    }

    public class Value<out T>(public val value: T) : Frame<T>() {
        override fun toString(): String = "Frame.Value($value)"
    }
}

public operator fun <T : Comparable<T>> Frame<T>.compareTo(other: Frame<T>): Int {
    return when (this) {
        Frame.Start -> if (other == Frame.Start) 0 else -1
        Frame.End -> if (other == Frame.End) 0 else 1
        is Frame.Value -> when (other) {
            Frame.Start -> 1
            Frame.End -> -1
            is Frame.Value -> value.compareTo(other.value)
        }
    }
}

public fun <T, R> Frame<T>.map(transform: (T) -> R): Frame<R> {
    return when (this) {
        Frame.Start -> Frame.Start
        Frame.End -> Frame.End
        is Frame.Value -> Frame.Value(transform(value))
    }
}

/**
 * Converts the [Frame] of type [T] to just a value of type `T`; where [start] is the default
 * representation for [Frame.Start], and [end] is the default representation for
 * [Frame.End].
 * By default, the first and last frames of the scene will be used
 * and must be specified if the scene has no frames.
 */
context(sceneScope: SceneScope<R>)
public fun <T, R : T> Frame<R>.toValue(
    start: T = when {
        sceneScope.frames.isNotEmpty() -> sceneScope.frames.first()
        else -> error("implicit conversion to value requires non-empty frames")
    },
    end: T = when {
        sceneScope.frames.isNotEmpty() -> sceneScope.frames.last()
        else -> error("implicit conversion to value requires non-empty frames")
    },
): T {
    return when (this) {
        Frame.Start -> start
        Frame.End -> end
        is Frame.Value -> value
    }
}

/**
 * Converts the [Frame] of type [T] to just a value of type `T`; where [start] is the default
 * representation for [Frame.Start], and [end] is the default representation for
 * [Frame.End].
 */
public fun <T> Frame<T>.toValue(start: T, end: T): T {
    return when (this) {
        Frame.Start -> start
        Frame.End -> end
        is Frame.Value -> value
    }
}
