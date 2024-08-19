package dev.bnorm.storyboard.core

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Stable

@Stable
sealed interface SlideScope<out T> : AnimatedVisibilityScope, SharedTransitionScope {
    val state: T
    val direction: AdvanceDirection

    // TODO advance to the next slide; provide via composition local instead?
    fun advance(): Boolean

    // TODO jump to a slide; provide via composition local instead?
    fun jumpTo(slide: Slide<*>, index: Int = slide.currentIndex): Boolean
}

internal class PreviewSlideScope<T>(
    override val state: T,
    animatedContentScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) : SlideScope<T>,
    AnimatedVisibilityScope by animatedContentScope,
    SharedTransitionScope by sharedTransitionScope {

    override val direction: AdvanceDirection get() = AdvanceDirection.Forward
    override fun advance(): Boolean = false
    override fun jumpTo(slide: Slide<*>, index: Int): Boolean = false
}

internal class StoryboardSlideScope<T>(
    private val slide: Slide<T>,
    private val storyboard: Storyboard,
    animatedContentScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) : SlideScope<T>,
    AnimatedVisibilityScope by animatedContentScope,
    SharedTransitionScope by sharedTransitionScope {

    override val state: T get() = slide.states[slide.currentIndex].value
    override val direction: AdvanceDirection get() = storyboard.direction

    override fun advance(): Boolean {
        require(slide.states[slide.currentIndex].transitional) { "cannot self advance non-transitional slide" }
        if (transition.isRunning) return false
        return storyboard.advance(direction)
    }

    override fun jumpTo(slide: Slide<*>, index: Int): Boolean {
        return storyboard.jumpTo(slide, index)
    }
}
