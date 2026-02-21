package dev.bnorm.storyboard

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import kotlin.jvm.JvmField

public fun interface Decorator {
    @Composable
    public fun decorate(content: @Composable () -> Unit)

    public companion object {
        @JvmField
        public val None: Decorator = Decorator { it() }

        public fun from(vararg decorators: Decorator): Decorator {
            return when (decorators.size) {
                0 -> None
                1 -> decorators[0]
                else -> CompositeDecorator(decorators.toList())
            }
        }
    }
}

public operator fun Decorator.plus(other: Decorator): Decorator {
    val self = this
    return when (Decorator.None) {
        other -> self
        self -> other
        else -> CompositeDecorator(decorators = buildList {
            when (self) {
                is CompositeDecorator -> addAll(self.decorators)
                else -> add(self)
            }
            when (other) {
                is CompositeDecorator -> addAll(other.decorators)
                else -> add(other)
            }
        })
    }
}

private class CompositeDecorator(
    val decorators: List<Decorator>
) : Decorator {
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

public fun <T> SceneContent<T>.decorated(
    decorator: Decorator,
): SceneContent<T> {
    return DecoratedSceneContent(this, decorator)
}

private class DecoratedSceneContent<T>(
    private val upstream: SceneContent<T>,
    private val decorator: Decorator,
) : SceneContent<T> {
    @Composable
    context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
    override fun SceneScope<T>.Content() {
        decorator.decorate {
            Render(upstream)
        }
    }
}

public fun StoryboardBuilder.decorated(
    decorator: Decorator,
    block: StoryboardBuilder.() -> Unit,
) {
    DecoratedStoryboardBuilder(this, decorator).block()
}

private class DecoratedStoryboardBuilder(
    private val upstream: StoryboardBuilder,
    private val decorator: Decorator,
) : StoryboardBuilder {
    override fun <T> scene(
        frames: List<T>,
        enterTransition: SceneEnterTransition,
        exitTransition: SceneExitTransition,
        content: SceneContent<T>,
    ): Scene<T> {
        return upstream.scene(
            frames = frames,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            content = content.decorated(decorator)
        )
    }
}
