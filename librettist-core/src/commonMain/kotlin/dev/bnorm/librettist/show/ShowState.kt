package dev.bnorm.librettist.show

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.*

fun ShowState(builder: ShowBuilder.() -> Unit): ShowState {
    return ShowState(buildSlides(builder))
}

@Stable
class ShowState(val slides: List<Slide>) {
    private class Node(
        val slide: Slide,
        val state: InternalSlideState,
        val index: Int,
    ) : Comparable<Node> {
        var next: Node? = null
        var prev: Node? = null

        override fun compareTo(other: Node): Int {
            return compareValues(index, other.index)
        }
    }

    private val nodes: List<Node> = buildList {
        for ((index, slide) in slides.withIndex()) {
            add(Node(slide, InternalSlideState(index, slide, index > 0, index < slides.size - 1), index))
        }

        var prev: Node? = null
        for (node in this) {
            node.prev = prev
            prev?.next = node
            prev = node
        }
    }

    // This is wrapped in a MutableState to allow setting a new MutableTransitionState when the show
    // needs to jump to a particular SlideNode without animation or skip the current animation.
    // TODO can SeekableTransitionState eventually help?
    private var currentNode by mutableStateOf(MutableTransitionState(nodes[0]))

    val index: Slide.Index
        get() = currentNode.currentState.state.currentIndex

    fun getSlide(index: Slide.Index): SlideContent<SlideState<Int>>? {
        val node = nodes.find { it.index == index.index } ?: return null
        return node.slide.content
    }

    fun jumpToSlide(index: Slide.Index) {
        val node = nodes.find { it.index == index.index } ?: return
        node.state.jumpToSlide(index)
        currentNode = MutableTransitionState(node)
    }

    fun advance(direction: AdvanceDirection, jump: Boolean = false): Boolean {
        val targetState = currentNode.targetState
        if (!currentNode.isIdle) {
            skip(direction)
            return true
        }

        when (targetState.state.advance(direction, jump)) {
            true -> return true
            false -> {}
            null -> {
                skip(direction)
                return true
            }
        }

        val nextNode = when (direction) {
            AdvanceDirection.Forward -> targetState.next ?: return false
            AdvanceDirection.Backward -> targetState.prev ?: return false
        }
        if (jump) {
            when (direction) {
                AdvanceDirection.Forward -> nextNode.state.jumpToSlide(nextNode.state.firstIndex)
                AdvanceDirection.Backward -> nextNode.state.jumpToSlide(nextNode.state.lastIndex)
            }
            currentNode = MutableTransitionState(nextNode)
        } else {
            nextNode.state.reset(direction)
            currentNode.targetState = nextNode
        }

        return true
    }

    private fun skip(direction: AdvanceDirection) {
        when (direction) {
            AdvanceDirection.Forward -> {
                var next = maxOf(currentNode.currentState, currentNode.targetState)
                // Jump to the next SlideState.Index node.
                // Non-SlideState.Index nodes always have a non-null next node.
                while (next.state.nodes.all { it.state !is SlideState.Index }) {
                    next = next.next!!
                }
                next.state.jumpToSlide(next.state.firstIndex)
                currentNode = MutableTransitionState(next)
            }

            AdvanceDirection.Backward -> {
                var previous = minOf(currentNode.currentState, currentNode.targetState)
                // Jump to the previous SlideState.Index node.
                // Non-SlideState.Index nodes always have a non-null prev node.
                while (previous.state.nodes.all { it.state !is SlideState.Index }) {
                    previous = previous.prev!!
                }
                previous.state.jumpToSlide(previous.state.lastIndex)
                currentNode = MutableTransitionState(previous)
            }
        }
    }

