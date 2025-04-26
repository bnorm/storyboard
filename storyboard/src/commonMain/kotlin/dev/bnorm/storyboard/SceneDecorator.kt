package dev.bnorm.storyboard

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable

fun interface SceneDecorator {
    @Composable
    fun decorate(content: @Composable () -> Unit)

    companion object {
        val None = SceneDecorator { it() }

        fun from(vararg decorators: SceneDecorator): SceneDecorator {
            return when (decorators.size) {
                0 -> None
                1 -> decorators[0]
                else -> CompositeSceneDecorator(decorators.toList())
            }
        }
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

fun <T> SceneContent<T>.decorated(
    decorator: SceneDecorator,
): SceneContent<T> {
    return DecoratedSceneContent(this, decorator)
}

private class DecoratedSceneContent<T>(
    private val upstream: SceneContent<T>,
    private val decorator: SceneDecorator,
) : SceneContent<T> {
    @Composable
    context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
    override fun SceneScope<T>.Content() {
        decorator.decorate {
            Render(upstream)
        }
    }
}

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
        return upstream.scene(
            states = states,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            content = content.decorated(decorator)
        )
    }
}
