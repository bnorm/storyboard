package dev.bnorm.storyboard.core

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.SeekableTransitionState
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.core.internal.SlideNode
import dev.bnorm.storyboard.core.internal.SlideNode.Companion.toNodes
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

@Stable
class Storyboard private constructor(
    val title: String,
    val description: String?,
    val slides: ImmutableList<Slide<*>>,
    val size: DpSize = DEFAULT_SIZE,
    val decorator: SlideDecorator = SlideDecorator.None,
) {
    @Immutable
    data class Frame(
        val slideIndex: Int,
        val stateIndex: Int,
    ) : Comparable<Frame> {
        override fun compareTo(other: Frame): Int {
            val cmp = compareValues(slideIndex, other.slideIndex)
            if (cmp != 0) return cmp
            return compareValues(stateIndex, other.stateIndex)
        }
    }

    companion object {
        val DEFAULT_SIZE = DpSize(960.dp, 540.dp)

        fun build(
            title: String,
            description: String? = null,
            size: DpSize = DEFAULT_SIZE,
            decorator: SlideDecorator = SlideDecorator.None,
            block: StoryboardBuilder.() -> Unit,
        ): Storyboard {
            val builder = StoryboardBuilderImpl()
            builder.block()
            return Storyboard(title, description, builder.build().toImmutableList(), size, decorator)
        }
    }

    val frames: ImmutableList<Frame> = slides.flatMapIndexed { slideIndex, slide ->
        List(slide.states.size) { stateIndex -> Frame(slideIndex, stateIndex) }
    }.toImmutableList()

    private val nodes = slides.toNodes()
    internal var node by mutableStateOf(SeekableTransitionState(nodes.first()))
        private set

    var direction by mutableStateOf(AdvanceDirection.Forward)
        private set

    val currentFrame: Frame
        get() = node.currentState.let { it.frames[it.stateIndex.currentState] }

    val advancementProgress: Float
        get() = node.fraction.takeIf { it != 0.0f }
            ?: node.currentState.stateIndex.fraction.takeIf { it != 0.0f }
            ?: 1f

    fun advance(direction: AdvanceDirection, jump: Boolean = false): Boolean =
        events.tryEmit(Event.Advance(direction, jump))

    fun jumpTo(frame: Frame): Boolean {
        if (frame.slideIndex !in nodes.indices) return false

        val newNode = nodes[frame.slideIndex]
        if (!newNode.jumpTo(frame.stateIndex)) return false

        val targetState = node.targetState
        this.direction = when (newNode == targetState) {
            true -> (frame.stateIndex > targetState.stateIndex.currentState).toDirection()
            false -> (newNode.slideIndex > targetState.slideIndex).toDirection()
        }

        node = SeekableTransitionState(newNode)
        return true
    }

    private sealed class Event {
        data class Advance(val direction: AdvanceDirection, val jump: Boolean) : Event()
    }

    internal suspend fun handleEvents() {
        events.collectLatest { event ->
            when (event) {
                is Event.Advance -> eventAdvance(event.direction, event.jump)
            }
        }
    }

    private val events = MutableSharedFlow<Event>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    private suspend fun eventAdvance(direction: AdvanceDirection, jump: Boolean) {
        this.direction = direction
        var shouldSnap = false

        val currentState = node.currentState
        val targetState = node.targetState
        if (currentState != targetState) {
            // Current and target nodes are different:
            // 1. Pick the current node based on the advancement direction.
            // 2. Switch to a jump to skip the current transition.
            val newNode = when (direction) {
                AdvanceDirection.Forward -> minOf(currentState, targetState).next() ?: return
                AdvanceDirection.Backward -> maxOf(currentState, targetState).previous() ?: return
            }
            node.snapTo(newNode)
            shouldSnap = true
        }

        while (true) {
            // Advance the current node to the next state.
            val currentNode = node.currentState
            when (currentNode.advance(direction, shouldSnap)) {
                // Advancement is complete.
                SlideNode.AdvanceResult.Complete -> return

                // Advancement requires node transition as well.
                SlideNode.AdvanceResult.Jumped -> shouldSnap = true
                SlideNode.AdvanceResult.Incomplete -> {}
            }

            // Advance the storyboard to the next node.
            val newNode = when (direction) {
                AdvanceDirection.Forward -> currentNode.next() ?: return
                AdvanceDirection.Backward -> currentNode.previous() ?: return
            }
            when (shouldSnap) {
                true -> node.snapTo(newNode)
                false -> node.animateTo(newNode)
            }
        }
    }

    private fun Boolean.toDirection(): AdvanceDirection = when (this) {
        true -> AdvanceDirection.Forward
        false -> AdvanceDirection.Backward
    }
}

private class StoryboardBuilderImpl : StoryboardBuilder {
    private val slides = mutableListOf<Slide<*>>()

    override fun <T> slide(
        states: List<T>,
        enterTransition: (AdvanceDirection) -> EnterTransition,
        exitTransition: (AdvanceDirection) -> ExitTransition,
        content: SlideContent<T>,
    ): Slide<T> {
        val slide = Slide(
            states = states.toImmutableList(),
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            content = content,
        )
        slides.add(slide)
        return slide
    }

    fun build(): List<Slide<*>> {
        return slides
    }
}