    @Composable
    fun Present() {
        val slideNode = currentNode.currentState
        val slideState = slideNode.state
        val stableState = currentNode.isIdle && slideState.currentNode.isIdle
        LaunchedEffect(stableState) {
            if (!stableState) return@LaunchedEffect
            // TODO is there a better way to listen for this situation and make the change

            // Automatically advance to the next/previous slide after completing transition to exiting/entering state.
            // This slide state seamlessly transitions from Index -> Exit -> Enter -> Index and the reverse.
            // Entering and Exiting states are always contiguous between slides based on slide node construction.
            val stateNode = slideState.currentNode.currentState
            when (stateNode.state) {
                SlideState.Entering -> {
                    when (slideState.direction) {
                        AdvanceDirection.Forward -> {
                            slideNode.state.currentNode.targetState = stateNode.next!!
                        }

                        AdvanceDirection.Backward -> {
                            val prev = slideNode.prev!!
                            prev.state.reset(AdvanceDirection.Backward)
                            currentNode.targetState = prev
                        }
                    }
                }

                SlideState.Exiting -> {
                    when (slideState.direction) {
                        AdvanceDirection.Forward -> {
                            val next = slideNode.next!!
                            next.state.reset(AdvanceDirection.Forward)
                            currentNode.targetState = next
                        }

                        AdvanceDirection.Backward -> {
                            slideNode.state.currentNode.targetState = stateNode.prev!!
                        }
                    }
                }

                is SlideState.Index -> {} // Do nothing
            }
        }

        SharedTransitionLayout {
            rememberTransition(currentNode).AnimatedContent(
                transitionSpec = {
                    if (targetState > initialState) {
                        targetState.slide.enterTransition(AdvanceDirection.Forward) togetherWith
                                initialState.slide.exitTransition(AdvanceDirection.Forward)
                    } else {
                        targetState.slide.enterTransition(AdvanceDirection.Backward) togetherWith
                                initialState.slide.exitTransition(AdvanceDirection.Backward)
                    }
                }
            ) { node ->
                val content = node.slide.content
                key(content) {
                    val stateTransition = rememberTransition(node.state.currentNode).createChildTransition { it.state }
                    SlideScope(stateTransition, this@AnimatedContent, this@SharedTransitionLayout).content()
                }
            }
        }
    }
}

private class InternalSlideState(val index: Int, val slide: Slide, enter: Boolean, exit: Boolean) {
    class Node(
        val state: SlideState<Int>,
        val index: Slide.Index,
    ) : Comparable<Node> {
        var next: Node? = null
        var prev: Node? = null

        override fun compareTo(other: Node): Int {
            return state.compareTo(other.state)
        }
    }

    val nodes = buildList<Node> {
        if (enter) {
            add(Node(SlideState.Entering, Slide.Index(index, 0)))
        }
        repeat(slide.states) {
            add(Node(SlideState.Index(it), Slide.Index(index, it)))
        }
        if (exit) {
            add(Node(SlideState.Exiting, Slide.Index(index, slide.states - 1)))
        }

        var prev: Node? = null
        for (node in this) {
            node.prev = prev
            prev?.next = node
            prev = node
        }
    }

    var currentNode by mutableStateOf(MutableTransitionState(nodes[0]))
    var direction by mutableStateOf(AdvanceDirection.Forward)

    val currentIndex: Slide.Index
        get() = currentNode.currentState.index

    val firstIndex: Slide.Index
        get() = nodes.first().index

    val lastIndex: Slide.Index
        get() = nodes.last().index

    fun reset(direction: AdvanceDirection) {
        this.direction = direction
        this.currentNode = when (direction) {
            AdvanceDirection.Forward -> {
                MutableTransitionState(nodes.first())
            }

            AdvanceDirection.Backward -> {
                MutableTransitionState(nodes.last())
            }
        }
    }

    fun jumpToSlide(index: Slide.Index) {
        val node = nodes.find { it.state is SlideState.Index && it.index == index } ?: return
        currentNode = MutableTransitionState(node)
    }

    // TODO tri-state boolean => enum?
    fun advance(direction: AdvanceDirection, jump: Boolean = false): Boolean? {
        this.direction = direction

        if (!currentNode.isIdle) {
            currentNode = when (direction) {
                AdvanceDirection.Forward -> {
                    var next = maxOf(currentNode.currentState, currentNode.targetState)
                    // Jump to the next SlideState.Index node.
                    while (next.state !is SlideState.Index) {
                        next = next.next ?: return null
                    }
                    MutableTransitionState(next)
                }

                AdvanceDirection.Backward -> {
                    var previous = minOf(currentNode.currentState, currentNode.targetState)
                    // Jump to the previous SlideState.Index node.
                    while (previous.state !is SlideState.Index) {
                        previous = previous.prev ?: return null
                    }
                    MutableTransitionState(previous)
                }
            }

            return true
        }

        val nextState = when (direction) {
            AdvanceDirection.Forward -> currentNode.targetState.next ?: return false
            AdvanceDirection.Backward -> currentNode.targetState.prev ?: return false
        }
        if (jump) {
            currentNode = MutableTransitionState(nextState)
        } else {
            this.currentNode.targetState = nextState
        }

        return true
    }
}