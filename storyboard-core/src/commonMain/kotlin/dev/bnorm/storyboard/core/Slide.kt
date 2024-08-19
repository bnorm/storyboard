package dev.bnorm.storyboard.core

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Stable
sealed interface Slide<T> : Comparable<Slide<*>> {
    class State<out T>(
        val value: T,
        val transitional: Boolean = false, // I.e., not shown in overview and export
    )

    val states: ImmutableList<State<T>>
    val currentIndex: Int

    val enterTransition: (AdvanceDirection) -> EnterTransition
    val exitTransition: (AdvanceDirection) -> ExitTransition
    val content: SlideContent<T>
}

fun <T> slideStateOf(
    value: T,
    transitional: Boolean = false,
): Slide.State<T> = Slide.State(value, transitional)

fun <T> slideStateOf(
    transitional: Boolean = false,
    value: @Composable () -> T,
): Slide.State<@Composable () -> T> = Slide.State(value, transitional)
