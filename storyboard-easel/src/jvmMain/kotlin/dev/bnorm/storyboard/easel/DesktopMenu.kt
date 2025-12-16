package dev.bnorm.storyboard.easel

import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBarScope
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import dev.bnorm.storyboard.Storyboard

@Composable
fun MenuBarScope.DesktopMenu(
    storyboard: Storyboard,
    windowState: WindowState,
    window: ComposeWindow? = null,
    content: @Composable MenuBarScope.() -> Unit,
) {
    Menu("Easel") {
        Item(text = "Fullscreen", shortcut = KeyShortcut(Key.P, alt = true, meta = true)) {
            // TODO keynote seems to create a new window which fades in over the entire screen
            //  - is this a better experience then converting the window to full screen?
            //  - would *just* the overview be shown when not in full screen?
            windowState.placement = when (windowState.placement) {
                WindowPlacement.Floating -> WindowPlacement.Fullscreen
                WindowPlacement.Maximized -> WindowPlacement.Fullscreen
                WindowPlacement.Fullscreen -> WindowPlacement.Floating // TODO go back to float or max?
            }
        }
        if (window != null) {
            Menu("Scale") {
                val insets = window
                    .insets
                    ?.let { DpSize((it.left + it.right).dp, (it.top + it.bottom).dp) }
                    ?: DpSize.Zero

                val size = DpSize(storyboard.format.size.width.dp, storyboard.format.size.height.dp)

                Item(text = "200%") {
                    windowState.placement = WindowPlacement.Floating
                    windowState.size = size * 2f + insets
                }
                Item(text = "150%") {
                    windowState.placement = WindowPlacement.Floating
                    windowState.size = size * 1.5f + insets
                }
                Item(text = "100%") {
                    windowState.placement = WindowPlacement.Floating
                    windowState.size = size + insets
                }
                Item(text = "75%") {
                    windowState.placement = WindowPlacement.Floating
                    windowState.size = size * .75f + insets
                }
                Item(text = "50%") {
                    windowState.placement = WindowPlacement.Floating
                    windowState.size = size * .5f + insets
                }
                Item(text = "25%") {
                    windowState.placement = WindowPlacement.Floating
                    windowState.size = size * .25f + insets
                }
            }
        }
    }
    content()
}
