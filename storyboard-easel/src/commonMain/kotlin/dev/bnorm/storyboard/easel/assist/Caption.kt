package dev.bnorm.storyboard.easel.assist

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList

@Immutable
class Caption(
    val content: @Composable () -> Unit,
)

internal val LocalCaptions = compositionLocalOf<SnapshotStateList<Caption>?> { null }

/**
 * Creates a [Caption] within the [StoryAssistant].
 *
 * Captions are only created from within the Story Assistant,
 * and composition-local state is not shared between the Assistant window
 * and the Story window.
 * If state needs to be shared, consider using a view-model-like class,
 * and hoisting it to where the Storyboard is created.
 * Make sure the Storyboard is created outside of composition (or remembered)
 * so that it does not trigger recomposition!
 * See the interactive example for more details on how to achieve shared
 * state between the Assistant and the Story windows.
 */
@Composable
fun SceneCaption(content: @Composable () -> Unit) {
    val captions = LocalCaptions.current
    if (captions != null) {
        val captionContent = rememberUpdatedState(content)
        DisposableEffect(captionContent) {
            val tab = Caption(captionContent.value)
            captions.add(tab)
            onDispose {
                captions.remove(tab)
            }
        }
    }
}
