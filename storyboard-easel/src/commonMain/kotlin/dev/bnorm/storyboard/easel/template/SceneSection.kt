package dev.bnorm.storyboard.easel.template

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.StoryboardBuilderDsl
import dev.bnorm.storyboard.decorated

@Immutable
class SceneSection(
    val title: @Composable () -> Unit,
) {
    companion object {
        val Empty = SceneSection(
            title = {},
        )

        val current: SceneSection
            @Composable
            get() = LocalSceneSection.current

        val title: @Composable () -> Unit
            @Composable
            get() = LocalSceneSection.current.title
    }
}

private val LocalSceneSection = compositionLocalOf { SceneSection.Empty }

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
    val section = SceneSection(title)
    val decorator = SceneDecorator { content ->
        CompositionLocalProvider(LocalSceneSection provides section) {
            content()
        }
    }
    decorated(decorator, block)
}
