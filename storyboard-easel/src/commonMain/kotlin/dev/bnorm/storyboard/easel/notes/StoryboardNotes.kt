package dev.bnorm.storyboard.easel.notes

import androidx.compose.runtime.*

@Stable
class StoryboardNotes {
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

val LocalStoryboardNotes = compositionLocalOf<StoryboardNotes?> { null }

@Composable
fun NotesTab(title: String, content: @Composable () -> Unit) {
    val storyboardNotes = LocalStoryboardNotes.current
    if (storyboardNotes != null) {
        val tabContent = rememberUpdatedState(content)
        DisposableEffect(tabContent) {
            val tab = StoryboardNotes.Tab(title, tabContent.value)
            storyboardNotes.addTab(tab)
            onDispose {
                storyboardNotes.removeTab(tab)
            }
        }
    }
}
