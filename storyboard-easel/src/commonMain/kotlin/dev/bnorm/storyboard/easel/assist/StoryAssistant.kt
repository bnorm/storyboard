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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.ScenePreview
import dev.bnorm.storyboard.easel.StoryController
import dev.bnorm.storyboard.easel.internal.requestFocus
import dev.bnorm.storyboard.easel.onStoryNavigation
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun StoryAssistant(
    assistantState: StoryAssistantState,
    modifier: Modifier = Modifier,
) {
    val easel = assistantState.easel
    val captions = assistantState.captions

    // Box with constraints?
    Surface(
        modifier = modifier
            .fillMaxSize()
            .onStoryNavigation(easel)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                StoryTimer()
            }
            StorySlider(easel)

            Layout(
                content = {
                    CurrentFramePreview(easel)
                    NextFramePreview(easel)
                    // TODO previous frame?
                    // TODO highlight frame which is being advanced to?
                    Captions(captions)
                }
            ) { measurables, constraints ->
                val currentMeasurable = measurables[0]
                val nextMeasurable = measurables[1]

                val spacing = 16.dp.roundToPx()
                val quarterBoxHeight = constraints.maxHeight / 2 - spacing
                val quarterBoxWidth = constraints.maxWidth / 2 - spacing
                val quarterBoxConstraints = Constraints(
                    minWidth = 0, maxWidth = quarterBoxWidth,
                    minHeight = 0, maxHeight = quarterBoxHeight,
                )

                val currentPlaceable = currentMeasurable.measure(quarterBoxConstraints)
                val nextPlaceable = nextMeasurable.measure(quarterBoxConstraints)

                val vertical = currentPlaceable.height < quarterBoxConstraints.maxHeight
                val captionsConstraints = when {
                    vertical -> Constraints.fixed(
                        width = constraints.maxWidth,
                        height = constraints.maxHeight - spacing - currentPlaceable.height,
                    )

                    else -> Constraints.fixed(
                        width = constraints.maxWidth - spacing - currentPlaceable.width,
                        height = constraints.maxHeight,
                    )
                }

                val captionsMeasurable = measurables[2]
                val captionsPlaceable = captionsMeasurable.measure(captionsConstraints)

                layout(constraints.maxWidth, constraints.maxHeight) {
                    currentPlaceable.placeRelative(0, 0)
                    if (vertical) {
                        nextPlaceable.placeRelative(currentPlaceable.width + spacing, 0)
                        captionsPlaceable.placeRelative(0, currentPlaceable.height + spacing)
                    } else {
                        nextPlaceable.placeRelative(0, currentPlaceable.height + spacing)
                        captionsPlaceable.placeRelative(currentPlaceable.width + spacing, 0)
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentFramePreview(storyController: StoryController, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        // TODO should "current" actually be target index?
        Text("Current Frame", style = MaterialTheme.typography.h4)
        Spacer(Modifier.size(8.dp))
        Box {
            ClickableScenePreview(
                storyController.storyboard,
                storyController.currentIndex,
                // Add padding for the progress indicator.
                modifier = Modifier.padding(bottom = 2.dp),
            )
            Box(Modifier.matchParentSize(), contentAlignment = Alignment.BottomCenter) {
                SceneAnimationProgressIndicator(storyController, modifier = Modifier.height(2.dp))
            }
        }
    }
}

@Composable
private fun NextFramePreview(storyController: StoryController, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Next Frame", style = MaterialTheme.typography.h4)
        Spacer(Modifier.size(8.dp))
        val nextIndex by derivedStateOf {
            val i = storyController.storyboard.indices.binarySearch(storyController.currentIndex)
            require(i >= 0) { "targetIndex not found in storyboard" }
            storyController.storyboard.indices.getOrNull(i + 1)
        }
        nextIndex?.let {
            ClickableScenePreview(
                storyboard = storyController.storyboard,
                index = it,
                onClick = {
                    job?.cancel()
                    job = coroutineScope.launch {
                        storyController.jumpTo(it)
                        job = null
                    }
                },
            )
        }
    }
}

@Composable
private fun Captions(captions: SnapshotStateList<Caption>, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Captions", style = MaterialTheme.typography.h4)
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
private fun ClickableScenePreview(
    storyboard: Storyboard,
    index: Storyboard.Index,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    // TODO share with StoryboardOverview?
    Box(modifier) {
        ScenePreview(
            storyboard = storyboard,
            index = index,
        )

        if (onClick != null) {
            Box(
                modifier = Modifier.matchParentSize()
                    .pointerHoverIcon(PointerIcon.Hand)
                    .clickable(
                        interactionSource = null, indication = null, // disable ripple effect
                        onClick = {
                            onClick.invoke()
                        }
                    )
            )
        }
    }
}

@Composable
private fun SceneAnimationProgressIndicator(storyController: StoryController, modifier: Modifier = Modifier) {
    Row(modifier) {
        val advancementDistance = storyController.advancementDistance
        val advancementProgress = storyController.advancementProgress
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
