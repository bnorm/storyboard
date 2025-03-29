package dev.bnorm.storyboard.easel.notes

import androidx.compose.runtime.*

@Stable
class StoryNotes {
    var visible by mutableStateOf(false)

    private val mutableTabs = mutableStateListOf<Tab>()
    val tabs: List<Tab> = mutableTabs

    fun addTab(tab: Tab) {
        mutableTabs.add(tab)
    }

    fun removeTab(tab: Tab) {
        mutableTabs.remove(tab)
    }

    @Immutable
    class Tab(
        val title: String,
        val content: @Composable () -> Unit,
    )
}

val LocalStoryNotes = compositionLocalOf<StoryNotes?> { null }

@Composable
fun NotesTab(title: String, content: @Composable () -> Unit) {
    val storyboardNotes = LocalStoryNotes.current
    if (storyboardNotes != null) {
        val tabContent = rememberUpdatedState(content)
        DisposableEffect(title, tabContent) {
            val tab = StoryNotes.Tab(title, tabContent.value)
            storyboardNotes.addTab(tab)
            onDispose {
                storyboardNotes.removeTab(tab)
            }
        }
    }
}
