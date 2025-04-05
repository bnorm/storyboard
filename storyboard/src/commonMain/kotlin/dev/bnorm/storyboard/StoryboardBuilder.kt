package dev.bnorm.storyboard

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
    fun <T> scene(
        states: List<T>,
        enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
        exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
        content: SceneContent<T>,
    ): Scene<T>

    @StoryboardBuilderDsl
    fun scene(
        stateCount: Int = 1,
        enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
        exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
        content: SceneContent<Int>,
    ): Scene<Int> {
        require(stateCount > 0) { "stateCount must be greater than 0" }
        return scene((0..<stateCount).toList(), enterTransition, exitTransition, content)
    }
}

@StoryboardBuilderDsl
fun <T> StoryboardBuilder.scene(
    vararg states: T,
    enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
    exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
    content: SceneContent<T>,
): Scene<T> {
    return scene(states.asList(), enterTransition, exitTransition, content)
}

@StoryboardBuilderDsl
inline fun <reified T : Enum<T>> StoryboardBuilder.sceneForEnum(
    noinline enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
    noinline exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
    noinline content: SceneContent<T>,
): Scene<T> {
    return scene(enumEntries<T>(), enterTransition, exitTransition, content)
}

@StoryboardBuilderDsl
fun StoryboardBuilder.sceneForBoolean(
    enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
    exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
    content: SceneContent<Boolean>,
): Scene<Boolean> {
    val states = listOf(false, true)
    return scene(states, enterTransition, exitTransition, content)
}

@StoryboardBuilderDsl
fun StoryboardBuilder.sceneForTransition(
    enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
    exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
    content: SceneContent<Nothing>,
): Scene<Nothing> {
    return scene(emptyList(), enterTransition, exitTransition, content)
}
