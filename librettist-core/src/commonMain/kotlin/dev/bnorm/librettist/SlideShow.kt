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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.bnorm.librettist.show.LocalShowState
import dev.bnorm.librettist.show.ShowState
import dev.bnorm.librettist.show.assist.LocalShowAssistState

@Composable
fun SlideShow(
    showState: ShowState,
    showOverview: Boolean,
    theme: ShowTheme,
    targetSize: DpSize
) {
    CompositionLocalProvider(LocalShowState provides showState) {
        ShowTheme(theme) {
            Row(modifier = Modifier.fillMaxSize()) {
                val state = rememberLazyListState()
                if (showOverview) {
                    SlideShowOverview(
                        targetSize = targetSize,
                        showState = showState,
                        modifier = Modifier.weight(0.2f),
                        state = state
                    )
                }

                SlideShowDisplay(targetSize, showState.index, showState, Modifier.weight(0.8f))
            }
        }
    }
}

@Composable
fun SlideShowDisplay(
    targetSize: DpSize,
    slideIndex: Int,
    showState: ShowState,
    modifier: Modifier = Modifier,
) {
    ScaledBox(
        targetSize = targetSize,
        modifier = modifier.fillMaxHeight().background(MaterialTheme.colors.background)
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            // TODO why is this box required for proper alignment?
            Box(modifier = Modifier.fillMaxSize()) {
                key(slideIndex) {
                    val slide = showState.slides[slideIndex]
                    showState.slide()
                }
            }
        }
    }
}

@Composable
private fun ScaledBox(
    targetSize: DpSize,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var scale by remember { mutableStateOf(1f) }
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .onSizeChanged {
                val (w, h) = with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
                scale = minOf(w / targetSize.width, h / targetSize.height)
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.requiredSize(targetSize).scale(scale)) {
            content()
        }
    }
}

@Composable
fun SlideShowOverview(
    targetSize: DpSize,
    showState: ShowState,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
) {
    // TODO advancement never happens in the overview, but we need
    //  to provide a show state anyways so ListenAdvancement doesn't error.
    //  is there a better way to disable advancement?
    val constantShowState = remember { ShowState(emptyList()) }
    CompositionLocalProvider(LocalShowState provides constantShowState) {
        CompositionLocalProvider(LocalShowAssistState provides null) {

            // TODO: use state.animateScrollToItem(selectedSlide) somehow to always keep selected slide visible (but not always at the top)
            LazyColumn(modifier = modifier, contentPadding = PaddingValues(8.dp), state = state) {
                items(showState.slides.size) { index ->
                    val slide = remember(index) { showState.slides[index] }

                    ScaledBox(
                        targetSize = targetSize,
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp)
                            .aspectRatio(targetSize.width / targetSize.height)
                            .background(MaterialTheme.colors.background)
                            .clickable { showState.index = index }
                            .then(if (index == showState.index) Modifier.border(2.dp, Color.Red) else Modifier)
                    ) {
                        Surface(modifier = Modifier.fillMaxSize()) {
                            // TODO why is this box required for proper alignment?
                            Box(modifier = Modifier.fillMaxSize()) {
                                constantShowState.slide()
                            }
                        }
                    }
                }
            }
        }
    }
}
