package dev.bnorm.librettist

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.*
import dev.bnorm.librettist.show.*
import dev.bnorm.librettist.show.assist.LocalShowAssistState
import dev.bnorm.librettist.show.assist.ShowAssist
import dev.bnorm.librettist.show.assist.ShowAssistState

@Composable
fun ApplicationScope.DesktopSlideShow(
    title: String,
    theme: ShowTheme,
    slideSize: DpSize = DEFAULT_SLIDE_SIZE,
    builder: ShowBuilder.() -> Unit,
) {
    val windowState = remember { WindowState() }
    val showState = remember(builder) { ShowState(builder) }
    val showAssistState = remember { ShowAssistState() }
    var goToSlide by remember { mutableStateOf(false) }
    var showOverview by remember { mutableStateOf(false) }

    fun handleKeyEvent(event: KeyEvent): Boolean {
        if (event.type == KeyEventType.KeyUp) {
            when (event.key) {
                Key.Escape -> {
                    showOverview = !showOverview
                    return true
                }

                Key.Enter -> if (showOverview) {
                    showOverview = false
                    return true
                }

                Key.F -> if (event.isCtrlPressed && event.isMetaPressed) {
                    windowState.placement = when (windowState.placement) {
                        WindowPlacement.Floating -> WindowPlacement.Fullscreen
                        WindowPlacement.Maximized -> WindowPlacement.Fullscreen
                        WindowPlacement.Fullscreen -> WindowPlacement.Floating // TODO go back to float or max?
                    }
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
                    windowState.placement = when (windowState.placement) {
                        WindowPlacement.Floating -> WindowPlacement.Fullscreen
                        WindowPlacement.Maximized -> WindowPlacement.Fullscreen
                        WindowPlacement.Fullscreen -> WindowPlacement.Floating // TODO go back to float or max?
                    }
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
            Show(
                showState = showState,
                slideSize = slideSize,
                theme = theme,
                showOverview = showOverview,
                modifier = Modifier
                    .focusRequester(rememberFocusRequester()).focusTarget()
                    .run { if (showOverview) onOverviewNavigation(showState) else onShowNavigation(showState) },
            )
        }
    }

    if (showAssistState.visible) {
        Window(
            onCloseRequest = { showAssistState.visible = false },
            title = "Assist",
            onPreviewKeyEvent = ::handleKeyEvent,
        ) {
            ShowMenu()

            ShowAssist(
                slideSize = slideSize,
                theme = theme,
                showState = showState,
                showAssistState = showAssistState,
                modifier = Modifier
                    .focusRequester(rememberFocusRequester()).focusTarget()
                    .run { if (showOverview) onOverviewNavigation(showState) else onShowNavigation(showState) },
            )
        }
    }

    if (goToSlide) {
        GoToSlide(showState, onClose = { goToSlide = false })
    }
}

@Composable
private fun Show(
    showState: ShowState,
    slideSize: DpSize,
    theme: ShowTheme,
    showOverview: Boolean,
    modifier: Modifier = Modifier,
) {
    ShowTheme(theme) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            SharedTransitionLayout {
                AnimatedContent(
                    targetState = showOverview,
                    transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) }
                ) { showOverview ->
                    if (showOverview) {
                        ShowOverview(
                            showState = showState,
                            slideSize = slideSize,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@AnimatedContent,
                            modifier = Modifier.fillMaxSize(),
                        )
                    } else {
                        SlideShowDisplay(
                            showState = showState,
                            slideSize = slideSize,
                            modifier = Modifier.sharedElement(
                                state = rememberSharedContentState(key = "slide:current"),
                                animatedVisibilityScope = this@AnimatedContent
                            ).fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GoToSlide(showState: ShowState, onClose: () -> Unit) {
    DialogWindow(onCloseRequest = onClose, title = "Go To Slide") {
        var slide by remember { mutableStateOf("") }
        val indices = remember(showState) { showState.slides.toIndexes() }

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
            modifier = Modifier.focusTarget().focusRequester(rememberFocusRequester()).onKeyEvent(::onKeyEvent)
        )
    }
}
