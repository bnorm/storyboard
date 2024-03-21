package dev.bnorm.librettist.show

import androidx.compose.runtime.*

fun ShowState(builder: ShowBuilder.() -> Unit): ShowState {
    return ShowState(buildSlides(builder))
}

// TODO
//   - could the slide index be driven by MutableTransitionState? and a Transition exposed through SlideScope?
//     - once SeekableTransitionState is better in multiplatform, could help skip animations naturally
class ShowState(val slides: List<Slide>) {
    private val mutableIndex = mutableIntStateOf(0)
    private val mutableAdvancement = mutableIntStateOf(0)

    val index: Int
        get() = mutableIndex.value

    val advancement: Int
        get() = mutableAdvancement.value

    val currentSlide: SlideContent
        get() = slides[mutableIndex.value].content

    private val listeners = mutableListOf<AdvancementListener>() // TODO shared flow?

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
            if (inRange) mutableAdvancement.value = nextValue
            return inRange
        }

        /*
         * We need to call handlers in different directions based on advancement direction. When advancing forward,
         * handlers should be called in natural order. When advancing backwards, handlers should be called in reverse
         * order. This is so advancement handling happens in LIFO order. When something is the last to advance forward,
         * it needs to be the first to advance backwards. This is all so multiple advancement handlers defined within
         * the same Composable function are called in the expected order when advancing, regardless of direction.
         *
         * Advancing though the slide index is always the last "handler", since it is outside the slide Composable
         * function.
         */
        val result = advanceSlideAdvancement() || advanceSlideIndex()

        listeners.forEach { it(advancement) }

        return result
    }

    fun addAdvancementListener(listener: AdvancementListener) {
        listeners.add(listener)
    }

    fun removeAdvancementListener(listener: AdvancementListener) {
        listeners.remove(listener)
    }
}

typealias AdvancementListener = (Advancement) -> Unit
