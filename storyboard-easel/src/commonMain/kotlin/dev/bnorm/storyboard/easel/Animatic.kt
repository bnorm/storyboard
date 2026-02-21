package dev.bnorm.storyboard.easel

import androidx.compose.animation.core.SeekableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.runtime.*
import dev.bnorm.storyboard.AdvanceDirection
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.Scene
import dev.bnorm.storyboard.Storyboard
import kotlin.math.abs

@Composable
fun rememberAnimatic(
    storyboard: () -> Storyboard,
): Animatic {
    val storyboard = remember(storyboard) { storyboard() }
    val states = remember(storyboard) { Animatic.buildStates(storyboard) }

    var transitionState by remember { mutableStateOf(SeekableTransitionState(states.first())) }

    remember(storyboard) {
        // TODO a little ugly to have this in a remember...
        // TODO need to backtrack to the nearest frame value
        // Attempt to preserve the index of the previous Storyboard instance when it changes.
        val initialState = states.find { it.index == transitionState.currentState.index } ?: states.first()
        transitionState = SeekableTransitionState(initialState)
    }

    val transition = rememberTransition(transitionState, label = "Animatic")
    return remember(storyboard, states, transitionState, transition) {
        Animatic(storyboard, states, transitionState, transition)
    }
}

@Stable
class Animatic internal constructor(
    val storyboard: Storyboard,
    private val states: List<LinkedState<*>>,
    private val transitionState: SeekableTransitionState<LinkedState<*>>,
    val transition: Transition<out State<*>>,
) {
    @Immutable
    interface State<T> {
        val scene: Scene<T>
        val frame: Frame<T>
        val index: Storyboard.Index
    }

    internal class LinkedState<T>(
        override val scene: Scene<T>,
        override val frame: Frame<T>,
        override val index: Storyboard.Index,
        val stateIndex: Int,
    ) : State<T>, Comparable<LinkedState<*>> {
        var prev: LinkedState<*>? = null
        var next: LinkedState<*>? = null

        override fun compareTo(other: LinkedState<*>): Int {
            return compareValues(stateIndex, other.stateIndex)
        }
    }

    // TODO what should the initial values be?
    private var currentState by mutableStateOf(transitionState.currentState)
    private var targetState by mutableStateOf(transitionState.targetState)

    val currentIndex: Storyboard.Index get() = currentState.index
    val targetIndex: Storyboard.Index get() = targetState.index

    suspend fun advance(direction: AdvanceDirection): Boolean {
        val currentDirection = toDirection(transitionState.currentState.index, transitionState.targetState.index)
        if (currentDirection == null || currentDirection == direction) {
            if (currentDirection == direction) {
                // Snap to the target and continue.
                transitionState.snapTo(targetState)
                currentState = targetState
                // Fall-through and continue with the next advancement.
            }

            // Find the next target frame index.
            val targetFrame = findTargetFrame(direction) ?: return false
            targetState = targetFrame
        } else {
            // Reverse directions.
            val tmp = currentState
            currentState = targetState
            targetState = tmp

            transitionState.animateTo(transitionState.currentState)
        }

        // Animate to the target frame.
        var iterState = transitionState.currentState
        while (iterState != targetState) {
            iterState = iterState.advance(direction) ?: error("internal error")
            transitionState.animateTo(iterState)
        }

        currentState = targetState
        return true
    }

    suspend fun jumpTo(index: Storyboard.Index): Boolean {
        // TODO a little inefficient?
        var jumpIndex = states.binarySearchBy(index) { it.index }
        if (jumpIndex < 0) return false

        // Back up from the search index until a state frame is found.
        // Since the first frame of a storyboard is always a state frame,
        // this loop will never run out of bounds.
        while (states[jumpIndex].frame !is Frame.Value) {
            jumpIndex--
        }

        targetState = states[jumpIndex]
        transitionState.snapTo(targetState)
        currentState = targetState
        return true
    }

    private fun findTargetFrame(direction: AdvanceDirection): LinkedState<*>? {
        require(transitionState.currentState == transitionState.targetState) {
            "cannot find target state during transition: " +
                    "currentState=${transitionState.currentState} " +
                    "targetState=${transitionState.targetState}"
        }

        var iterState = transitionState.currentState.advance(direction) ?: return null
        while (iterState.frame !is Frame.Value) {
            iterState = iterState.advance(direction) ?: error("internal error")
        }
        return iterState
    }

    private fun LinkedState<*>.advance(direction: AdvanceDirection): LinkedState<*>? = when (direction) {
        AdvanceDirection.Forward -> next
        AdvanceDirection.Backward -> prev
    }

    private fun <T : Comparable<T>> toDirection(current: T, target: T): AdvanceDirection? {
        val compare = current.compareTo(target)
        return when {
            compare < 0 -> AdvanceDirection.Forward
            compare > 0 -> AdvanceDirection.Backward
            else -> null
        }
    }

    companion object {
        internal fun buildStates(storyboard: Storyboard): List<LinkedState<*>> = buildList {
            require(storyboard.scenes.isNotEmpty()) { "cannot build frames for empty list of scenes" }

            val first = storyboard.scenes.first()
            val last = storyboard.scenes.last()
            require(first.frames.isNotEmpty() && last.frames.isNotEmpty()) { "first and last scene must have states" }

            var stateIndex = 0
            fun <T> MutableList<LinkedState<*>>.addStates(scene: Scene<T>) {
                for ((frameIndex, value) in scene.frames.withIndex()) {
                    val index = LinkedState(
                        scene = scene,
                        frame = Frame.Value(value),
                        index = Storyboard.Index(scene.index, frameIndex),
                        stateIndex = stateIndex++,
                    )
                    add(index)
                }
            }

            for (scene in storyboard.scenes) {
                if (scene != first) {
                    // TODO don't create an invalid Storyboard.Index
                    add(
                        LinkedState(
                            scene = scene,
                            frame = Frame.Start,
                            index = Storyboard.Index(scene.index, -1),
                            stateIndex = stateIndex++,
                        )
                    )
                }
                addStates(scene)
                if (scene != last) {
                    // TODO don't create an invalid Storyboard.Index
                    add(
                        LinkedState(
                            scene = scene,
                            frame = Frame.End,
                            index = Storyboard.Index(scene.index, scene.frames.size),
                            stateIndex = stateIndex++,
                        )
                    )
                }
            }

            var prev: LinkedState<*>? = null
            for (state in this) {
                state.prev = prev
                prev?.next = state
                prev = state
            }
        }
    }

    // TODO internal stuff for slider
    //  - is there a way to simplify all of this?
    //  - is there a way we can make this public without exposing too much?

    internal val storyDistance: Float
        get() = states.lastIndex.toFloat()

    internal val storyProgress: Float
        get() {
            val current = transitionState.currentState.stateIndex
            val target = transitionState.targetState.stateIndex
            val fraction = when {
                current == target -> 0f
                target > current -> transitionState.fraction
                else -> -transitionState.fraction
            }

            return current.toFloat() + fraction
        }

    internal val advancementDistance: Float
        get() {
            if (currentState == targetState) return 1f
            val start = currentState.stateIndex
            val target = targetState.stateIndex
            return abs(target - start).toFloat()
        }

    internal val advancementProgress: Float
        get() {
            if (currentIndex == targetIndex) return 1f
            val start = currentState.stateIndex
            val current = transitionState.currentState.stateIndex
            val fraction = if (current == transitionState.targetState.stateIndex) 0f else transitionState.fraction
            return abs(current - start) + fraction
        }

    internal suspend fun seek(storyFraction: Float) {
        val progress = storyFraction * storyDistance
        val index = progress.toInt()
        val frameFraction = progress - index

        if (transitionState.currentState.stateIndex != index) {
            transitionState.snapTo(states[index])

            var currentState = index
            while (currentState >= 0 && states[currentState].frame !is Frame.Value) currentState--
            this.currentState = states[currentState]

            var targetState = index + if (frameFraction > 0f) 1 else 0
            while (targetState < states.size && states[targetState].frame !is Frame.Value) targetState++
            this.targetState = states[targetState]
        }

        // TODO there seems to be some serious bugs with SeekableTransitionState still...
        //  - seeking backwards seems to be broken for the opening animation
        //  - seeking backwards in general actually seems a little broken
        //  maybe I can create a minimal reproducer?
        if (frameFraction != 0f) {
            transitionState.seekTo(frameFraction, states[index + 1])
        }
    }
}
