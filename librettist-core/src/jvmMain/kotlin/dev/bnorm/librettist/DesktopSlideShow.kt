package dev.bnorm.librettist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import dev.bnorm.librettist.show.*

fun DesktopSlideShow(
    title: String,
    theme: @Composable () -> ShowTheme,
    builder: ShowBuilder.() -> Unit,
) {
    // Pulled from Google Slides with 1 inch = 100 dp
    val slideSize = DpSize(1000.dp, 563.dp)

    val windowState = WindowState(size = DpSize(1000.dp, 800.dp))
    val showState = ShowState(builder)

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
            SlideShow(
                showState = showState,
                showOverview = windowState.placement != WindowPlacement.Fullscreen,
                theme = theme(),
                targetSize = slideSize,
            )
        }
    }
}
