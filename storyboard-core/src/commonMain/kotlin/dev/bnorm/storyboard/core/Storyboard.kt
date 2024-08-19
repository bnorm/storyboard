package dev.bnorm.storyboard.core

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.Snapshot.Companion.withMutableSnapshot
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Stable
class Storyboard private constructor(
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
        val DEFAULT_SIZE = DpSize(960.dp, 540.dp) // DpSize(1920.dp, 1080.dp)

        fun build(
            size: DpSize = DEFAULT_SIZE,
            decorator: SlideDecorator = SlideDecorator.None,
            block: StoryboardBuilder.() -> Unit,
        ): Storyboard {
            val slides = persistentListOf<LinkedSlide<*>>().builder()
            StoryboardBuilderImpl(slides).block()
            return Storyboard(slides.build(), size, decorator)
        }
    }

    val frames: ImmutableList<Frame> = slides.flatMapIndexed { slideIndex, slide ->
        List(slide.states.size) { stateIndex -> Frame(slideIndex, stateIndex) }
    }.toImmutableList()

    internal var node by mutableStateOf(MutableTransitionState(slides.first() as LinkedSlide<*>))
        private set

    var direction by mutableStateOf(AdvanceDirection.Forward)
        private set

    val currentFrame: Frame get() = node.currentState.let { Frame(it.slideIndex, it.currentIndex) }

    fun advance(direction: AdvanceDirection, jump: Boolean = false): Boolean = withMutableSnapshot {
        this.direction = direction

        if (node.targetState.advance(direction)) {
            if (jump || !node.isIdle) node = MutableTransitionState(node.targetState)
            return@withMutableSnapshot true // Make sure to apply changes!
        }

        val newNode = when (direction) {
            AdvanceDirection.Forward -> node.targetState.nextSlide() ?: return false // Does not apply changes!
            AdvanceDirection.Backward -> node.targetState.prevSlide() ?: return false // Does not apply changes!
        }

        when (jump) {
            true -> node = MutableTransitionState(newNode)
            false -> node.targetState = newNode
        }
        return@withMutableSnapshot true // Make sure to apply changes!
    }

    // TODO deprecate and remove?
    fun jumpTo(slide: Slide<*>, index: Int = slide.currentIndex): Boolean = withMutableSnapshot {
        val newNode = slide as LinkedSlide<*>
        if (index !in newNode.states.indices) return false // Does not apply changes!

        this.direction = when (newNode == node.targetState) {
            true -> (index > node.targetState.currentIndex).toDirection()
            false -> (newNode > node.targetState).toDirection()
        }

        newNode.jumpTo(index)
        node = MutableTransitionState(newNode)
        return@withMutableSnapshot true // Make sure to apply changes!
    }

    fun jumpTo(slideIndex: Int, stateIndex: Int? = null): Boolean = withMutableSnapshot {
        if (slideIndex !in slides.indices) return false // Does not apply changes!
        val newNode = slides[slideIndex] as LinkedSlide<*>
        val stateIndex = stateIndex ?: newNode.currentIndex
        if (stateIndex !in newNode.states.indices) return false // Does not apply changes!

        this.direction = when (newNode == node.targetState) {
            true -> (stateIndex > node.targetState.currentIndex).toDirection()
            false -> (newNode > node.targetState).toDirection()
        }

        newNode.jumpTo(stateIndex)
        node = MutableTransitionState(newNode)
        return@withMutableSnapshot true // Make sure to apply changes!
    }

    private fun Boolean.toDirection(): AdvanceDirection = when (this) {
        true -> AdvanceDirection.Forward
        false -> AdvanceDirection.Backward
    }
}

@Stable
internal class LinkedSlide<T>(
    internal val slideIndex: Int,
    override val states: ImmutableList<Slide.State<T>>,
    override val enterTransition: (AdvanceDirection) -> EnterTransition,
    override val exitTransition: (AdvanceDirection) -> ExitTransition,
    override val content: SlideContent<T>,
) : Slide<T> {
    internal var next: LinkedSlide<*>? = null
    internal var prev: LinkedSlide<*>? = null

    override var currentIndex by mutableStateOf(0)
        private set

    fun advance(direction: AdvanceDirection): Boolean {
        val newIndex = when (direction) {
            AdvanceDirection.Forward -> currentIndex + 1
            AdvanceDirection.Backward -> currentIndex - 1
        }
        if (newIndex !in states.indices) return false
        currentIndex = newIndex
        return true
    }

    fun jumpTo(index: Int) {
        require(index in states.indices)
        this.currentIndex = index
    }

    fun nextSlide(): LinkedSlide<*>? {
        return next?.also { it.jumpTo(0) }
    }

    fun prevSlide(): LinkedSlide<*>? {
        return prev?.also { it.jumpTo(it.states.lastIndex) }
    }

    override fun compareTo(other: Slide<*>): Int {
        return compareValues(slideIndex, (other as LinkedSlide<*>).slideIndex)
    }
}

private class StoryboardBuilderImpl(
    private val slides: MutableList<LinkedSlide<*>>,
) : StoryboardBuilder {
    override fun <T> slide(
        states: List<Slide.State<T>>,
        enterTransition: (AdvanceDirection) -> EnterTransition,
        exitTransition: (AdvanceDirection) -> ExitTransition,
        content: SlideContent<T>,
    ): Slide<T> {
        val slide = LinkedSlide(
            slideIndex = slides.size,
            states = states.toImmutableList(),
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            content = content,
        )
        val prev = slides.lastOrNull()
        if (prev != null) {
            slide.prev = prev
            prev.next = slide
        }
        slides.add(slide)
        return slide
    }
}

// ---- TODO put this somewhere else
