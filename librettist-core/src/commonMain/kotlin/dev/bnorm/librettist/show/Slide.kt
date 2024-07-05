package dev.bnorm.librettist.show

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Immutable

fun buildSlides(builder: ShowBuilder.() -> Unit): List<Slide> {
    val slides = buildList {
        object : ShowBuilder {
            override fun slide(
                states: Int,
                enterTransition: (AdvanceDirection) -> EnterTransition,
                exitTransition: (AdvanceDirection) -> ExitTransition,
                content: SlideContent<SlideState<Int>>,
            ) {
                require(states >= 0)
                add(Slide(states, enterTransition, exitTransition, content))
            }
        }.builder()
    }
    return slides
}

class Slide(
    val states: Int,
    val enterTransition: (AdvanceDirection) -> EnterTransition,
    val exitTransition: (AdvanceDirection) -> ExitTransition,
    val content: SlideContent<SlideState<Int>>,
) {
    @Immutable
    data class Index(
        val index: Int,
        val state: Int,
    ) : Comparable<Index> {
        override fun compareTo(other: Index): Int {
            val compare = index.compareTo(other.index)
            if (compare != 0) return compare
            return state.compareTo(other.state)
        }
    }
}

fun List<Slide>.toIndexes(): List<Slide.Index> =
    flatMapIndexed { index, slide -> (0..<slide.states).map { Slide.Index(index, it) } }
