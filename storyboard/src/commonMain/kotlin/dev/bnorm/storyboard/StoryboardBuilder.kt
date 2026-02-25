package dev.bnorm.storyboard

@DslMarker
internal annotation class StoryboardBuilderDsl

@StoryboardBuilderDsl
public sealed interface StoryboardBuilder {
    @StoryboardBuilderDsl
    public fun <T> scene(
        frames: List<T>,
        enterTransition: SceneEnterTransition = SceneEnterTransition.None,
        exitTransition: SceneExitTransition = SceneExitTransition.None,
        content: SceneContent<T>,
    ): Scene<T>

    @StoryboardBuilderDsl
    public fun scene(
        frameCount: Int = 1,
        enterTransition: SceneEnterTransition = SceneEnterTransition.None,
        exitTransition: SceneExitTransition = SceneExitTransition.None,
        content: SceneContent<Int>,
    ): Scene<Int> {
        require(frameCount >= 0) { "frameCount must be greater than or equal to 0" }
        return scene((0..<frameCount).toList(), enterTransition, exitTransition, content)
    }
}
