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
internal fun MenuBarScope.DesktopMenu(
    storyboard: Storyboard,
    windowState: WindowState?,
    window: ComposeWindow?,
) {
    Menu("Easel") {
        if (windowState != null && window != null) {
            Item("Fullscreen", shortcut = KeyShortcut(Key.P, alt = true, meta = true)) {
                // TODO keynote seems to create a new window which fades in over the entire screen
                //  - is this a better experience then converting the window to full screen?
                //  - would *just* the overview be shown when not in full screen?
                windowState.placement = when (windowState.placement) {
                    WindowPlacement.Floating -> WindowPlacement.Fullscreen
                    WindowPlacement.Maximized -> WindowPlacement.Fullscreen
                    WindowPlacement.Fullscreen -> WindowPlacement.Floating // TODO go back to float or max?
                }
            }

            Menu("Scale") {
                val insets = window.insets
                    ?.let { DpSize((it.left + it.right).dp, (it.top + it.bottom).dp) }
                    ?: DpSize.Zero

                val size = DpSize(storyboard.format.size.width.dp, storyboard.format.size.height.dp)

                Item("200%") {
                    windowState.placement = WindowPlacement.Floating
                    windowState.size = size * 2.0f + insets
                }
                Item("150%") {
                    windowState.placement = WindowPlacement.Floating
                    windowState.size = size * 1.5f + insets
                }
                Item("100%") {
                    windowState.placement = WindowPlacement.Floating
                    windowState.size = size * 1.0f + insets
                }
                Item("75%") {
                    windowState.placement = WindowPlacement.Floating
                    windowState.size = size * 0.75f + insets
                }
                Item("50%") {
                    windowState.placement = WindowPlacement.Floating
                    windowState.size = size * 0.5f + insets
                }
                Item("25%") {
                    windowState.placement = WindowPlacement.Floating
                    windowState.size = size * 0.25f + insets
                }
            }
        }
    }
}
