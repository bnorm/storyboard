package dev.bnorm.storyboard.core

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import kotlin.enums.enumEntries

@DslMarker
annotation class StoryboardBuilderDsl

val DefaultEnterTransition: (AdvanceDirection) -> EnterTransition = { EnterTransition.None }

val DefaultExitTransition: (AdvanceDirection) -> ExitTransition = { ExitTransition.None }

@StoryboardBuilderDsl
sealed interface StoryboardBuilder {
    @StoryboardBuilderDsl
    fun <T> slide(
        states: List<T>,
        enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
        exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
        content: SlideContent<T>,
    ): Slide<T>
}

@StoryboardBuilderDsl
fun <T> StoryboardBuilder.slide(
    vararg states: T,
    enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
    exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
    content: SlideContent<T>,
): Slide<T> {
    return slide(states.asList(), enterTransition, exitTransition, content)
}

@StoryboardBuilderDsl
fun StoryboardBuilder.slide(
    stateCount: Int = 1,
    enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
    exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
    content: SlideContent<Int>,
): Slide<Int> {
    require(stateCount > 0) { "stateCount must be greater than 0" }
    return slide((0..<stateCount).toList(), enterTransition, exitTransition, content)
}

@StoryboardBuilderDsl
inline fun <reified T : Enum<T>> StoryboardBuilder.slideForEnum(
    noinline enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
    noinline exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
    noinline content: SlideContent<T>,
): Slide<T> {
    return slide(enumEntries<T>(), enterTransition, exitTransition, content)
}

@StoryboardBuilderDsl
fun StoryboardBuilder.slideForBoolean(
    enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
    exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
    content: SlideContent<Boolean>,
): Slide<Boolean> {
    val states = listOf(false, true)
    return slide(states, enterTransition, exitTransition, content)
}

@StoryboardBuilderDsl
fun StoryboardBuilder.slideForTransition(
    enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
    exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
    content: SlideContent<Nothing>,
): Slide<Nothing> {
    return slide(emptyList(), enterTransition, exitTransition, content)
}
