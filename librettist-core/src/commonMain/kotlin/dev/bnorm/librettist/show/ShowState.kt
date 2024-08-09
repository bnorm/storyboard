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
    private val nodes: List<InternalSlide> = buildList {
        for ((index, slide) in slides.withIndex()) {
            add(InternalSlide(index, slide, index > 0, index < slides.size - 1))
        }

        var prev: InternalSlide? = null
        for (slide in this) {
            slide.prev = prev
            prev?.next = slide
            prev = slide
        }
    }

    // This is wrapped in a MutableState to allow setting a new MutableTransitionState when the show
    // needs to jump to a particular slide without animation or skip the current animation.
    // TODO can SeekableTransitionState eventually help?
    private var slideState by mutableStateOf(MutableTransitionState(nodes[0]))

    val currentIndex: Slide.Index
        get() = slideState.currentState.currentIndex

    fun getSlide(index: Slide.Index): SlideContent<SlideState<Int>>? {
        val slide = nodes.find { it.index == index.index } ?: return null
        return slide.slide.content
    }

    fun jumpToSlide(index: Slide.Index): Boolean {
        val slide = nodes.find { it.index == index.index } ?: return false
        if (!slide.jumpToSlide(index)) return false

        slideState = MutableTransitionState(slide)
        return true
    }

    fun advance(direction: AdvanceDirection, jump: Boolean = false): Boolean {
        if (!slideState.isIdle) {
            when (direction) {
                AdvanceDirection.Forward ->
                    skipForward(start = maxOf(slideState.currentState, slideState.targetState))

                AdvanceDirection.Backward ->
                    skipBackward(start = minOf(slideState.currentState, slideState.targetState))
            }
            return true
        }

        val currentSlide = slideState.currentState
        when (currentSlide.advance(direction, jump)) {
            true -> return true
            false -> {}
            null -> {
                when (direction) {
                    AdvanceDirection.Forward ->
                        skipForward(start = currentSlide.next ?: return false)

                    AdvanceDirection.Backward ->
                        skipBackward(start = currentSlide.prev ?: return false)
                }
                return true
            }
        }

        val targetSlide = when (direction) {
            AdvanceDirection.Forward -> currentSlide.next ?: return false
            AdvanceDirection.Backward -> currentSlide.prev ?: return false
        }
        if (jump) {
            when (direction) {
                AdvanceDirection.Forward -> targetSlide.jumpToSlide(targetSlide.firstIndex)
                AdvanceDirection.Backward -> targetSlide.jumpToSlide(targetSlide.lastIndex)
            }
            this.slideState = MutableTransitionState(targetSlide)
        } else {
            targetSlide.reset(direction)
            this.slideState.targetState = targetSlide
        }

        return true
    }

    private fun skipForward(start: InternalSlide) {
        // Jump to the next SlideState.Index.
        // Slides without states always have a non-null next slide.
        var slide = start
        while (slide.slide.states == 0) {
            slide = slide.next!!
        }
        slide.jumpToSlide(slide.firstIndex)
        slideState = MutableTransitionState(slide)
    }

    private fun skipBackward(start: InternalSlide) {
        // Jump to the previous SlideState.Index.
        // Slides without states always have a non-null prev slide.
        var slide = start
        while (slide.slide.states == 0) {
            slide = slide.prev!!
        }
        slide.jumpToSlide(slide.lastIndex)
        slideState = MutableTransitionState(slide)
    }

    @Composable
    fun Present() {
        val stableState = slideState.isIdle && slideState.currentState.currentNode.isIdle
        LaunchedEffect(stableState) {
            if (!stableState) return@LaunchedEffect
            // TODO is there a better way to listen for this situation and make the change

            // Automatically advance to the next/previous slide after completing transition to exiting/entering state.
            // This slide state seamlessly transitions from Index -> Exit -> Enter -> Index and the reverse.
            // Entering and Exiting states are always contiguous between slides based on slide node construction.
            val currentSlide = slideState.currentState
            val stateNode = currentSlide.currentNode.currentState
            when (stateNode.state) {
                SlideState.Entering -> {
                    when (currentSlide.direction) {
                        AdvanceDirection.Forward -> {
                            currentSlide.currentNode.targetState = stateNode.next!!
                        }

                        AdvanceDirection.Backward -> {
                            val prev = currentSlide.prev!!
                            prev.reset(AdvanceDirection.Backward)
                            slideState.targetState = prev
                        }
                    }
                }

                SlideState.Exiting -> {
                    when (currentSlide.direction) {
                        AdvanceDirection.Forward -> {
                            val next = currentSlide.next!!
                            next.reset(AdvanceDirection.Forward)
                            slideState.targetState = next
                        }

                        AdvanceDirection.Backward -> {
                            currentSlide.currentNode.targetState = stateNode.prev!!
                        }
                    }
                }

                is SlideState.Index -> {} // Do nothing
            }
        }

        SharedTransitionLayout {
            rememberTransition(slideState).AnimatedContent(
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
                key(node.index) {
                    val transition = rememberTransition(node.currentNode).createChildTransition { it.state }
                    val content = node.slide.content
                    SlideScope(transition, this@AnimatedContent, this@SharedTransitionLayout).content()
                }
            }
        }
    }
}

private class InternalSlide(
    val index: Int,
    val slide: Slide,
    enter: Boolean,
    exit: Boolean,
) : Comparable<InternalSlide> {
    var next: InternalSlide? = null
    var prev: InternalSlide? = null

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

    private val nodes = buildList<Node> {
        if (enter) {
            add(Node(SlideState.Entering, Slide.Index(index, 0)))
        }
        repeat(slide.states) {
            add(Node(SlideState.Index(it), Slide.Index(index, it)))
        }
        if (exit) {
            add(Node(SlideState.Exiting, Slide.Index(index, maxOf(slide.states - 1, 0))))
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

    val firstIndex: Slide.Index = nodes.first().index
    val lastIndex: Slide.Index = nodes.last().index
    val currentIndex: Slide.Index
        get() = currentNode.currentState.index

    fun reset(direction: AdvanceDirection) {
        val node = when (direction) {
            AdvanceDirection.Forward -> nodes.first()
            AdvanceDirection.Backward -> nodes.last()
        }

        this.direction = direction
        this.currentNode = MutableTransitionState(node)
    }

    fun jumpToSlide(index: Slide.Index): Boolean {
        val node = nodes.find { it.state is SlideState.Index && it.index == index } ?: return false
        currentNode = MutableTransitionState(node)
        return true
    }

    // TODO tri-state boolean => enum?
    fun advance(direction: AdvanceDirection, jump: Boolean = false): Boolean? {
        this.direction = direction

        if (!currentNode.isIdle) {
            currentNode = MutableTransitionState(
                when (direction) {
                    AdvanceDirection.Forward ->
                        navForward(start = maxOf(currentNode.currentState, currentNode.targetState)) ?: return null

                    AdvanceDirection.Backward ->
                        navBackward(start = minOf(currentNode.currentState, currentNode.targetState)) ?: return null
                }
            )

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

    private fun navForward(start: Node): Node? {
        // Jump to the next SlideState.Index node.
        var node = start
        while (node.state !is SlideState.Index) {
            node = node.next ?: return null
        }
        return node
    }

    private fun navBackward(start: Node): Node? {
        // Jump to the previous SlideState.Index node.
        var node = start
        while (node.state !is SlideState.Index) {
            node = node.prev ?: return null
        }
        return node
    }

    override fun compareTo(other: InternalSlide): Int {
        return compareValues(index, other.index)
    }

    override fun toString(): String {
        return "InternalSlide(index=$index)"
    }
}