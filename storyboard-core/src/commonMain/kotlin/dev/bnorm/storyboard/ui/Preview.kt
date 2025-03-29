package dev.bnorm.storyboard.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.core.*

@Composable
internal fun <T> ScenePreview(
    scene: Scene<T>,
    stateIndex: Int,
    size: DpSize = Storyboard.DEFAULT_SIZE,
    decorator: SceneDecorator = SceneDecorator.None,
    displayType: DisplayType = DisplayType.Preview,
    modifier: Modifier = Modifier,
) {
    SceneWrapper(size, decorator, displayType, modifier.aspectRatio(size.width / size.height)) {
        AnimatedVisibility(true) {
            Box(Modifier.fillMaxSize()) {
                key(scene, stateIndex) {
                    val scope = PreviewSceneScope(
                        states = scene.states,
                        frame = updateTransition(Frame.State(scene.states[stateIndex])),
                        animatedVisibilityScope = this@AnimatedVisibility,
                        sharedTransitionScope = this@SceneWrapper
                    )
                    scene.content(scope)
                }
            }
        }
    }
}

@Composable
internal fun <T> ScenePreview(
    scene: Scene<T>,
    state: Frame<Nothing>,
    size: DpSize = Storyboard.DEFAULT_SIZE,
    decorator: SceneDecorator = SceneDecorator.None,
    displayType: DisplayType = DisplayType.Preview,
    modifier: Modifier = Modifier,
) {
    SceneWrapper(size, decorator, displayType, modifier.aspectRatio(size.width / size.height)) {
        AnimatedVisibility(true) {
            Box(Modifier.fillMaxSize()) {
                key(scene, state) {
                    val scope = PreviewSceneScope(
                        states = scene.states,
                        frame = updateTransition(state),
                        animatedVisibilityScope = this@AnimatedVisibility,
                        sharedTransitionScope = this@SceneWrapper
                    )
                    scene.content(scope)
                }
            }
        }
    }
}

@Composable
fun ScenePreview(
    storyboard: Storyboard,
    index: Storyboard.Index,
    displayType: DisplayType = DisplayType.Preview,
    modifier: Modifier = Modifier,
) {
    ProvideStoryboard(storyboard) {
        ScenePreview(
            scene = storyboard.scenes[index.sceneIndex],
            stateIndex = index.stateIndex,
            displayType = displayType,
            size = storyboard.size,
            decorator = storyboard.decorator,
            modifier = modifier,
        )
    }
}

@Composable
fun StoryPreview(
    storyboard: Storyboard,
    displayType: DisplayType = DisplayType.Preview,
) {
    ProvideStoryboard(storyboard) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 16.dp)
                .verticalScroll(rememberScrollState())
                .background(Color.Transparent)
        ) {
            for (scene in storyboard.scenes) {
                Text("Frame: Start")
                ScenePreview(scene, Frame.Start, storyboard.size, storyboard.decorator, displayType)

                for (stateIndex in scene.states.indices) {
                    Text("Frame: $stateIndex")
                    ScenePreview(scene, stateIndex, storyboard.size, storyboard.decorator, displayType)
                }

                Text("Frame: End")
                ScenePreview(scene, Frame.End, storyboard.size, storyboard.decorator, displayType)
            }
        }
    }
}

@Composable
fun StoryPreview(
    name: String = "Preview",
    description: String? = null,
    size: DpSize = Storyboard.DEFAULT_SIZE,
    decorator: SceneDecorator = SceneDecorator.None,
    displayType: DisplayType = DisplayType.Preview,
    block: StoryboardBuilder.() -> Unit,
) {
    StoryPreview(Storyboard.build(name, description, size, decorator, block), displayType)
}
