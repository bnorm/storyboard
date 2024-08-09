package dev.bnorm.librettist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.bnorm.librettist.show.AdvanceDirection
import dev.bnorm.librettist.show.ShowState

val DEFAULT_SLIDE_SIZE = DpSize(1920.dp, 1080.dp)

@Composable
fun SlideShowDisplay(
    showState: ShowState,
    slideSize: DpSize,
    modifier: Modifier = Modifier,
) {
    ScaledBox(
        targetSize = slideSize,
        modifier = modifier
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            showState.Present()
        }
    }
}

fun Modifier.onShowNavigation(showState: ShowState): Modifier {
    var keyHeld = false
    return onPreviewKeyEvent { event ->
        // TODO rate-limit holding down the key?
        when (event.type) {
            KeyEventType.KeyDown -> {
                val wasHeld = keyHeld
                keyHeld = true

                when (event.key) {
                    Key.DirectionRight,
                    Key.DirectionDown,
                    Key.Enter,
                    Key.Spacebar,
                        -> return@onPreviewKeyEvent showState.advance(AdvanceDirection.Forward, jump = wasHeld)

                    Key.DirectionLeft,
                    Key.DirectionUp,
                    Key.Backspace,
                        -> return@onPreviewKeyEvent showState.advance(AdvanceDirection.Backward, jump = wasHeld)
                }
            }

            KeyEventType.KeyUp -> {
                keyHeld = false
            }
        }

        return@onPreviewKeyEvent false
    }
}
