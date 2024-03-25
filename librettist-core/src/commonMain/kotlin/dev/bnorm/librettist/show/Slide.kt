package dev.bnorm.librettist.show

import androidx.compose.runtime.Immutable

fun buildSlides(builder: ShowBuilder.() -> Unit): List<Slide> {
    val slides = buildList {
        object : ShowBuilder {
            override fun slide(states: Int, content: SlideContent<SlideState<Int>>) {
                require(states >= 0)
                add(Slide(states, content))
            }
        }.builder()
    }
    return slides
}

class Slide(
    val states: Int,
    val content: SlideContent<SlideState<Int>>,
) {
    @Immutable
    data class Index(
        val index: Int,
        val state: Int,
    )
}

val List<Slide>.indices: List<Slide.Index>
    get() = flatMapIndexed { index, slide -> (0..<slide.states).map { Slide.Index(index, it) } }
