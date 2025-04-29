package dev.bnorm.storyboard.easel

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.*
import kotlinx.collections.immutable.ImmutableList

private class PreviewSceneScope<T>(
    override val states: ImmutableList<T>,
    override val transition: Transition<out Frame<T>>,
) : SceneScope<T>

@Composable
internal fun <T> ScenePreview(
    scene: Scene<T>,
    stateIndex: Int,
    modifier: Modifier = Modifier,
    size: DpSize = Storyboard.DEFAULT_SIZE,
    decorator: SceneDecorator = SceneDecorator.None,
    sceneMode: SceneMode = SceneMode.Preview,
) {
    SceneWrapper(size, decorator, sceneMode, modifier) {
        AnimatedVisibility(true) {
            key(scene, stateIndex) {
                val scope = PreviewSceneScope(
                    states = scene.states,
                    transition = updateTransition(Frame.State(scene.states[stateIndex])),
                )
                scope.Render(scene.content)
            }
        }
    }
}

@Composable
fun <T> ScenePreview(
    scene: Scene<T>,
    frame: Frame<T>,
    modifier: Modifier = Modifier,
    size: DpSize = Storyboard.DEFAULT_SIZE,
    decorator: SceneDecorator = SceneDecorator.None,
    sceneMode: SceneMode = SceneMode.Preview,
) {
    SceneWrapper(size, decorator, sceneMode, modifier) {
        AnimatedVisibility(true) {
            key(scene, frame) {
                val scope = PreviewSceneScope(
                    states = scene.states,
                    transition = updateTransition(frame),
                )
                scope.Render(scene.content)
            }
        }
    }
}

@Composable
fun <T> ScenePreview(
    storyboard: Storyboard,
    scene: Scene<T>,
    frame: Frame<T>,
    modifier: Modifier = Modifier,
    sceneMode: SceneMode = SceneMode.Preview,
) {
    CompositionLocalProvider(LocalStoryboard provides storyboard) {
        SceneWrapper(storyboard.size, storyboard.decorator, sceneMode, modifier) {
            AnimatedVisibility(true) {
                key(scene, frame) {
                    val scope = PreviewSceneScope(
                        states = scene.states,
                        transition = updateTransition(frame),
                    )
                    scope.Render(scene.content)
                }
            }
        }
    }
}

@Composable
fun ScenePreview(
    storyboard: Storyboard,
    index: Storyboard.Index,
    modifier: Modifier = Modifier,
    sceneMode: SceneMode = SceneMode.Preview,
) {
    CompositionLocalProvider(LocalStoryboard provides storyboard) {
        ScenePreview(
            scene = storyboard.scenes[index.sceneIndex],
            stateIndex = index.stateIndex,
            modifier = modifier,
            sceneMode = sceneMode,
            size = storyboard.size,
            decorator = storyboard.decorator,
        )
    }
}

@Composable
fun <T> SceneGallery(
    scene: Scene<T>,
    modifier: Modifier = Modifier,
    size: DpSize = Storyboard.DEFAULT_SIZE,
    decorator: SceneDecorator = SceneDecorator.None,
    sceneMode: SceneMode = SceneMode.Preview,
) {
    Text("Frame: Start")
    ScenePreview(
        scene, Frame.Start,
        modifier, size, decorator, sceneMode
    )

    for (stateIndex in scene.states.indices) {
        Text("Frame: $stateIndex")
        ScenePreview(
            scene, Frame.State(scene.states[stateIndex]),
            modifier, size, decorator, sceneMode
        )
    }

    Text("Frame: End")
    ScenePreview(
        scene, Frame.End,
        modifier, size, decorator, sceneMode
    )
}

@Composable
fun StoryGallery(
    storyboard: Storyboard,
    modifier: Modifier = Modifier,
    sceneMode: SceneMode = SceneMode.Preview,
) {
    CompositionLocalProvider(LocalStoryboard provides storyboard) {
        for (scene in storyboard.scenes) {
            SceneGallery(scene, modifier, storyboard.size, storyboard.decorator, sceneMode)
        }
    }
}

@Composable
fun StoryPreview(
    storyboard: Storyboard,
    modifier: Modifier = Modifier,
    sceneMode: SceneMode = SceneMode.Preview,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState())
            .background(Color.Transparent)
    ) {
        StoryGallery(storyboard, Modifier, sceneMode)
    }
}

@Composable
fun StoryGallery(
    modifier: Modifier = Modifier,
    name: String = "Gallery",
    description: String? = null,
    size: DpSize = Storyboard.DEFAULT_SIZE,
    decorator: SceneDecorator = SceneDecorator.None,
    sceneMode: SceneMode = SceneMode.Preview,
    block: StoryboardBuilder.() -> Unit,
) {
    StoryGallery(
        storyboard = Storyboard.build(name, description, size, decorator, block),
        modifier = modifier,
        sceneMode = sceneMode,
    )
}

@Composable
fun StoryPreview(
    modifier: Modifier = Modifier,
    name: String = "Preview",
    description: String? = null,
    size: DpSize = Storyboard.DEFAULT_SIZE,
    decorator: SceneDecorator = SceneDecorator.None,
    sceneMode: SceneMode = SceneMode.Preview,
    block: StoryboardBuilder.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState())
            .background(Color.Transparent)
    ) {
        StoryGallery(
            name = name,
            description = description,
            size = size,
            decorator = decorator,
            sceneMode = sceneMode,
            block = block,
        )
    }
}
