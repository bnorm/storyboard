package dev.bnorm.librettist

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import dev.bnorm.librettist.show.AdvanceDirection
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.ShowState
import dev.bnorm.librettist.show.assist.LocalShowAssistState
import dev.bnorm.librettist.show.assist.ShowAssist
import dev.bnorm.librettist.show.assist.ShowAssistState
import dev.bnorm.librettist.show.indices

@Composable
fun ApplicationScope.DesktopSlideShow(
    title: String,
    theme: ShowTheme,
    slideSize: DpSize = DEFAULT_SLIDE_SIZE,
    builder: ShowBuilder.() -> Unit,
) {
    val windowState = remember { WindowState(size = DpSize(1000.dp, 800.dp)) }
    val showState = remember(builder) { ShowState(builder) }
    val showAssistState = remember { ShowAssistState() }
    var goToSlide by remember { mutableStateOf(false) }

    fun handleKeyEvent(event: KeyEvent): Boolean {
        // TODO rate-limit holding down the key?
        if (event.type == KeyEventType.KeyDown) {
            when (event.key) {
                Key.DirectionRight,
                Key.Enter,
                Key.Spacebar,
                -> return showState.advance(AdvanceDirection.Forward)

                Key.DirectionLeft,
                Key.Backspace,
                -> return showState.advance(AdvanceDirection.Backward)
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
                Item(text = "Jump To Slide", shortcut = KeyShortcut(Key.J, meta = true)) {
                    goToSlide = true
                }
            }
            Menu("Assist") {
                CheckboxItem(text = "Visible", checked = showAssistState.visible, shortcut = KeyShortcut(Key.F2)) {
                    showAssistState.visible = it
                }
            }
        }
    }

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = title,
        onPreviewKeyEvent = ::handleKeyEvent,
    ) {
        ShowMenu()

        CompositionLocalProvider(LocalShowAssistState provides showAssistState) {
            ShowTheme(theme) {
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

            ShowAssist(slideSize, theme, showState, showAssistState)
        }
    }

    if (goToSlide) {
        GoToSlide(showState, onClose = { goToSlide = false })
    }
}

@Composable
private fun GoToSlide(showState: ShowState, onClose: () -> Unit) {
    val focusRequester = remember { FocusRequester() }

    DialogWindow(onCloseRequest = onClose, title = "Go To Slide") {
        var slide by remember { mutableStateOf("") }
        val indices = remember(showState) { showState.slides.indices }

        fun onKeyEvent(it: KeyEvent): Boolean {
            if (it.key == Key.Enter) {
                val index = slide.toIntOrNull()
                if (index != null) {
                    showState.jumpToSlide(indices[index.coerceIn(0, indices.size - 1)])
                    onClose()
                    return true
                }
            }

            return false
        }

        TextField(
            value = slide,
            onValueChange = { slide = it.trim() },
            isError = slide.toIntOrNull() == null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.focusTarget().focusRequester(focusRequester).onKeyEvent(::onKeyEvent)
        )
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
