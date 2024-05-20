package dev.bnorm.librettist.show

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.runtime.*

fun ShowState(builder: ShowBuilder.() -> Unit): ShowState {
    return ShowState(buildSlides(builder))
}

@Stable
class ShowState(val slides: List<Slide>) {
    private class SlideNode(
        val content: SlideContent<SlideState<Int>>,
        val state: SlideState<Int>,
        val index: Slide.Index,
    ) : Comparable<SlideNode> {
        var next: SlideNode? = null
        var prev: SlideNode? = null

        override fun compareTo(other: SlideNode): Int {
            val compare = compareValues(index, other.index)
            if (compare != 0) return compare
            return state.compareTo(other.state)
        }
    }

    private val nodes: List<SlideNode> = buildList {
        for ((index, slide) in slides.withIndex()) {
            if (index > 0) {
                add(SlideNode(slide.content, SlideState.Entering, Slide.Index(index, 0)))
            }
            repeat(slide.states) {
                add(SlideNode(slide.content, SlideState.Index(it), Slide.Index(index, it)))
            }
            if (index < slides.size - 1) {
                add(SlideNode(slide.content, SlideState.Exiting, Slide.Index(index, slide.states - 1)))
            }
        }

        var prev: SlideNode? = null
        for (node in this) {
            node.prev = prev
            prev?.next = node
            prev = node
        }
    }

    // This is wrapped in a MutableState to allow setting a new MutableTransitionState when the show
    // needs to jump to a particular SlideNode without animation or skip the current animation.
    // TODO can SeekableTransitionState eventually help?
    private var state by mutableStateOf(MutableTransitionState(nodes[0]))

    val index: Slide.Index
        get() = state.currentState.index

    val currentSlide: SlideContent<SlideState<Int>>
        get() = state.currentState.content

    fun isShowing(index: Slide.Index): Boolean {
        return state.currentState.index == index
    }

    fun jumpToSlide(index: Slide.Index) {
        val node = nodes.find { it.state is SlideState.Index && it.index == index } ?: return
        state = MutableTransitionState(node)
    }

    fun getSlide(index: Slide.Index): SlideContent<SlideState<Int>>? {
        val node = nodes.find { it.state is SlideState.Index && it.index == index } ?: return null
        return node.content
    }

    fun advance(direction: AdvanceDirection, jump: Boolean = false): Boolean {
        val targetState = state.targetState
        val currentState = state.currentState
        if (currentState != targetState) {
            when (direction) {
                AdvanceDirection.Forward -> {
                    var next = maxOf(currentState, targetState)
                    // Jump to the next SlideState.Index node.
                    // Non-SlideState.Index nodes always have a non-null next node.
                    while (next.state !is SlideState.Index) {
                        next = next.next!!
                    }
                    state = MutableTransitionState(next)
                }

                AdvanceDirection.Backward -> {
                    var previous = minOf(currentState, targetState)
                    // Jump to the previous SlideState.Index node.
                    // Non-SlideState.Index nodes always have a non-null prev node.
                    while (previous.state !is SlideState.Index) {
                        previous = previous.prev!!
                    }
                    state = MutableTransitionState(previous)
                }
            }

            return true
        }

        val nextState = when (direction) {
            AdvanceDirection.Forward -> targetState.next ?: return false
            AdvanceDirection.Backward -> targetState.prev ?: return false
        }
        if (jump) {
            state = MutableTransitionState(nextState)
        } else {
            state.targetState = nextState
        }

        return true
    }

    @Composable
    fun rememberAdvancementTransition(): Transition<SlideState<Int>> {
        val current = state.currentState
        val stableState = current == state.targetState
        LaunchedEffect(stableState) {
            // TODO is there a better way to listen for this situation and make the change

            // Automatically advance to the next/previous slide after completing transition to exiting/entering state.
            // This slide state seamlessly transitions from Index -> Exit -> Enter -> Index and the reverse.
            // Entering and Exiting states are always contiguous between slides based on slide node construction.
            if (stableState && current.state == SlideState.Entering) {
                val previous = current.prev!!
                require(previous.state is SlideState.Exiting)
                state = MutableTransitionState(previous).apply { targetState = previous.prev!! }
            } else if (stableState && current.state == SlideState.Exiting) {
                val next = current.next!!
                require(next.state is SlideState.Entering)
                state = MutableTransitionState(next).apply { targetState = next.next!! }
            }
        }

        return rememberTransition(state).createChildTransition { it.state }
    }
}
