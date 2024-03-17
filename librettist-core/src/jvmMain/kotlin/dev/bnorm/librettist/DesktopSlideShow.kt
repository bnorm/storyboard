package dev.bnorm.librettist

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import dev.bnorm.librettist.show.Advancement
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.ShowState
import dev.bnorm.librettist.show.assist.LocalShowAssistState
import dev.bnorm.librettist.show.assist.ShowAssist
import dev.bnorm.librettist.show.assist.ShowAssistState

fun DesktopSlideShow(
    title: String,
    theme: @Composable () -> ShowTheme,
    slideSize: DpSize = DEFAULT_SLIDE_SIZE,
    builder: ShowBuilder.() -> Unit,
) {
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

        return false
    }

    @Composable
    fun FrameWindowScope.ShowMenu() {
        MenuBar {
            Menu("Play") {
                Item(text = "Play Slideshow", shortcut = KeyShortcut(Key.P, alt = true, meta = true)) {
                    // TODO keynote seems to create a new window which fades in over the entire screen
                    //  - is this a better experience then converting the window to full screen?
                    //  - would *just* the overview be shown when note in full screen?
                    windowState.placement = WindowPlacement.Fullscreen
                }
            }
            Menu("Assist") {
                CheckboxItem(text = "Visible", checked = showAssistState.visible, shortcut = KeyShortcut(Key.F2)) {
                    showAssistState.visible = it
                }
            }
        }
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = title,
            onPreviewKeyEvent = ::handleKeyEvent,
        ) {
            ShowMenu()

            CompositionLocalProvider(LocalShowAssistState provides showAssistState) {
                ShowTheme(theme()) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        val state = rememberLazyListState()
                        if (windowState.placement != WindowPlacement.Fullscreen) {
                            SlideShowOverview(
                                showState = showState,
                                slideSize = slideSize,
                                modifier = Modifier.weight(0.2f),
                                state = state
                            )
                        }

                        SlideShowDisplay(
                            showState = showState,
                            slideSize = slideSize,
                            modifier = Modifier.weight(0.8f).fillMaxHeight()
                        )
                    }
                }
            }
        }

        if (showAssistState.visible) {
            Window(
                onCloseRequest = { showAssistState.visible = false },
                title = "Assist",
            ) {
                ShowMenu()

                ShowAssist(showAssistState)
            }
        }
    }
}
