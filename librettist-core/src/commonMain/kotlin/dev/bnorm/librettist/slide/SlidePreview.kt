package dev.bnorm.librettist.slide

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.DpSize
import dev.bnorm.librettist.ScaledBox
import dev.bnorm.librettist.show.Slide
import dev.bnorm.librettist.show.SlideContent
import dev.bnorm.librettist.show.SlideScope
import dev.bnorm.librettist.show.SlideState
import dev.bnorm.librettist.show.assist.LocalShowAssistState

@Composable
fun SlidePreview(
    slide: Slide,
    state: Int,
    slideSize: DpSize,
    modifier: Modifier = Modifier,
) {
    require(state in 0..<slide.states)
    SlidePreview(state, slideSize, modifier, slide.content)
}

@Composable
fun <T> SlidePreview(
    state: T,
    slideSize: DpSize,
    modifier: Modifier = Modifier,
    content: SlideContent<SlideState<T>>,
) {
    // TODO how to disable all clicks and scrolls from propagating into slide?
    CompositionLocalProvider(
        LocalShowAssistState provides null,
        LocalUriHandler provides noopUriHandler,
    ) {
        ScaledBox(
            targetSize = slideSize,
            modifier = modifier
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                SharedTransitionLayout {
                    AnimatedContent(Unit) {
                        key(state) {
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
    }
}

private val noopUriHandler = object : UriHandler {
    override fun openUri(uri: String) {
    }
}
