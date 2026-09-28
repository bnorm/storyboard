package dev.bnorm.storyboard.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.*
import dev.bnorm.storyboard.layout.template.Body
import dev.bnorm.storyboard.layout.template.Header
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit

// Milliseconds
const val TWEEN_DURATION_MS = 200

val boundsTransform = BoundsTransform { _, _ ->
    spring(
        dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
    )
}

fun dev.bnorm.storyboard.StoryboardBuilder.warp(
    codeBlocks: List<CodeBlock>,
    language: Language,
    enter: SceneEnterTransition = SceneEnter(alignment = Alignment.CenterEnd),
    exit: SceneExitTransition = SceneExit(alignment = Alignment.CenterEnd),
    header: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    scene(
        frameCount = codeBlocks.size,
        enterTransition = enter,
        exitTransition = exit
    ) {
        // Build the diffList once and be ready.
        val diffList: List<DiffContents> = remember(codeBlocks, language) {
            buildDiffList(codeBlocks, language)
        }
        val hScrollState = rememberScrollState()
        val vScrollState = rememberScrollState()
        Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Header { header() }
            Divider(color = MaterialTheme.colors.primary)
            Body(modifier = Modifier.verticalScroll(vScrollState).horizontalScroll(hScrollState)) {
                SharedTransitionLayout(modifier = modifier) {
                    transition.AnimatedContent(
                        transitionSpec = {
                            fadeIn(
                                animationSpec = tween(durationMillis = TWEEN_DURATION_MS)
                            ).togetherWith(
                                exit = fadeOut(
                                    animationSpec = tween(durationMillis = TWEEN_DURATION_MS)
                                )
                            )
                        }
                    ) { frame ->
                        val index = frame.toValue()
                        val diffContents = diffList[index]
                        Code(
                            diffContents = diffContents,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this,
                            modifier = modifier
                        )
                    }
                }
            }
        }
    }
}
