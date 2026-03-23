package dev.bnorm.storyboard

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import kotlin.jvm.JvmField

public fun interface ContentDecorator {
    @Composable
    public fun decorate(content: @Composable () -> Unit)

    public companion object {
        @JvmField
        public val None: ContentDecorator = ContentDecorator { it() }

        public fun from(vararg decorators: ContentDecorator): ContentDecorator {
            val decoratorList = buildList {
                for (decorator in decorators) {
                    when (decorator) {
                        None -> Unit
                        is CompositeContentDecorator -> addAll(decorator.decorators)
                        else -> add(decorator)
                    }
                }
            }
            return when (decoratorList.size) {
                0 -> None
                1 -> decoratorList[0]
                else -> CompositeContentDecorator(decoratorList)
            }
        }
    }
}

public operator fun ContentDecorator.plus(other: ContentDecorator): ContentDecorator {
    val self = this
    return when (ContentDecorator.None) {
        other -> self
        self -> other
        else -> CompositeContentDecorator(buildList {
            when (self) {
                is CompositeContentDecorator -> addAll(self.decorators)
                else -> add(self)
            }
            when (other) {
                is CompositeContentDecorator -> addAll(other.decorators)
                else -> add(other)
            }
        })
    }
}

private class CompositeContentDecorator(
    val decorators: List<ContentDecorator>
) : ContentDecorator {
    init {
        require(decorators.size > 1) { "Must be more than one decorator" }
        require(decorators.all { it != ContentDecorator.None && it !is CompositeContentDecorator }) {
            "Decorators must not contain ContentDecorator.None or CompositeContentDecorator"
        }
    }

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
    decorator: ContentDecorator,
): SceneContent<T> {
    return DecoratedSceneContent(this, decorator)
}

private class DecoratedSceneContent<T>(
    private val upstream: SceneContent<T>,
    private val decorator: ContentDecorator,
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
    decorator: ContentDecorator,
    block: StoryboardBuilder.() -> Unit,
) {
    DecoratedStoryboardBuilder(this, decorator).block()
}

private class DecoratedStoryboardBuilder(
    private val upstream: StoryboardBuilder,
    private val decorator: ContentDecorator,
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
