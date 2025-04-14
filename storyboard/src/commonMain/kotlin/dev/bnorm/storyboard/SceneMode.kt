package dev.bnorm.storyboard

/**
 * What mode for a [Scene] is active.
 * Scenes can be displayed multiple times and in multiple locations.
 * It is useful for a scene to know what mode it is in,
 * so it can change how it is rendered
 * or perform different [effects][androidx.compose.runtime.LaunchedEffect].
 * For example, a scene may have an animation,
 * which is not useful to play when the scene is being previewed.
 *
 * There are currently two modes:
 * * [Story] - Used when the scene is displayed within the main story.
 * Only one copy of the scene should ever be in story mode.
 * * [Preview] - Used when previewing the display of a scene.
 * For example, in the overview, assistant, or when being exported.
 */
enum class SceneMode {
    Story,
    Preview,
}
