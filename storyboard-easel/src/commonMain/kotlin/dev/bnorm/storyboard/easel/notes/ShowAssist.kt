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
import dev.bnorm.storyboard.easel.StoryState
import dev.bnorm.storyboard.easel.internal.requestFocus
import dev.bnorm.storyboard.easel.onStoryboardNavigation
import dev.bnorm.storyboard.easel.ScenePreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun StoryNotes(storyState: StoryState, notes: StoryNotes, modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize()
            .requestFocus()
            .onStoryboardNavigation(storyboard = storyState)
    ) {
        Column(modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                StoryTimer()
            }

            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Current Frame")
                    CompositionLocalProvider(LocalStoryNotes provides notes) {
                        ClickableScenePreview(storyState, storyState.currentIndex)
                    }
                    SceneAnimationProgressIndicator(storyState)
                }
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Next Frame")
                    val nextIndex by derivedStateOf {
                        val i = storyState.storyboard.indices.binarySearch(storyState.currentIndex)
                        require(i >= 0) { "targetIndex not found in storyboard" }
                        storyState.storyboard.indices.getOrNull(i + 1)
                    }
                    nextIndex?.let {
                        ClickableScenePreview(storyState, it)
                    }
                }
                // TODO previous frame?
                // TODO highlight frame which is being advanced to?
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
    storyboard: StoryState,
    index: Storyboard.Index,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    // TODO share with StoryboardOverview?
    Box(modifier) {
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
private fun SceneAnimationProgressIndicator(storyboard: StoryState) {
    Row {
        val advancementDistance = storyboard.advancementDistance
        val advancementProgress = storyboard.advancementProgress
        when {
            advancementProgress == advancementDistance -> {
                Spacer(Modifier.height(2.dp).weight(1f).background(Color.Green))
            }

            else -> {
                val complete = advancementProgress.toInt() / advancementDistance
                val partial = (advancementProgress % 1f) / advancementDistance
                val remaining = 1f - complete - partial

                if (complete > 0f) Spacer(Modifier.height(2.dp).weight(complete).background(Color.Green))
                if (partial > 0f) Spacer(Modifier.height(2.dp).weight(partial).background(Color.Red))
                if (remaining > 0f) Spacer(Modifier.height(2.dp).weight(remaining).background(Color.DarkGray))
            }
        }
    }
}

