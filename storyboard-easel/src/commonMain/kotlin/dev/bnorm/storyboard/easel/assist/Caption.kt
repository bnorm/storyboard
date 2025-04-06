package dev.bnorm.storyboard.easel.assist

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList

@Immutable
class Caption(
    val title: String,
    val content: @Composable () -> Unit,
)

internal val LocalCaptions = compositionLocalOf<SnapshotStateList<Caption>?> { null }

@Composable
fun SceneCaption(title: String, content: @Composable () -> Unit) {
    val captions = LocalCaptions.current
    if (captions != null) {
        val captionContent = rememberUpdatedState(content)
        DisposableEffect(title, captionContent) {
            val tab = Caption(title, captionContent.value)
            captions.add(tab)
            onDispose {
                captions.remove(tab)
            }
        }
    }
}
