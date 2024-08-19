package dev.bnorm.storyboard.core

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable

fun interface SlideDecorator {
    @Composable
    fun decorate(content: @Composable () -> Unit)

    companion object {
        val None = SlideDecorator { it() }
    }
}

@StoryboardBuilderDsl
fun StoryboardBuilder.decorated(
    decorator: SlideDecorator,
    block: StoryboardBuilder.() -> Unit,
) {
    DecoratedStoryboardBuilder(this, decorator).block()
}

private class DecoratedStoryboardBuilder(
    private val upstream: StoryboardBuilder,
    private val decorator: SlideDecorator,
) : StoryboardBuilder {
    override fun <T> slide(
        states: List<Slide.State<T>>,
        enterTransition: (AdvanceDirection) -> EnterTransition,
        exitTransition: (AdvanceDirection) -> ExitTransition,
        content: SlideContent<T>,
    ): Slide<T> {
        return upstream.slide(states, enterTransition, exitTransition) {
            decorator.decorate {
                content()
            }
        }
    }
}