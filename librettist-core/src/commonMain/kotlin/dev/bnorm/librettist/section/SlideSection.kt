package dev.bnorm.librettist.section

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import dev.bnorm.librettist.show.ShowBuilder
import dev.bnorm.librettist.show.ShowBuilderDsl
import dev.bnorm.librettist.show.SlideContent

@Immutable
class SlideSection(
    val header: @Composable () -> Unit,
) {
    companion object {
        val Empty = SlideSection {}
    }
}

val LocalSlideSection = compositionLocalOf { SlideSection.Empty }

@ShowBuilderDsl
fun ShowBuilder.section(
    title: @Composable () -> Unit,
    block: ShowBuilder.() -> Unit,
) {
    val upstream = this
    val section = SlideSection(title)

    object : ShowBuilder {
        override fun slide(content: SlideContent) {
            upstream.slide {
                CompositionLocalProvider(LocalSlideSection provides section) {
                    content()
                }
            }
        }
    }.block()
}
