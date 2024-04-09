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
    ) {
        var next: SlideNode? = null
        var prev: SlideNode? = null
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

    fun advance(direction: AdvanceDirection): Boolean {
        val targetState = state.targetState
        if (state.currentState != targetState) {
            when (direction) {
                AdvanceDirection.Forward -> {
                    when (targetState.state) {
                        SlideState.Entering -> nextSlide(withTransition = false)
                        SlideState.Exiting -> nextSlide(withTransition = false)
                        is SlideState.Index -> state = MutableTransitionState(targetState)
                    }
                }

                AdvanceDirection.Backward -> {
                    when (targetState.state) {
                        SlideState.Entering -> previousSlide(withTransition = false)
                        SlideState.Exiting -> previousSlide(withTransition = false)
                        is SlideState.Index -> state = MutableTransitionState(targetState)
                    }
                }
            }

            return true
        }

        state.targetState = when (direction) {
            AdvanceDirection.Forward -> targetState.next ?: return false
            AdvanceDirection.Backward -> targetState.prev ?: return false
        }

        return true
    }

    @Composable
    fun rememberAdvancementTransition(): Transition<SlideState<Int>> {
        LaunchedEffect(state.currentState, state.targetState) {
            if (state.currentState == state.targetState) {
                // Automatically advance to the next/previous slide after completing transition to
                // exiting/entering state. This slide state seamlessly transitions from
                // Index -> Exit -> Enter -> Index and the reverse.
                // TODO is there a better way to listen for this situation and make the change
                if (state.currentState.state == SlideState.Entering) {
                    previousSlide()
                } else if (state.currentState.state == SlideState.Exiting) {
                    nextSlide()
                }
            }
        }

        return rememberTransition(state).createChildTransition { it.state }
    }

    private fun previousSlide(withTransition: Boolean = true) {
        val current = state.currentState
        var previous = current.prev ?: return // No previous slide.

        if (withTransition) {
            when (previous.state) {
                SlideState.Entering -> state.targetState = previous
                SlideState.Exiting -> state = MutableTransitionState(previous).apply { targetState = previous.prev!! }
                is SlideState.Index -> state.targetState = previous
            }
        } else {
            // Jump to the previous SlideState.Index node.
            // Non-SlideState.Index nodes always have a non-null prev node.
            while (previous.state !is SlideState.Index) {
                previous = previous.prev!!
            }
            state = MutableTransitionState(previous)
        }
    }

    private fun nextSlide(withTransition: Boolean = true) {
        val current = state.currentState
        var next = current.next ?: return // No next slide.

        if (withTransition) {
            when (next.state) {
                SlideState.Entering -> state = MutableTransitionState(next).apply { targetState = next.next!! }
                SlideState.Exiting -> state.targetState = next
                is SlideState.Index -> state.targetState = next
            }
        } else {
            // Jump to the next SlideState.Index node.
            // Non-SlideState.Index nodes always have a non-null next node.
            while (next.state !is SlideState.Index) {
                next = next.next!!
            }
            state = MutableTransitionState(next)
        }
    }
}
