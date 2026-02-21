package dev.bnorm.storyboard.easel

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
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
    frameIndex: Int,
    modifier: Modifier = Modifier,
    format: SceneFormat = SceneFormat.Default,
    decorator: Decorator = Decorator.None,
    sceneMode: SceneMode = SceneMode.Preview,
) {
    SceneWrapper(format, decorator, sceneMode, modifier) {
        SharedTransitionLayout {
            AnimatedVisibility(true) {
                key(scene, frameIndex) {
                    val scope = PreviewSceneScope(
                        states = scene.states,
                        transition = updateTransition(Frame.State(scene.states[frameIndex])),
                    )
                    scope.Render(scene.content)
                }
            }
        }
    }
}

@Composable
fun <T> ScenePreview(
    scene: Scene<T>,
    frame: Frame<T>,
    modifier: Modifier = Modifier,
    format: SceneFormat = SceneFormat.Default,
    decorator: Decorator = Decorator.None,
    sceneMode: SceneMode = SceneMode.Preview,
) {
    SceneWrapper(format, decorator, sceneMode, modifier) {
        SharedTransitionLayout {
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
fun <T> ScenePreview(
    storyboard: Storyboard,
    scene: Scene<T>,
    frame: Frame<T>,
    modifier: Modifier = Modifier,
    sceneMode: SceneMode = SceneMode.Preview,
) {
    CompositionLocalProvider(LocalStoryboard provides storyboard) {
        SceneWrapper(storyboard.format, storyboard.decorator, sceneMode, modifier) {
            SharedTransitionLayout {
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
            frameIndex = index.frameIndex,
            modifier = modifier,
            sceneMode = sceneMode,
            format = storyboard.format,
            decorator = storyboard.decorator,
        )
    }
}

@Composable
fun <T> SceneGallery(
    scene: Scene<T>,
    modifier: Modifier = Modifier,
    format: SceneFormat = SceneFormat.Default,
    decorator: Decorator = Decorator.None,
    sceneMode: SceneMode = SceneMode.Preview,
) {
    Text("Frame: Start")
    ScenePreview(
        scene, Frame.Start,
        modifier, format, decorator, sceneMode
    )

    for (frameIndex in scene.states.indices) {
        Text("Frame: $frameIndex")
        ScenePreview(
            scene, Frame.State(scene.states[frameIndex]),
            modifier, format, decorator, sceneMode
        )
    }

    Text("Frame: End")
    ScenePreview(
        scene, Frame.End,
        modifier, format, decorator, sceneMode
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
            SceneGallery(scene, modifier, storyboard.format, storyboard.decorator, sceneMode)
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
    format: SceneFormat = SceneFormat.Default,
    decorator: Decorator = Decorator.None,
    sceneMode: SceneMode = SceneMode.Preview,
    block: StoryboardBuilder.() -> Unit,
) {
    StoryGallery(
        storyboard = Storyboard.build(name, description, format, decorator, block),
        modifier = modifier,
        sceneMode = sceneMode,
    )
}

@Composable
fun StoryPreview(
    modifier: Modifier = Modifier,
    name: String = "Preview",
    description: String? = null,
    format: SceneFormat = SceneFormat.Default,
    decorator: Decorator = Decorator.None,
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
            format = format,
            decorator = decorator,
            sceneMode = sceneMode,
            block = block,
        )
    }
}
