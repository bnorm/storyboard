package dev.bnorm.librettist.slide

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import dev.bnorm.librettist.ScaledBox
import dev.bnorm.librettist.show.Slide
import dev.bnorm.librettist.show.SlideScope
import dev.bnorm.librettist.show.SlideState

@Composable
fun SlidePreview(
    slide: Slide,
    state: Int,
    size: DpSize,
    modifier: Modifier = Modifier,
) {
    require(state in 0..<slide.states)

    // TODO how to disable all clicks and scrolls from propagating into slide?
    ScaledBox(
        targetSize = size,
        modifier = modifier
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            SharedTransitionLayout {
                AnimatedContent(Unit) {
                    val content = slide.content
                    val scope = SlideScope(
                        SlideState.Index(state),
                        this@AnimatedContent,
                        this@SharedTransitionLayout
                    )
                    scope.content()
                }
            }
        }
    }
}