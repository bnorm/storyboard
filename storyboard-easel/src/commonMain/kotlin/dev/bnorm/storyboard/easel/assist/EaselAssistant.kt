package dev.bnorm.storyboard.easel.assist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.easel.*
import dev.bnorm.storyboard.easel.internal.QuarteredBox
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun EaselAssistant(
    animatic: Animatic,
    captions: SnapshotStateList<Caption>,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .onStoryNavigation(animatic)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                EaselTimer()
            }
            EaselSlider(animatic)

            QuarteredBox(
                // TODO highlight frame which is being advanced to?
                quarter1 = { CurrentFramePreview(animatic, modifier = Modifier.padding(8.dp)) },
                quarter2 = { NextFramePreview(animatic, modifier = Modifier.padding(8.dp)) },
                remaining = { Captions(captions, modifier = Modifier.padding(8.dp)) },
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun CurrentFramePreview(animatic: Animatic, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Current Frame", style = MaterialTheme.typography.h5)
        Spacer(Modifier.size(8.dp))
        Box {
            Easel(
                animatic,
                mode = SceneMode.Preview,
                // Add padding for the progress indicator.
                modifier = Modifier.padding(bottom = 2.dp),
            )
            Box(Modifier.matchParentSize(), contentAlignment = Alignment.BottomCenter) {
                SceneAnimationProgressIndicator(animatic, modifier = Modifier.height(2.dp))
            }
        }
    }
}

@Composable
private fun NextFramePreview(animatic: Animatic, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Next Frame", style = MaterialTheme.typography.h5)
        Spacer(Modifier.size(8.dp))
        val nextIndex by derivedStateOf {
            val i = animatic.storyboard.indices.binarySearch(animatic.currentIndex)
            require(i >= 0) { "targetIndex not found in storyboard" }
            animatic.storyboard.indices.getOrNull(i + 1)
        }
        nextIndex?.let {
            // TODO share with StoryboardOverview?
            Box(Modifier) {
                ScenePreview(
                    storyboard = animatic.storyboard,
                    index = it,
                )

                // Use a Box to overlay the preview so nothing within the preview can be clicked.
                // Also gives it a pointer hand.
                Box(
                    modifier = Modifier.matchParentSize()
                        .pointerHoverIcon(PointerIcon.Hand)
                        .clickable(
                            interactionSource = null, indication = null, // disable ripple effect
                            onClick = {
                                job?.cancel()
                                job = coroutineScope.launch {
                                    animatic.jumpTo(it)
                                    job = null
                                }
                            }
                        )
                )
            }
        }
    }
}

@Composable
private fun Captions(captions: SnapshotStateList<Caption>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Captions", style = MaterialTheme.typography.h5)
        Spacer(Modifier.size(8.dp))
        LazyColumn {
            items(captions) { caption ->
                Card(
                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        caption.content()
                    }
                }
            }
        }
    }
}

@Composable
private fun SceneAnimationProgressIndicator(animatic: Animatic, modifier: Modifier = Modifier) {
    Row(modifier) {
        val advancementDistance = animatic.advancementDistance
        val advancementProgress = animatic.advancementProgress
        when {
            advancementProgress == advancementDistance -> {
                Spacer(Modifier.fillMaxHeight().weight(1f).background(Color.Green))
            }

            else -> {
                val complete = advancementProgress.toInt() / advancementDistance
                val partial = (advancementProgress % 1f) / advancementDistance
                val remaining = 1f - complete - partial

                if (complete > 0f) Spacer(Modifier.fillMaxHeight().weight(complete).background(Color.Green))
                if (partial > 0f) Spacer(Modifier.fillMaxHeight().weight(partial).background(Color.Red))
                if (remaining > 0f) Spacer(Modifier.fillMaxHeight().weight(remaining).background(Color.DarkGray))
            }
        }
    }
}
