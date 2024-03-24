package dev.bnorm.librettist.show

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
)

val List<Slide>.states: List<Pair<Int, Int>>
    get() = flatMapIndexed { index, slide -> (0..<slide.states).map { index to it } }
