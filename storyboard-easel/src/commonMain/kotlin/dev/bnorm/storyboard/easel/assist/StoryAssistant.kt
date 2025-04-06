package dev.bnorm.storyboard.easel.assist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.ScenePreview
import dev.bnorm.storyboard.easel.StoryState
import dev.bnorm.storyboard.easel.internal.requestFocus
import dev.bnorm.storyboard.easel.onStoryNavigation
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun StoryAssistant(assistantState: StoryAssistantState, modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier.fillMaxSize()
            .requestFocus()
            .onStoryNavigation(storyState = assistantState.storyState)
    ) {
        Column(modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                StoryTimer()
            }
            ScenePreview(assistantState)
            Captions(assistantState.captions)
        }
    }
}

@Composable
private fun ScenePreview(assistantState: StoryAssistantState) {
    val storyState = assistantState.storyState
    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    Column {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                // TODO should "current" actually be target index?
                Text("Current Frame")
                CompositionLocalProvider(LocalCaptions provides assistantState.captions) {
                    ClickableScenePreview(storyState.storyboard, storyState.currentIndex)
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
                    ClickableScenePreview(
                        storyboard = storyState.storyboard,
                        index = it,
                        onClick = {
                            job?.cancel()
                            job = coroutineScope.launch {
                                storyState.jumpTo(it)
                                job = null
                            }
                        },
                    )
                }
            }
            // TODO previous frame?
            // TODO highlight frame which is being advanced to?
        }

        StorySlider(storyState, modifier = Modifier.fillMaxWidth().padding(top = 16.dp))
    }
}

@Composable
private fun Captions(captions: SnapshotStateList<Caption>) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
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

@Composable
private fun ClickableScenePreview(
    storyboard: Storyboard,
    index: Storyboard.Index,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    // TODO share with StoryboardOverview?
    Box(modifier.height(IntrinsicSize.Min).width(IntrinsicSize.Min)) {
        ScenePreview(
            storyboard = storyboard,
            index = index,
        )

        // Cover the scene content with a clickable modifier
        // to disable interaction while in overview.
        Box(
            modifier = Modifier.fillMaxSize()
                .pointerHoverIcon(if (onClick != null) PointerIcon.Hand else PointerIcon.Default)
                .clickable(
                    interactionSource = null, indication = null, // disable ripple effect
                    onClick = {
                        onClick?.invoke()
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
