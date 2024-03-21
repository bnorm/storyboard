package dev.bnorm.librettist.show

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf

@Immutable
class SlideSection(
    val title: @Composable () -> Unit,
) {
    companion object {
        val Empty = SlideSection {}

        val header: @Composable () -> Unit
            @Composable
            get() = LocalSlideSection.current.title
    }
}

private val LocalSlideSection = compositionLocalOf { SlideSection.Empty }

@ShowBuilderDsl
fun ShowBuilder.section(
    title: @Composable () -> Unit,
    block: ShowBuilder.() -> Unit,
) {
    val slides = buildSlides(block)
    val advancements = slides.advancements

    slide(advancements.size) {
        CompositionLocalProvider(LocalSlideSection provides SlideSection(title)) {
            val (index, adv) = advancements[advancement]
            val content = slides[index].content
            SlideScope(adv).content()
        }
    }
}