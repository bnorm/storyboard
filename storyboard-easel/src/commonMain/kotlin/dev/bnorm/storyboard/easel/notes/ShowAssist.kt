package dev.bnorm.storyboard.easel.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.core.StoryboardState
import dev.bnorm.storyboard.easel.internal.aspectRatio
import dev.bnorm.storyboard.easel.internal.requestFocus
import dev.bnorm.storyboard.easel.onStoryboardNavigation
import dev.bnorm.storyboard.ui.ScenePreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun StoryboardNotes(storyboard: StoryboardState, notes: StoryboardNotes, modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize()
            .requestFocus()
            .onStoryboardNavigation(storyboard = storyboard)
    ) {
        Column(modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                StoryboardTimer()
            }

            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                val currentIndex = storyboard.currentIndex
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Current Frame")
                    ClickableScenePreview(storyboard, currentIndex)
                    SceneAnimationProgressIndicator(storyboard)
                }
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Next Frame")
                    val targetIndex = storyboard.targetIndex
                    if (targetIndex != currentIndex) {
                        ClickableScenePreview(storyboard, targetIndex)

                    }
                }
            }

            if (notes.tabs.isNotEmpty()) {
                var state by remember { mutableStateOf(0) }
                Scaffold(
                    topBar = {
                        TabRow(selectedTabIndex = state) {
                            notes.tabs.forEachIndexed { index, tab ->
                                Tab(
                                    text = { Text(tab.title) },
                                    selected = state == index,
                                    onClick = { state = index }
                                )
                            }
                        }
                    },
                ) {
                    val tab = notes.tabs[state]
                    tab.content()
                }
            }
        }
    }
}

@Composable
private fun ClickableScenePreview(
    storyboard: StoryboardState,
    index: Storyboard.Index,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    // TODO share with StoryboardOverview?
    Box(modifier.aspectRatio(storyboard.storyboard.size.aspectRatio)) {
        ScenePreview(
            storyboard = storyboard.storyboard,
            index = index,
        )

        // Cover the scene content with a clickable modifier
        // to disable interaction while in overview.
        Box(
            modifier = Modifier.fillMaxSize()
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable(
                    interactionSource = null, indication = null, // disable ripple effect
                    onClick = {
                        job?.cancel()
                        job = coroutineScope.launch {
                            storyboard.jumpTo(index)
                            job = null
                        }
                    }
                )
        )
    }
}

@Composable
private fun SceneAnimationProgressIndicator(storyboard: StoryboardState) {
    Row {
        val advancementProgress = storyboard.advancementProgress
        val color = if (advancementProgress == 1f) Color.Green else Color.Red
        Spacer(Modifier.height(2.dp).weight(advancementProgress).background(color))
        if (advancementProgress < 1f) {
            Spacer(Modifier.weight(1f - advancementProgress))
        }
    }
}

