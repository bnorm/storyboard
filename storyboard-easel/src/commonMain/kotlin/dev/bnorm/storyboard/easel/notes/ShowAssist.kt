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
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Current Frame")
                    CompositionLocalProvider(LocalStoryboardNotes provides notes) {
                        ClickableScenePreview(storyboard, storyboard.currentIndex)
                    }
                    SceneAnimationProgressIndicator(storyboard)
                }
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Next Frame")
                    val nextIndex by derivedStateOf {
                        val i = storyboard.storyboard.indices.binarySearch(storyboard.currentIndex)
                        require(i >= 0) { "targetIndex not found in storyboard" }
                        storyboard.storyboard.indices.getOrNull(i + 1)
                    }
                    nextIndex?.let {
                        ClickableScenePreview(storyboard, it)
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

