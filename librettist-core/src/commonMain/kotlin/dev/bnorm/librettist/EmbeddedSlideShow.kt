package dev.bnorm.librettist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.unit.DpSize
import dev.bnorm.librettist.show.*

@Composable
fun EmbeddedSlideShow(
    theme: ShowTheme,
    slideSize: DpSize = DEFAULT_SLIDE_SIZE,
    showIndicators: Boolean = true,
    startSlide: Int = 0,
    modifier: Modifier = Modifier,
    builder: ShowBuilder.() -> Unit,
) {
    val showState = remember(builder, startSlide) {
        ShowState(builder).also { state ->
            val indices = state.slides.toIndexes()
            state.jumpToSlide(indices[startSlide.coerceIn(0, indices.size - 1)])
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .focusRequester(rememberFocusRequester()).focusTarget()
            .onShowNavigation(showState)
    ) {
        ShowTheme(theme) {
            SlideShowDisplay(
                showState = showState,
                slideSize = slideSize,
                modifier = Modifier.fillMaxSize()
            )

            if (showIndicators) {
                MouseNavigationIndicators(showState)
            }
        }
    }
}
