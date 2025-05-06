package dev.bnorm.storyboard.easel

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import dev.bnorm.storyboard.Storyboard

@Composable
fun MenuBarScope.DesktopMenu(
    storyboard: Storyboard,
    windowState: WindowState,
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
        Menu("Scale") {
            // This seems like the best way to get the title bar size,
            // which is needed to correctly set the size of the window.
            @Suppress("INVISIBLE_REFERENCE")
            val insets = LocalWindow.current
                ?.insets
                ?.let { DpSize((it.left + it.right).dp, (it.top + it.bottom).dp) }
                ?: DpSize.Zero

            Item(text = "200%") {
                windowState.placement = WindowPlacement.Floating
                windowState.size = storyboard.size * 2f + insets
            }
            Item(text = "150%") {
                windowState.placement = WindowPlacement.Floating
                windowState.size = storyboard.size * 1.5f + insets
            }
            Item(text = "100%") {
                windowState.placement = WindowPlacement.Floating
                windowState.size = storyboard.size + insets
            }
            Item(text = "75%") {
                windowState.placement = WindowPlacement.Floating
                windowState.size = storyboard.size * .75f + insets
            }
            Item(text = "50%") {
                windowState.placement = WindowPlacement.Floating
                windowState.size = storyboard.size * .5f + insets
            }
            Item(text = "25%") {
                windowState.placement = WindowPlacement.Floating
                windowState.size = storyboard.size * .25f + insets
            }
        }
    }
    content()
}
