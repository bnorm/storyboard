package dev.bnorm.librettist.show

import androidx.compose.runtime.*

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
            val (index, adv) = advancements[transition.targetState]
            val content = slides[index].content
            key(index) {
                createChildScope { adv }.content()
            }
        }
    }
}