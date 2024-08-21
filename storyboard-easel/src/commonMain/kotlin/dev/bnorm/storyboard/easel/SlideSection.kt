package dev.bnorm.storyboard.easel

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import dev.bnorm.storyboard.core.SlideDecorator
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.StoryboardBuilderDsl
import dev.bnorm.storyboard.core.decorated

@Immutable
class SlideSection(
    val title: @Composable () -> Unit,
) {
    companion object {
        val Empty = SlideSection(
            title = {},
        )

        val title: @Composable () -> Unit
            @Composable
            get() = LocalSlideSection.current.title
    }
}

private val LocalSlideSection = compositionLocalOf { SlideSection.Empty }

@StoryboardBuilderDsl
fun StoryboardBuilder.section(
    title: String,
    block: StoryboardBuilder.() -> Unit,
) {
    section(
        title = { Text(title) },
        block = block,
    )
}

@StoryboardBuilderDsl
fun StoryboardBuilder.section(
    title: @Composable () -> Unit,
    block: StoryboardBuilder.() -> Unit,
) {
    val section = SlideSection(title)
    val decorator = SlideDecorator { content ->
        CompositionLocalProvider(LocalSlideSection provides section) {
            content()
        }
    }
    decorated(decorator, block)
}
