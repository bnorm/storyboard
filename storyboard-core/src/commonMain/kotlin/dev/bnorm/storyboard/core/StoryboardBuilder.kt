package dev.bnorm.storyboard.core

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import kotlinx.collections.immutable.persistentListOf
import kotlin.jvm.JvmName

@DslMarker
annotation class StoryboardBuilderDsl

@PublishedApi
internal val DefaultEnterTransition: (AdvanceDirection) -> EnterTransition = { EnterTransition.None }

@PublishedApi
internal val DefaultExitTransition: (AdvanceDirection) -> ExitTransition = { ExitTransition.None }

@StoryboardBuilderDsl
sealed interface StoryboardBuilder {
    @StoryboardBuilderDsl
    fun <T> slide(
        states: List<Slide.State<T>>,
        enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
        exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
        content: SlideContent<T>,
    ): Slide<T>
}

@StoryboardBuilderDsl
fun <T> StoryboardBuilder.slide(
    vararg states: Slide.State<T>,
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
    val states = (0..<stateCount).map { slideStateOf(it) }
    return slide(states, enterTransition, exitTransition, content)
}

@StoryboardBuilderDsl
inline fun <reified T : Enum<T>> StoryboardBuilder.slideForEnum(
    noinline enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
    noinline exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
    noinline content: SlideContent<T>,
): Slide<T> {
    val states = enumValues<T>().map { slideStateOf(it) }
    return slide(states, enterTransition, exitTransition, content)
}

@StoryboardBuilderDsl
fun StoryboardBuilder.slideForBoolean(
    enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
    exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
    content: SlideContent<Boolean>,
): Slide<Boolean> {
    val states = persistentListOf(slideStateOf(false), slideStateOf(true))
    return slide(states, enterTransition, exitTransition, content)
}
