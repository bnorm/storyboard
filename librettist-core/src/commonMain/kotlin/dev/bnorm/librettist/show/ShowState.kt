package dev.bnorm.librettist.show

import androidx.compose.runtime.mutableStateOf

fun ShowState(builder: ShowBuilder.() -> Unit): ShowState {
    return ShowState(buildSlides(builder))
}

// TODO
//   - could the slide index be driven by MutableTransitionState? and a Transition exposed through SlideScope?
//     - once SeekableTransitionState is better in multiplatform, could help skip animations naturally
class ShowState(val slides: List<Slide>) {
    private val mutableIndex = mutableStateOf(0)
    private val mutableAdvancement = mutableStateOf(0)

    val index: Int
        get() = mutableIndex.value

    val advancement: Int
        get() = mutableAdvancement.value

    val currentSlide: SlideContent<Int>
        get() = slides[mutableIndex.value].content

    fun jumpToSlide(index: Int, advancement: Int = 0) {
        require(index in 0..<slides.size)
        require(advancement in 0..<slides[index].advancements)
        mutableIndex.value = index
        mutableAdvancement.value = advancement
    }

    fun advance(advancement: Advancement): Boolean {
        fun advanceSlideIndex(): Boolean {
            val nextValue = mutableIndex.value + advancement.direction.toValue(forward = +1, backward = -1)

            val inRange = nextValue in slides.indices
            if (inRange) {
                mutableIndex.value = nextValue
                // TODO snap value?
                mutableAdvancement.value = advancement.direction.toValue(
                    forward = 0,
                    backward = slides[nextValue].advancements - 1,
                )
            }
            return inRange
        }

        fun advanceSlideAdvancement(): Boolean {
            val nextValue = mutableAdvancement.value + advancement.direction.toValue(forward = +1, backward = -1)

            val inRange = nextValue >= 0 && nextValue < slides[mutableIndex.value].advancements
            if (inRange) {
                mutableAdvancement.value = nextValue
            }
            return inRange
        }

        return advanceSlideAdvancement() || advanceSlideIndex()
    }
}
