package dev.bnorm.storyboard.easel.assist

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import dev.bnorm.storyboard.SceneMode
import dev.bnorm.storyboard.LocalSceneMode

@Immutable
class Caption(
    val content: @Composable () -> Unit,
)

internal val LocalCaptions = compositionLocalOf<SnapshotStateList<Caption>?> { null }

/**
 * Creates a [Caption] within the [StoryAssistant].
 *
 * Captions are only created when the Scene is in [Story][SceneMode.Story] mode.
 * This means captions will disappear when the
 * [overview][dev.bnorm.storyboard.easel.overview.StoryOverview] is opened.
 */
// TODO should the overview be able to support gathering captions for the selected scene?
@Composable
fun SceneCaption(content: @Composable () -> Unit) {
    val captions = LocalCaptions.current
    if (captions != null && LocalSceneMode.current == SceneMode.Story) {
        val captionContent = rememberUpdatedState(content)
        DisposableEffect(captionContent) {
            val caption = Caption(captionContent.value)
            captions.add(caption)
            onDispose { captions.remove(caption) }
        }
    }
}
