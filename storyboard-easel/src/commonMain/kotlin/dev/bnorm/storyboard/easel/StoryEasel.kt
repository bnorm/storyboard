package dev.bnorm.storyboard.easel

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import dev.bnorm.storyboard.AdvanceDirection
import dev.bnorm.storyboard.easel.internal.requestFocus
import dev.bnorm.storyboard.easel.overlay.StoryOverlay
import dev.bnorm.storyboard.easel.overlay.StoryOverlayScope
import dev.bnorm.storyboard.easel.overview.OverviewCurrentItemKey
import dev.bnorm.storyboard.easel.overview.StoryOverview
import dev.bnorm.storyboard.easel.overview.StoryOverviewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalStoryStateApi::class)
@Composable
fun StoryEasel(
    storyState: StoryState,
    overlay: @Composable StoryOverlayScope.() -> Unit = {},
    modifier: Modifier = Modifier,
) {
    StoryEasel(storyState, storyState.rememberTransition(), overlay, modifier)
}

@Composable
fun StoryEasel(
    storyController: StoryController,
    transition: Transition<SceneFrame<*>>,
    overlay: @Composable StoryOverlayScope.() -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    val holder = rememberSaveableStateHolder()

    val storyboard = storyController.storyboard
    val storyOverviewState = remember(storyboard) { StoryOverviewState.of(storyboard) }
    var overviewVisible by remember { mutableStateOf(false) } // TODO support initial visibility?

    fun handleKeyEvent(event: KeyEvent): Boolean {
        if (event.type == KeyEventType.KeyUp && event.key == Key.Escape) {
            storyOverviewState.jumpToIndex(storyController.currentIndex)
            overviewVisible = true
            return true
        }
        return false
    }

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        SharedTransitionLayout {
            AnimatedContent(
                targetState = overviewVisible,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) }
            ) { isOverview ->
                if (isOverview) {
                    StoryOverview(
                        storyController = storyController,
                        storyOverviewState = storyOverviewState,
                        onExitOverview = {
                            job?.cancel()
                            job = coroutineScope.launch {
                                storyController.jumpTo(it)
                                job = null
                                overviewVisible = false
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    holder.SaveableStateProvider(storyboard) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            StoryOverlay(overlay = overlay) {
                                Story(
                                    storyboard = storyController.storyboard,
                                    transition = transition,
                                    modifier = Modifier
                                        .sharedElement(
                                            rememberSharedContentState(OverviewCurrentItemKey),
                                            animatedVisibilityScope = this@AnimatedContent
                                        )
                                        .requestFocus()
                                        .onStoryNavigation(storyController = storyController)
                                        .onKeyEvent { handleKeyEvent(it) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
