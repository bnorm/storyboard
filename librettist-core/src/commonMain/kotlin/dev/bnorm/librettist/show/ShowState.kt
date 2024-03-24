package dev.bnorm.librettist.show

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.Snapshot

fun ShowState(builder: ShowBuilder.() -> Unit): ShowState {
    return ShowState(buildSlides(builder))
}

// TODO once SeekableTransitionState is better in multiplatform, could help skip animations naturally
class ShowState(val slides: List<Slide>) {
    private val mutableIndex = mutableStateOf(0)
    private val mutableAdvancement = mutableStateOf(MutableTransitionState<SlideState<Int>>(SlideState.Index(0)))

    val index: Int
        get() = mutableIndex.value

    val advancement: Int
        get() = when (val state = mutableAdvancement.value.targetState) {
            SlideState.Entering -> 0
            SlideState.Exiting -> currentSlide().states - 1
            is SlideState.Index -> state.value
        }

    private fun currentSlide(): Slide = slides[mutableIndex.value]

    val currentSlide: SlideContent<SlideState<Int>>
        get() = currentSlide().content

    fun jumpToSlide(index: Int, advancement: Int = 0) {
        require(index in 0..<slides.size)
        require(advancement in 0..<slides[index].states)
        mutableIndex.value = index
        mutableAdvancement.value = MutableTransitionState(SlideState.Index(advancement))
    }

    fun advance(advancement: Advancement): Boolean {
        val states = currentSlide().states
        val state = mutableAdvancement.value
        val targetState = state.targetState

        if (state.currentState != targetState) {
            // Seek to the next slide/advancement
            when (targetState) {
                SlideState.Entering -> previousSlide(withTransition = false)
                SlideState.Exiting -> nextSlide(withTransition = false)
                is SlideState.Index -> mutableAdvancement.value = MutableTransitionState(targetState)
            }

            return true
        }

        val newTargetState = when (advancement.direction) {
            Advancement.Direction.Forward -> targetState.next(states)
            Advancement.Direction.Backward -> targetState.previous(states)
        }

        if (
            state.targetState != newTargetState && when (newTargetState) {
                SlideState.Entering -> mutableIndex.value > 0
                SlideState.Exiting -> mutableIndex.value < slides.size - 1
                is SlideState.Index -> true
            }
        ) {
            state.targetState = newTargetState
            return true
        }

        return false
    }

    @Composable
    fun rememberAdvancementTransition(): Transition<SlideState<Int>> {
        val state by mutableAdvancement
        val transition = rememberTransition(state)

        LaunchedEffect(state.currentState) {
            if (state.currentState == state.targetState) {
                if (state.currentState == SlideState.Entering) {
                    previousSlide()
                } else if (state.currentState == SlideState.Exiting) {
                    nextSlide()
                }
            }
        }

        return transition
    }

    private fun previousSlide(withTransition: Boolean = true) {
        if (mutableIndex.value <= 0) return // No previous slide

        Snapshot.withMutableSnapshot {
            val nextValue = mutableIndex.value - 1
            mutableIndex.value = nextValue
            val nextSlide = slides[nextValue]

            val targetState = SlideState.Exiting.previous(nextSlide.states)
            val nextState = MutableTransitionState(if (withTransition) SlideState.Exiting else targetState)
            nextState.targetState = targetState
            mutableAdvancement.value = nextState
        }
    }

    private fun nextSlide(withTransition: Boolean = true) {
        if (mutableIndex.value >= slides.size - 1) return // No next slide

        Snapshot.withMutableSnapshot {
            val nextValue = mutableIndex.value + 1
            mutableIndex.value = nextValue
            val nextSlide = slides[nextValue]


            val targetState = SlideState.Entering.next(nextSlide.states)
            val nextState = MutableTransitionState(if (withTransition) SlideState.Entering else targetState)
            nextState.targetState = targetState
            mutableAdvancement.value = nextState
        }
    }

    private fun SlideState<Int>.next(states: Int = currentSlide().states): SlideState<Int> {
        val nextIndex = when (this) {
            SlideState.Entering -> 0
            SlideState.Exiting -> return this
            is SlideState.Index -> value + 1
        }

        if (nextIndex >= states) return SlideState.Exiting

        return SlideState.Index(nextIndex)
    }

    private fun SlideState<Int>.previous(states: Int = currentSlide().states): SlideState<Int> {
        val nextIndex = when (this) {
            SlideState.Entering -> return this
            SlideState.Exiting -> states - 1
            is SlideState.Index -> value - 1
        }

        if (nextIndex < 0) return SlideState.Entering

        return SlideState.Index(nextIndex)
    }
}
