package dev.bnorm.librettist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import dev.bnorm.librettist.show.Advancement
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.ShowState
import dev.bnorm.librettist.show.assist.LocalShowAssistState
import dev.bnorm.librettist.show.assist.ShowAssist
import dev.bnorm.librettist.show.assist.ShowAssistState

fun DesktopSlideShow(
    title: String,
    theme: @Composable () -> ShowTheme,
    builder: ShowBuilder.() -> Unit,
) {
    // Pulled from Google Slides with 1 inch = 100 dp
    val slideSize = DpSize(1000.dp, 563.dp)

    val windowState = WindowState(size = DpSize(1000.dp, 800.dp))
    val showState = ShowState(builder)
    val showAssistState = ShowAssistState()

    fun handleKeyEvent(event: KeyEvent): Boolean {
        // TODO rate-limit holding down the key?
        if (event.type == KeyEventType.KeyDown) {
            val advancement = when (event.key) {
                Key.DirectionRight,
                Key.Enter,
                Key.Spacebar,
                -> Advancement(direction = Advancement.Direction.Forward)

                Key.DirectionLeft,
                Key.Backspace,
                -> Advancement(direction = Advancement.Direction.Backward)

                else -> null
            }
            if (advancement != null) {
                showState.advance(advancement)
                return true
            }
        }

        // TODO handle some other keys?
        //  - navigating to specific slides?
        if (event.type == KeyEventType.KeyUp) {
            when (event.key) {
                Key.Escape -> if (windowState.placement == WindowPlacement.Fullscreen) {
                    windowState.placement = WindowPlacement.Floating
                    return true
                }

                Key.F -> if (windowState.placement != WindowPlacement.Fullscreen && event.isCtrlPressed && event.isMetaPressed) {
                    windowState.placement = WindowPlacement.Fullscreen
                    return true
                }
            }
        }

        return true
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = title,
            onPreviewKeyEvent = ::handleKeyEvent,
        ) {
            CompositionLocalProvider(LocalShowAssistState provides showAssistState) {
                SlideShow(
                    showState = showState,
                    showOverview = windowState.placement != WindowPlacement.Fullscreen,
                    theme = theme(),
                    targetSize = slideSize,
                )
            }
        }

        Window(
            onCloseRequest = ::exitApplication,
            title = "Assist",
        ) {
            ShowAssist(showAssistState)
        }
    }
}
