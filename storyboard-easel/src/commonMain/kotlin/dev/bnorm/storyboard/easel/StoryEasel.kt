package dev.bnorm.storyboard.easel

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import dev.bnorm.storyboard.Decorator
import dev.bnorm.storyboard.easel.overview.OverviewCurrentItemKey
import dev.bnorm.storyboard.easel.overview.StoryOverview
import dev.bnorm.storyboard.easel.overview.StoryOverviewState
import dev.bnorm.storyboard.plus
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun OverviewDecorator(
    easel: Easel,
): Decorator = Decorator { content ->
    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    val holder = rememberSaveableStateHolder()

    val storyboard = easel.storyboard
    val storyOverviewState = remember(storyboard) { StoryOverviewState.of(storyboard) }
    var overviewVisible by remember { mutableStateOf(false) } // TODO support initial visibility?

    fun handleKeyEvent(event: KeyEvent): Boolean {
        if (event.type == KeyEventType.KeyUp && event.key == Key.Escape) {
            storyOverviewState.jumpToIndex(easel.currentIndex)
            overviewVisible = true
            return true
        }
        return false
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        SharedTransitionLayout {
            AnimatedContent(
                targetState = overviewVisible,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) }
            ) { isOverview ->
                if (isOverview) {
                    StoryOverview(
                        storyController = easel,
                        storyOverviewState = storyOverviewState,
                        onExitOverview = {
                            job?.cancel()
                            job = coroutineScope.launch {
                                easel.jumpTo(it)
                                job = null
                                overviewVisible = false
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    holder.SaveableStateProvider(storyboard) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .sharedElement(
                                        rememberSharedContentState(OverviewCurrentItemKey),
                                        animatedVisibilityScope = this@AnimatedContent
                                    )
                                    .onStoryNavigation(easel)
                                    .onKeyEvent { handleKeyEvent(it) }
                            ) {
                                content()
                            }
                        }
                    }
                }
            }
        }
    }
}
