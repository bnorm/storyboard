package dev.bnorm.storyboard

@DslMarker
internal annotation class StoryboardBuilderDsl

@StoryboardBuilderDsl
public sealed interface StoryboardBuilder {
    @StoryboardBuilderDsl
    public fun <T> scene(
        states: List<T>,
        enterTransition: SceneEnterTransition = SceneEnterTransition.Default,
        exitTransition: SceneExitTransition = SceneExitTransition.Default,
        content: SceneContent<T>,
    ): Scene<T>

    @StoryboardBuilderDsl
    public fun scene(
        stateCount: Int = 1,
        enterTransition: SceneEnterTransition = SceneEnterTransition.Default,
        exitTransition: SceneExitTransition = SceneExitTransition.Default,
        content: SceneContent<Int>,
    ): Scene<Int> {
        require(stateCount >= 0) { "stateCount must be greater than or equal to 0" }
        return scene((0..<stateCount).toList(), enterTransition, exitTransition, content)
    }
}
