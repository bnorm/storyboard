package dev.bnorm.storyboard.easel

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import dev.bnorm.storyboard.core.AdvanceDirection
import dev.bnorm.storyboard.core.StoryboardState
import dev.bnorm.storyboard.easel.internal.requestFocus
import dev.bnorm.storyboard.ui.StoryboardScene
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun Storyboard(
    storyboard: StoryboardState,
    overview: StoryboardOverview = remember(storyboard) { StoryboardOverview.of(storyboard) },
    overlayState: OverlayState = rememberOverlayState(),
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    val holder = rememberSaveableStateHolder()

    fun handleKeyEvent(event: KeyEvent): Boolean {
        if (event.type == KeyEventType.KeyUp && event.key == Key.Escape) {
            overview.isVisible = true
            return true
        }
        return false
    }

    Box(modifier = modifier) {
        SharedTransitionLayout {
            AnimatedContent(
                targetState = overview.isVisible,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) }
            ) { isOverview ->
                if (isOverview) {
                    StoryboardOverview(
                        overview = overview,
                        onExitOverview = {
                            job?.cancel()
                            job = coroutineScope.launch {
                                // TODO this doesn't work because the transition is not attached!
                                storyboard.jumpTo(it)
                                job = null
                                overview.isVisible = false
                            }
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    holder.SaveableStateProvider(storyboard) {
                        Box(modifier = Modifier.onPointerMovePress(state = overlayState)) {
                            StoryboardScene(
                                storyboard = storyboard,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .sharedElement(
                                        rememberSharedContentState(OverviewCurrentIndex),
                                        animatedVisibilityScope = this@AnimatedContent
                                    )
                                    .requestFocus()
                                    .onStoryboardNavigation(storyboard = storyboard)
                                    .onKeyEvent { handleKeyEvent(it) })
                            StoryboardOverlay(storyboard = storyboard, state = overlayState)
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun Modifier.onStoryboardNavigation(storyboard: StoryboardState): Modifier {
    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    fun handle(event: KeyEvent): Boolean {
        when (event.type) {
            KeyEventType.KeyDown -> {
                when (event.key) {
                    Key.DirectionRight,
//                    Key.DirectionDown,
//                    Key.Enter,
//                    Key.Spacebar,
                        -> {
                        job?.cancel()
                        job = coroutineScope.launch {
                            storyboard.advance(AdvanceDirection.Forward)
                            job = null
                        }
                        return true
                    }

                    Key.DirectionLeft,
//                    Key.DirectionUp,
//                    Key.Backspace,
                        -> {
                        job?.cancel()
                        job = coroutineScope.launch {
                            storyboard.advance(AdvanceDirection.Backward)
                            job = null
                        }
                        return true
                    }
                }
            }
        }

        return false
    }

    return onKeyEvent { handle(it) }
}
