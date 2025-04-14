package dev.bnorm.storyboard.easel

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.MenuBarScope
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState

@Composable
fun MenuBarScope.DesktopMenu(
    windowState: WindowState,
    content: @Composable MenuBarScope.() -> Unit,
) {
    Menu("Play") {
        Item(text = "Fullscreen", shortcut = KeyShortcut(Key.Companion.P, alt = true, meta = true)) {
            // TODO keynote seems to create a new window which fades in over the entire screen
            //  - is this a better experience then converting the window to full screen?
            //  - would *just* the overview be shown when not in full screen?
            windowState.placement = when (windowState.placement) {
                WindowPlacement.Floating -> WindowPlacement.Fullscreen
                WindowPlacement.Maximized -> WindowPlacement.Fullscreen
                WindowPlacement.Fullscreen -> WindowPlacement.Floating // TODO go back to float or max?
            }
        }
    }
    content()
}
