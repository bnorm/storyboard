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

class StoryState(
    val storyboard: Storyboard,
    initialIndex: Storyboard.Index = Storyboard.Index(0, 0),
) {
    private val frames = buildList {
        val scenes = storyboard.scenes
        require(scenes.isNotEmpty()) { "cannot build frames for empty list of scenes" }

        val first = scenes.first()
        val last = scenes.last()
        require(first.states.isNotEmpty() && last.states.isNotEmpty()) { "first and last scene must have states" }

        var frameIndex = 0
        fun <T> MutableList<StateFrame<*>>.addStates(scene: StateScene<T>) {
            for ((stateIndex, state) in scene.scene.states.withIndex()) {
                val index = StateFrame(
                    frameIndex = frameIndex++,
                    scene = scene,
                    state = Frame.State(state),
                    storyboardIndex = Storyboard.Index(scene.sceneIndex, stateIndex)
                )
                add(index)
            }
        }

        for ((sceneIndex, scene) in storyboard.scenes.withIndex()) {
            val stateScene = StateScene(sceneIndex, scene)
            if (scene != first) add(StateFrame(frameIndex++, stateScene, Frame.Start, Storyboard.Index(sceneIndex, -1)))
            addStates(stateScene)
            if (scene != last) add(StateFrame(frameIndex++, stateScene, Frame.End, Storyboard.Index(sceneIndex, -2)))
        }
    }

    private val byIndex = frames.filter { it.storyboardIndex.stateIndex >= 0 }.associateBy { it.storyboardIndex }

    init {
        require(initialIndex.sceneIndex >= 0 && initialIndex.stateIndex >= 0) { "initialIndex must be a valid frame" }
        require(initialIndex in byIndex) { "initialIndex must be in storyboard" }
    }

    var currentDirection: AdvanceDirection by mutableStateOf(AdvanceDirection.Forward)
        private set

    // TODO the mutable state is a workaround for SeekableTransitionState not
    //  supporting `snapTo` without a transition.
    // TODO report a bug?
    private var frameIndex by mutableStateOf(SeekableTransitionState(0))

    var currentIndex: Storyboard.Index by mutableStateOf(initialIndex)
        private set

    var targetIndex: Storyboard.Index by mutableStateOf(currentIndex)
        private set

    val advancementDistance: Float
        get() {
            if (currentIndex == targetIndex) return 1f
            val start = byIndex.getValue(currentIndex).frameIndex
            val target = byIndex.getValue(targetIndex).frameIndex
            return abs(target - start).toFloat()
        }

    val advancementProgress: Float
        get() {
            if (currentIndex == targetIndex) return 1f
            val start = byIndex.getValue(currentIndex).frameIndex
            val current = frameIndex.currentState
            return abs(current - start) + frameIndex.fraction
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
                val target = byIndex.getValue(targetIndex)
                frameIndex.snapTo(target.frameIndex)
                currentIndex = target.storyboardIndex
            }

            // Find the next target frame index.
            val targetIndex = findTargetIndex(direction) ?: return false
            this@StoryState.targetIndex = frames[targetIndex].storyboardIndex
        } else {
            // Reverse directions.
            val tmp = currentIndex
            currentIndex = targetIndex
            targetIndex = tmp

            frameIndex.animateTo(frameIndex.currentState)
        }

        // Animate to the target frame.
        while (frames[frameIndex.currentState].storyboardIndex != targetIndex) {
            frameIndex.animateTo(frameIndex.currentState + direction.toInt())
        }

        currentIndex = targetIndex
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

        while (frames[targetState].state !is Frame.State) {
            targetState += direction.toInt()
        }
        return targetState
    }

    suspend fun jumpTo(index: Storyboard.Index): Boolean {
        val frame = byIndex[index]
        require(frame != null) { "$frame not found in storyboard" }

        targetIndex = frame.storyboardIndex
        frameIndex.snapTo(frame.frameIndex)

        // TODO this is a workaround for SeekableTransitionState not supporting
        //  `snapTo` without a transition.
        // TODO report a bug?
        if (frameIndex.currentState != frame.frameIndex) {
            frameIndex = SeekableTransitionState(frame.frameIndex)
        }

        currentIndex = targetIndex
        return true
    }

    internal class StateScene<T>(
        val sceneIndex: Int,
        val scene: Scene<T>,
    ) : Comparable<StateScene<*>> {
        override fun compareTo(other: StateScene<*>): Int {
            return compareValues(sceneIndex, other.sceneIndex)
        }
    }

    internal class StateFrame<T>(
        val frameIndex: Int,
        val scene: StateScene<T>,
        val state: Frame<T>,
        val storyboardIndex: Storyboard.Index,
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
