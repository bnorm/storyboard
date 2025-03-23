package dev.bnorm.storyboard.core

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Stable
class Storyboard private constructor(
    val title: String,
    val description: String?,
    val slides: ImmutableList<Slide<*>>,
    val size: DpSize = DEFAULT_SIZE,
    val decorator: SlideDecorator = SlideDecorator.None,
) {
    @Immutable
    data class Frame(
        val slideIndex: Int,
        val stateIndex: Int,
    ) : Comparable<Frame> {
        override fun compareTo(other: Frame): Int {
            val cmp = compareValues(slideIndex, other.slideIndex)
            if (cmp != 0) return cmp
            return compareValues(stateIndex, other.stateIndex)
        }
    }

    companion object {
        val DEFAULT_SIZE = DpSize(960.dp, 540.dp)

        fun build(
            title: String,
            description: String? = null,
            size: DpSize = DEFAULT_SIZE,
            decorator: SlideDecorator = SlideDecorator.None,
            block: StoryboardBuilder.() -> Unit,
        ): Storyboard {
            val builder = StoryboardBuilderImpl()
            builder.block()
            return Storyboard(title, description, builder.build().toImmutableList(), size, decorator)
        }
    }

    val frames: ImmutableList<Frame> = slides.flatMapIndexed { slideIndex, slide ->
        List(slide.states.size) { stateIndex -> Frame(slideIndex, stateIndex) }
    }.toImmutableList()
}

private class StoryboardBuilderImpl : StoryboardBuilder {
    private val slides = mutableListOf<Slide<*>>()

    override fun <T> slide(
        states: List<T>,
        enterTransition: (AdvanceDirection) -> EnterTransition,
        exitTransition: (AdvanceDirection) -> ExitTransition,
        content: SlideContent<T>,
    ): Slide<T> {
        val slide = Slide(
            states = states.toImmutableList(),
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            content = content,
        )
        slides.add(slide)
        return slide
    }

    fun build(): List<Slide<*>> {
        return slides
    }
}
