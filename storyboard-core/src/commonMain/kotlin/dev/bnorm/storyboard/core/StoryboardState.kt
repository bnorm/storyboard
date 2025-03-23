package dev.bnorm.storyboard.core

import androidx.compose.animation.core.SeekableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.abs

class StoryboardState(
    val storyboard: Storyboard,
    initialFrame: Storyboard.Frame = Storyboard.Frame(0, 0),
) {
    private val frames = buildList {
        val slides = storyboard.slides
        require(slides.isNotEmpty()) { "cannot build frames for empty list of slides" }

        val first = slides.first()
        val last = slides.last()
        require(first.states.isNotEmpty() && last.states.isNotEmpty()) { "first and last slide must have states" }

        var frameIndex = 0
        fun <T> MutableList<StateFrame<*>>.addStates(scene: StateScene<T>, slideIndex: Int) {
            for ((stateIndex, state) in scene.slide.states.withIndex()) {
                val frame = StateFrame(
                    index = frameIndex++,
                    scene = scene,
                    state = SlideState.Value(state),
                    frame = Storyboard.Frame(slideIndex, stateIndex)
                )
                add(frame)
            }
        }

        for ((slideIndex, slide) in storyboard.slides.withIndex()) {
            val scene = StateScene(slideIndex, slide)
            if (slide != first) add(StateFrame(frameIndex++, scene, SlideState.Start, Storyboard.Frame(slideIndex, -1)))
            addStates(scene, slideIndex)
            if (slide != last) add(StateFrame(frameIndex++, scene, SlideState.End, Storyboard.Frame(slideIndex, -2)))
        }
    }

    private val byFrame = frames.filter { it.frame.stateIndex >= 0 }.associateBy { it.frame }

    init {
        require(initialFrame.slideIndex >= 0 && initialFrame.stateIndex >= 0) { "initialFrame must be a valid frame" }
        require(initialFrame in byFrame) { "initialFrame must be in storyboard" }
    }

    var currentDirection: AdvanceDirection by mutableStateOf(AdvanceDirection.Forward)
        private set

    // TODO the mutable state is a workaround for SeekableTransitionState not
    //  supporting `snapTo` without a transition.
    // TODO report a bug?
    private var frameIndex by mutableStateOf(SeekableTransitionState(0))

    var currentFrame: Storyboard.Frame by mutableStateOf(initialFrame)
        private set

    var targetFrame: Storyboard.Frame by mutableStateOf(currentFrame)
        private set

    val advancementProgress: Float
        get() {
            if (currentFrame == targetFrame) return 1f

            val start = byFrame.getValue(currentFrame)
            val current = frameIndex.currentState
            val target = byFrame.getValue(targetFrame)

            val progress = abs(current - start.index)
            val distance = abs(target.index - start.index)
            return progress + (frameIndex.fraction / distance)
        }

    // TODO manage scope + job + launch internally? or have utilities for it?
    //  - seems like a lot of places need to manage a scope and job,
    //    there should be some utility for each of those place to advance/jump
    //    without suspension. This would also allow for coordinated cancellation.
    //  - is that this class? is it some other utility?
    //  - do we need to be careful about which scope the advancement runs in?
    suspend fun advance(direction: AdvanceDirection): Boolean {
        this.currentDirection = direction

        val currentDirection = toDirection(frameIndex.currentState, frameIndex.targetState)
        if (currentDirection == null || currentDirection == direction) {
            if (currentDirection == direction) {
                // Snap to the target and continue.
                val target = byFrame.getValue(targetFrame)
                frameIndex.snapTo(target.index)
                currentFrame = target.frame
            }

            // Find the next target frame index.
            val targetIndex = findTargetIndex(direction) ?: return false
            targetFrame = frames[targetIndex].frame
        } else {
            // Reverse directions.
            val tmp = currentFrame
            targetFrame = currentFrame
            currentFrame = tmp

            frameIndex.animateTo(frameIndex.currentState)
        }

        // Animate to the target frame.
        while (frames[frameIndex.currentState].frame != targetFrame) {
            frameIndex.animateTo(frameIndex.currentState + direction.toInt())
        }

        currentFrame = targetFrame
        return true
    }

    private fun findTargetIndex(direction: AdvanceDirection): Int? {
        require(frameIndex.currentState == frameIndex.targetState) {
            "cannot find target state during transition: " +
                    "currentState=${frameIndex.currentState} " +
                    "targetState=${frameIndex.targetState}"
        }

        var targetState = frameIndex.currentState + direction.toInt()
        if (targetState !in frames.indices) return null

        while (frames[targetState].state !is SlideState.Value) {
            targetState += direction.toInt()
        }
        return targetState
    }

    suspend fun jumpTo(frame: Storyboard.Frame): Boolean {
        val frame = byFrame[frame]
        require(frame != null) { "$frame not found in storyboard" }

        targetFrame = frame.frame
        frameIndex.snapTo(frame.index)

        // TODO this is a workaround for SeekableTransitionState not supporting
        //  `snapTo` without a transition.
        // TODO report a bug?
        if (frameIndex.currentState != frame.index) {
            frameIndex = SeekableTransitionState(frame.index)
        }

        currentFrame = targetFrame
        return true
    }

    class StateScene<T>(
        val index: Int,
        val slide: Slide<T>,
    ) : Comparable<StateScene<*>> {
        override fun compareTo(other: StateScene<*>): Int {
            return compareValues(index, other.index)
        }
    }

    class StateFrame<T>(
        val index: Int,
        val scene: StateScene<T>,
        val state: SlideState<T>,
        val frame: Storyboard.Frame,
    )

    @Composable
    internal fun rememberTransition(): Transition<StateFrame<*>> {
        return rememberTransition(frameIndex)
            .createChildTransition { frames[it] }
    }
}

private fun AdvanceDirection.toInt(): Int = when (this) {
    AdvanceDirection.Forward -> 1
    AdvanceDirection.Backward -> -1
}

private fun <T : Comparable<T>> toDirection(current: T, target: T): AdvanceDirection? {
    val compare = current.compareTo(target)
    return when {
        compare < 0 -> AdvanceDirection.Forward
        compare > 0 -> AdvanceDirection.Backward
        else -> null
    }
}
