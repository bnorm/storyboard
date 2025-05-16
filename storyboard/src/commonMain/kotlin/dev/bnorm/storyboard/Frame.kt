package dev.bnorm.storyboard

public sealed class Frame<out T> {
    public data object Start : Frame<Nothing>() {
        override fun toString(): String = "Frame.Start"
    }

    public data object End : Frame<Nothing>() {
        override fun toString(): String = "Frame.End"
    }

    public class State<out T>(public val state: T) : Frame<T>() {
        override fun toString(): String = "Frame.State($state)"
    }
}

public operator fun <T : Comparable<T>> Frame<T>.compareTo(other: Frame<T>): Int {
    return when (this) {
        Frame.Start -> if (other == Frame.Start) 0 else -1
        Frame.End -> if (other == Frame.End) 0 else 1
        is Frame.State -> when (other) {
            Frame.Start -> 1
            Frame.End -> -1
            is Frame.State -> state.compareTo(other.state)
        }
    }
}

public fun <T, R> Frame<T>.map(transform: (T) -> R): Frame<R> {
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
 * By default, the first and last states of the scene will be used
 * and must be specified if the scene has no states.
 */
context(sceneScope: SceneScope<R>)
public fun <T, R : T> Frame<R>.toState(
    start: T = when {
        sceneScope.states.isNotEmpty() -> sceneScope.states.first()
        else -> error("implicit conversion to state requires non-empty states")
    },
    end: T = when {
        sceneScope.states.isNotEmpty() -> sceneScope.states.last()
        else -> error("implicit conversion to state requires non-empty states")
    },
): T {
    return when (this) {
        Frame.Start -> start
        Frame.End -> end
        is Frame.State -> state
    }
}

/**
 * Converts the [Frame] of type [T] to just a value of type `T`; where [start] is the default
 * representation for [Frame.Start], and [end] is the default representation for
 * [Frame.End].
 */
public fun <T> Frame<T>.toState(start: T, end: T): T {
    return when (this) {
        Frame.Start -> start
        Frame.End -> end
        is Frame.State -> state
    }
}
