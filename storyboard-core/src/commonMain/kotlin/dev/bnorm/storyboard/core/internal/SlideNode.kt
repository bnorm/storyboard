package dev.bnorm.storyboard.core.internal

import androidx.compose.animation.core.SeekableTransitionState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.bnorm.storyboard.core.AdvanceDirection
import dev.bnorm.storyboard.core.Slide
import dev.bnorm.storyboard.core.SlideState
import dev.bnorm.storyboard.core.Storyboard

@Stable
internal class SlideNode<T> private constructor(
    val slideIndex: Int,
    val slide: Slide<T>,
    val states: List<SlideState<T>>,
    val frames: List<Storyboard.Frame>,
    private val prev: SlideNode<*>?,
) : Comparable<SlideNode<*>> {
    companion object {
        fun List<Slide<*>>.toNodes(): List<SlideNode<*>> {
            require(isNotEmpty()) { "cannot build nodes for empty list of slides" }
            require(first().states.isNotEmpty() && last().states.isNotEmpty()) { "first and last slide must have states" }
            val lastSlideIndex = lastIndex

            fun <T> buildNode(
                slideIndex: Int,
                slide: Slide<T>,
                prev: SlideNode<*>?,
            ): SlideNode<T> {
                return SlideNode(
                    slideIndex = slideIndex,
                    slide = slide,
                    states = buildList {
                        if (slideIndex != 0) add(SlideState.Start)
                        for (state in slide.states) {
                            add(SlideState.Value(state))
                        }
                        if (slideIndex != lastSlideIndex) add(SlideState.End)
                    },
                    frames = buildList {
                        if (slide.states.isNotEmpty()) {
                            if (slideIndex != 0) add(Storyboard.Frame(slideIndex, 0))
                            for (stateIndex in slide.states.indices) {
                                add(Storyboard.Frame(slideIndex, stateIndex))
                            }
                            if (slideIndex != lastSlideIndex) add(Storyboard.Frame(slideIndex, slide.states.lastIndex))
                        }
                    },
                    prev = prev,
                )
            }

            return buildList {
                var prev: SlideNode<*>? = null

                for ((index, slide) in this@toNodes.withIndex()) {
                    val node = buildNode(index, slide, prev)
                    add(node)
                    prev = node
                }
            }
        }
    }

    enum class AdvanceResult {
        Complete,
        Jumped,
        Incomplete,
    }

    private var next: SlideNode<*>? = null

    init {
        prev?.next = this
    }

    internal var stateIndex by mutableStateOf(SeekableTransitionState(0))
        private set

    suspend fun advance(direction: AdvanceDirection, jump: Boolean): AdvanceResult {
        val currentIndex = stateIndex.currentState
        val targetIndex = stateIndex.targetState
        if (currentIndex != targetIndex) {
            // Current and target indexes are different:
            // 1. Pick the current index based on the advancement direction.
            // 2. Switch to a jump to skip the current transition.
            return advanceFrom(
                currentIndex = when (direction) {
                    AdvanceDirection.Forward -> minOf(currentIndex, targetIndex) // Pick the earlier index.
                    AdvanceDirection.Backward -> maxOf(currentIndex, targetIndex) // Pick the latter index.
                },
                direction = direction,
                jump = true,
            )
        }

        return advanceFrom(currentIndex, direction, jump)
    }

    private suspend fun advanceFrom(currentIndex: Int, direction: AdvanceDirection, jump: Boolean): AdvanceResult {
        val newIndex = currentIndex + direction.toInt()
        if (newIndex !in states.indices) return AdvanceResult.Incomplete
        when (jump) {
            true -> stateIndex = SeekableTransitionState(newIndex)
            false -> stateIndex.animateTo(newIndex)
        }

        return when {
            direction == AdvanceDirection.Forward && states[newIndex] != SlideState.End -> AdvanceResult.Complete
            direction == AdvanceDirection.Backward && states[newIndex] != SlideState.Start -> AdvanceResult.Complete
            jump -> AdvanceResult.Jumped
            else -> return AdvanceResult.Incomplete
        }
    }

    fun jumpTo(index: Int): Boolean {
        if (index !in slide.states.indices) return false
        val newIndex = index + if (states.first() is SlideState.Start) 1 else 0
        this.stateIndex = SeekableTransitionState(newIndex)
        return true
    }

    fun next(): SlideNode<*>? {
        return next?.also {
            it.stateIndex = SeekableTransitionState(0)
        }
    }

    fun previous(): SlideNode<*>? {
        return prev?.also {
            it.stateIndex = SeekableTransitionState(it.states.lastIndex)
        }
    }

    override fun compareTo(other: SlideNode<*>): Int {
        return compareValues(slideIndex, other.slideIndex)
    }
}

private fun AdvanceDirection.toInt() = when (this) {
    AdvanceDirection.Forward -> 1
    AdvanceDirection.Backward -> -1
}
