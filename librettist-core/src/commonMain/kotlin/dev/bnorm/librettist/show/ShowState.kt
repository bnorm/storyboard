package dev.bnorm.librettist.show

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

fun ShowState(builder: ShowBuilder.() -> Unit): ShowState {
    return ShowState(buildSlides(builder))
}

// TODO once SeekableTransitionState is better in multiplatform, could help skip animations naturally
class ShowState(val slides: List<Slide>) {
    private val mutableIndex = mutableStateOf(0)
    private val mutableAdvancement = mutableStateOf(MutableTransitionState(0))

    val index: Int
        get() = mutableIndex.value

    val advancement: Int
        get() = mutableAdvancement.value.targetState

    private fun currentSlide(): Slide = slides[mutableIndex.value]

    val currentSlide: SlideContent<Int>
        get() = currentSlide().content

    fun jumpToSlide(index: Int, advancement: Int = 0) {
        require(index in 0..<slides.size)
        require(advancement in 0..<slides[index].advancements)
        mutableIndex.value = index
        mutableAdvancement.value = MutableTransitionState(advancement)
    }

    fun advance(advancement: Advancement): Boolean {
        val advancements = currentSlide().advancements
        val state = mutableAdvancement.value
        val targetState = state.targetState

        if (state.currentState != targetState) {
            // Seek to the next slide/advancement
            when (targetState) {
                -1 -> previousSlide(withTransition = false)
                advancements -> nextSlide(withTransition = false)
                else -> mutableAdvancement.value = MutableTransitionState(targetState)
            }

            return true
        }

        val newTargetState = targetState + advancement.direction.toValue(forward = +1, backward = -1)
        if (
            newTargetState in 0..<advancements || // Advance within slide
            newTargetState == -1 && mutableIndex.value > 0 || // Advance to previous slide
            newTargetState == advancements && mutableIndex.value < slides.size - 1 // Advance to next slide
        ) {
            state.targetState = newTargetState
            return true
        }

        return false
    }

    @Composable
    fun rememberAdvancementTransition(): Transition<Int> {
        val state by mutableAdvancement
        val transition = rememberTransition(state)

        LaunchedEffect(state.currentState) {
            if (state.currentState == state.targetState) {
                if (state.currentState == -1) {
                    previousSlide()
                } else if (state.currentState == currentSlide().advancements) {
                    nextSlide()
                }
            }
        }

        return transition
    }

    private fun previousSlide(withTransition: Boolean = true) {
        if (mutableIndex.value <= 0) return // No previous slide

        val nextValue = mutableIndex.value - 1
        mutableIndex.value = nextValue

        val advancements = slides[nextValue].advancements
        val nextState = MutableTransitionState(if (withTransition) advancements else advancements - 1)
        nextState.targetState = advancements - 1
        mutableAdvancement.value = nextState
    }

    private fun nextSlide(withTransition: Boolean = true) {
        if (mutableIndex.value >= slides.size - 1) return // No next slide

        val nextValue = mutableIndex.value + 1
        mutableIndex.value = nextValue

        val nextState = MutableTransitionState(if (withTransition) -1 else 0)
        nextState.targetState = 0
        mutableAdvancement.value = nextState
    }
}
