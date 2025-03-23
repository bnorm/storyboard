package dev.bnorm.storyboard.core

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable

fun interface SceneDecorator {
    @Composable
    fun decorate(content: @Composable () -> Unit)

    companion object {
        val None = SceneDecorator { it() }
    }
}

operator fun SceneDecorator.plus(other: SceneDecorator): SceneDecorator {
    val self = this
    return when (SceneDecorator.None) {
        other -> self
        self -> other
        else -> CompositeSceneDecorator(decorators = buildList {
            when (self) {
                is CompositeSceneDecorator -> addAll(self.decorators)
                else -> add(self)
            }
            when (other) {
                is CompositeSceneDecorator -> addAll(other.decorators)
                else -> add(other)
            }
        })
    }
}

private class CompositeSceneDecorator(
    val decorators: List<SceneDecorator>
) : SceneDecorator {

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
    decorator: SceneDecorator,
    block: StoryboardBuilder.() -> Unit,
) {
    DecoratedStoryboardBuilder(this, decorator).block()
}

private class DecoratedStoryboardBuilder(
    private val upstream: StoryboardBuilder,
    private val decorator: SceneDecorator,
) : StoryboardBuilder {
    override fun <T> scene(
        states: List<T>,
        enterTransition: (AdvanceDirection) -> EnterTransition,
        exitTransition: (AdvanceDirection) -> ExitTransition,
        content: SceneContent<T>,
    ): Scene<T> {
        return upstream.scene(states, enterTransition, exitTransition) {
            decorator.decorate {
                content()
            }
        }
    }
}