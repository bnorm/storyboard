package dev.bnorm.librettist.show.assist

import androidx.compose.runtime.*

@Stable
class ShowAssistState {
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
        val name: String,
        val content: @Composable () -> Unit
    )
}

val LocalShowAssistState = compositionLocalOf<ShowAssistState?> { null }

@Composable
fun ShowAssistTab(name: String, content: @Composable () -> Unit) {
    val state = LocalShowAssistState.current
    if (state != null) {
        val tabContent = rememberUpdatedState(content)
        DisposableEffect(tabContent) {
            val tab = ShowAssistState.Tab(name, tabContent.value)
            state.addTab(tab)
            onDispose {
                state.removeTab(tab)
            }
        }
    }
}
