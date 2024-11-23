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

operator fun SlideDecorator.plus(other: SlideDecorator): SlideDecorator {
    val self = this
    return when (SlideDecorator.None) {
        other -> self
        self -> other
        else -> CompositeSlideDecorator(decorators = buildList {
            when (self) {
                is CompositeSlideDecorator -> addAll(self.decorators)
                else -> add(self)
            }
            when (other) {
                is CompositeSlideDecorator -> addAll(other.decorators)
                else -> add(other)
            }
        })
    }
}

private class CompositeSlideDecorator(
    val decorators: List<SlideDecorator>
) : SlideDecorator {

    @Composable
    override fun decorate(content: @Composable () -> Unit) {
        @Composable
        fun recurse(i: Int) {
            if (i == decorators.size) {
                content()
            } else {
                decorators[i].decorate { recurse(i + 1) }
            }
        }

        recurse(0)
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
        states: List<T>,
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