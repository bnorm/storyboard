package dev.bnorm.librettist.show

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.core.rememberTransition
import androidx.compose.runtime.Composable

fun ShowState(builder: ShowBuilder.() -> Unit): ShowState {
    return ShowState(buildSlides(builder))
}

// TODO
//   - could the slide index be driven by MutableTransitionState? and a Transition exposed through SlideScope?
//     - once SeekableTransitionState is better in multiplatform, could help skip animations naturally
class ShowState(val slides: List<Slide>) {
    private val mutableIndex = MutableTransitionState(0)
    private val mutableAdvancement = MutableTransitionState(0)

    val index: Int
        get() = mutableIndex.targetState

    val advancement: TransitionState<Int>
        get() = mutableAdvancement

    val currentSlide: SlideContent
        get() = slides[mutableIndex.targetState].content

    fun jumpToSlide(index: Int, advancement: Int = 0) {
        require(index in 0..<slides.size)
        require(advancement in 0..<slides[index].advancements)
        mutableIndex.targetState = index
        mutableAdvancement.targetState = advancement
    }

    fun advance(advancement: Advancement): Boolean {
        fun advanceSlideIndex(): Boolean {
            val nextValue = mutableIndex.targetState + advancement.direction.toValue(forward = +1, backward = -1)

            val inRange = nextValue in slides.indices
            if (inRange) {
                mutableIndex.targetState = nextValue
                // TODO snap value?
                mutableAdvancement.targetState = advancement.direction.toValue(
                    forward = 0,
                    backward = slides[nextValue].advancements - 1,
                )
            }
            return inRange
        }

        fun advanceSlideAdvancement(): Boolean {
            val nextValue = mutableAdvancement.targetState + advancement.direction.toValue(forward = +1, backward = -1)

            val inRange = nextValue >= 0 && nextValue < slides[mutableIndex.targetState].advancements
            if (inRange) {
                mutableAdvancement.targetState = nextValue
            }
            return inRange
        }

        return advanceSlideAdvancement() || advanceSlideIndex()
    }

    @Composable
    fun rememberAdvancementTransition(): Transition<Int> {
        return rememberTransition(mutableAdvancement)
    }
}
