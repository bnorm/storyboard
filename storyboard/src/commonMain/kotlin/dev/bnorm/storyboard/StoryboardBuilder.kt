package dev.bnorm.storyboard

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import kotlin.enums.enumEntries

@DslMarker
internal annotation class StoryboardBuilderDsl

public val DefaultEnterTransition: (AdvanceDirection) -> EnterTransition = { EnterTransition.None }

public val DefaultExitTransition: (AdvanceDirection) -> ExitTransition = { ExitTransition.None }

@StoryboardBuilderDsl
public sealed interface StoryboardBuilder {
    @StoryboardBuilderDsl
    public fun <T> scene(
        states: List<T>,
        enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
        exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
        content: SceneContent<T>,
    ): Scene<T>

    @StoryboardBuilderDsl
    public fun scene(
        stateCount: Int = 1,
        enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
        exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
        content: SceneContent<Int>,
    ): Scene<Int> {
        require(stateCount >= 0) { "stateCount must be greater than or equal to 0" }
        return scene((0..<stateCount).toList(), enterTransition, exitTransition, content)
    }
}

@StoryboardBuilderDsl
public fun <T> StoryboardBuilder.scene(
    vararg states: T,
    enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
    exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
    content: SceneContent<T>,
): Scene<T> {
    return scene(states.asList(), enterTransition, exitTransition, content)
}

@StoryboardBuilderDsl
public inline fun <reified T : Enum<T>> StoryboardBuilder.sceneForEnum(
    noinline enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
    noinline exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
    content: SceneContent<T>,
): Scene<T> {
    return scene(enumEntries<T>(), enterTransition, exitTransition, content)
}

@StoryboardBuilderDsl
public fun StoryboardBuilder.sceneForBoolean(
    enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
    exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
    content: SceneContent<Boolean>,
): Scene<Boolean> {
    val states = listOf(false, true)
    return scene(states, enterTransition, exitTransition, content)
}

@StoryboardBuilderDsl
public fun StoryboardBuilder.sceneForTransition(
    enterTransition: (AdvanceDirection) -> EnterTransition = DefaultEnterTransition,
    exitTransition: (AdvanceDirection) -> ExitTransition = DefaultExitTransition,
    content: SceneContent<Nothing>,
): Scene<Nothing> {
    return scene(emptyList(), enterTransition, exitTransition, content)
}
