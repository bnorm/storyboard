package dev.bnorm.storyboard.ui

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
import dev.bnorm.storyboard.core.*
import kotlinx.collections.immutable.ImmutableList

private class PreviewSceneScope<T>(
    override val states: ImmutableList<T>,
    override val frame: Transition<out Frame<T>>,
) : SceneScope<T> {
    override val direction: AdvanceDirection get() = AdvanceDirection.Forward
}

@Composable
internal fun <T> ScenePreview(
    scene: Scene<T>,
    stateIndex: Int,
    modifier: Modifier = Modifier,
    size: DpSize = Storyboard.DEFAULT_SIZE,
    decorator: SceneDecorator = SceneDecorator.None,
    displayType: DisplayType = DisplayType.Preview,
) {
    SceneWrapper(size, decorator, displayType, modifier) {
        AnimatedVisibility(true) {
            key(scene, stateIndex) {
                val scope = PreviewSceneScope(
                    states = scene.states,
                    frame = updateTransition(Frame.State(scene.states[stateIndex])),
                )
                scene.content(scope)
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
    displayType: DisplayType = DisplayType.Preview,
) {
    SceneWrapper(size, decorator, displayType, modifier) {
        AnimatedVisibility(true) {
            key(scene, frame) {
                val scope = PreviewSceneScope(
                    states = scene.states,
                    frame = updateTransition(frame),
                )
                scene.content(scope)
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
    displayType: DisplayType = DisplayType.Preview,
) {
    CompositionLocalProvider(LocalStoryboard provides storyboard) {
        SceneWrapper(storyboard.size, storyboard.decorator, displayType, modifier) {
            AnimatedVisibility(true) {
                key(scene, frame) {
                    val scope = PreviewSceneScope(
                        states = scene.states,
                        frame = updateTransition(frame),
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
    modifier: Modifier = Modifier,
    displayType: DisplayType = DisplayType.Preview,
) {
    CompositionLocalProvider(LocalStoryboard provides storyboard) {
        ScenePreview(
            scene = storyboard.scenes[index.sceneIndex],
            stateIndex = index.stateIndex,
            modifier = modifier,
            displayType = displayType,
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
    displayType: DisplayType = DisplayType.Preview,
) {
    Text("Frame: Start")
    ScenePreview(
        scene, Frame.Start,
        modifier, size, decorator, displayType
    )

    for (stateIndex in scene.states.indices) {
        Text("Frame: $stateIndex")
        ScenePreview(
            scene, Frame.State(scene.states[stateIndex]),
            modifier, size, decorator, displayType
        )
    }

    Text("Frame: End")
    ScenePreview(
        scene, Frame.End,
        modifier, size, decorator, displayType
    )
}

@Composable
fun StoryGallery(
    storyboard: Storyboard,
    modifier: Modifier = Modifier,
    displayType: DisplayType = DisplayType.Preview,
) {
    CompositionLocalProvider(LocalStoryboard provides storyboard) {
        for (scene in storyboard.scenes) {
            SceneGallery(scene, modifier, storyboard.size, storyboard.decorator, displayType)
        }
    }
}

@Composable
fun StoryPreview(
    storyboard: Storyboard,
    modifier: Modifier = Modifier,
    displayType: DisplayType = DisplayType.Preview,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(vertical = 16.dp)
            .verticalScroll(rememberScrollState())
            .background(Color.Transparent)
    ) {
        StoryGallery(storyboard, Modifier, displayType)
    }
}

@Composable
fun StoryGallery(
    modifier: Modifier = Modifier,
    name: String = "Gallery",
    description: String? = null,
    size: DpSize = Storyboard.DEFAULT_SIZE,
    decorator: SceneDecorator = SceneDecorator.None,
    displayType: DisplayType = DisplayType.Preview,
    block: StoryboardBuilder.() -> Unit,
) {
    StoryGallery(
        storyboard = Storyboard.build(name, description, size, decorator, block),
        modifier = modifier,
        displayType = displayType,
    )
}

@Composable
fun StoryPreview(
    modifier: Modifier = Modifier,
    name: String = "Preview",
    description: String? = null,
    size: DpSize = Storyboard.DEFAULT_SIZE,
    decorator: SceneDecorator = SceneDecorator.None,
    displayType: DisplayType = DisplayType.Preview,
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
            displayType = displayType,
            block = block,
        )
    }
}
