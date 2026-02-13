package dev.bnorm.storyboard.easel

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.MenuScope
import dev.bnorm.storyboard.Decorator

// TODO name is not consistent with storyboard theme
// TODO mostly specific to desktop, but not exclusive?
interface EaselWindow {
    val name: String
    var visible: Boolean

    val decorator: Decorator get() = Decorator.None

    @Composable
    fun MenuScope.Menu()

    @Composable
    fun Content()
}
