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
import dev.bnorm.storyboard.core.AdvanceDirection
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.easel.internal.aspectRatio
import dev.bnorm.storyboard.easel.internal.requestFocus
import dev.bnorm.storyboard.easel.onStoryboardNavigation
import dev.bnorm.storyboard.ui.SlidePreview

@Composable
fun StoryboardNotes(storyboard: Storyboard, notes: StoryboardNotes, modifier: Modifier = Modifier) {
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
                val frames = remember(storyboard) { storyboard.frames }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Current Slide")
                    ClickableSlidePreview(storyboard, storyboard.currentFrame)
                    SlideAnimationProgressIndicator(storyboard)
                }
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Next Slide")
                    val nextFrame by derivedStateOf {
                        val searchIndex = frames.binarySearch(storyboard.currentFrame)
                        val currentIndex = when {
                            searchIndex >= 0 -> searchIndex
                            else -> when (storyboard.direction) {
                                AdvanceDirection.Forward -> -(searchIndex - 1)
                                AdvanceDirection.Backward -> -searchIndex
                            }
                        }
                        val nextIndex = currentIndex + 1
                        if (nextIndex in frames.indices) frames[nextIndex] else null
                    }
                    nextFrame?.let {
                        ClickableSlidePreview(storyboard, it)
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
private fun ClickableSlidePreview(
    storyboard: Storyboard,
    frame: Storyboard.Frame,
    modifier: Modifier = Modifier,
) {
    // TODO share with StoryboardOverview?
    Box(modifier.aspectRatio(storyboard.size.aspectRatio)) {
        SlidePreview(
            storyboard = storyboard,
            frame = frame,
        )

        // Cover the slide content with a clickable modifier
        // to disable interaction while in overview.
        Box(
            modifier = Modifier.fillMaxSize()
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable(
                    interactionSource = null, indication = null, // disable ripple effect
                    onClick = { storyboard.jumpTo(frame) }
                )
        )
    }
}

@Composable
private fun SlideAnimationProgressIndicator(storyboard: Storyboard) {
    Row {
        val advancementProgress = storyboard.advancementProgress
        val color = if (advancementProgress == 1f) Color.Green else Color.Red
        Spacer(Modifier.height(2.dp).weight(advancementProgress).background(color))
        if (advancementProgress < 1f) {
            Spacer(Modifier.weight(1f - advancementProgress))
        }
    }
}

