package dev.bnorm.storyboard.easel

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import dev.bnorm.storyboard.*
import dev.bnorm.storyboard.easel.internal.FixedSize
import kotlinx.collections.immutable.ImmutableList

@Composable
fun Easel(
    animatic: Animatic,
    mode: SceneMode = SceneMode.Story,
    modifier: Modifier = Modifier,
) {
    Easel(
        storyboard = animatic.storyboard,
        transition = animatic.transition,
        mode = mode,
        modifier = modifier
    )
}

@Composable
fun Easel(
    storyboard: Storyboard,
    transition: Transition<out Animatic.State<*>>,
    mode: SceneMode = SceneMode.Story,
    modifier: Modifier = Modifier,
) {
    val holder = rememberSaveableStateHolder()
    SceneWrapper(storyboard, mode, modifier) {
        SharedTransitionLayout {
            val sceneTransition = transition.createChildTransition { it.scene }
            sceneTransition.AnimatedContent(
                transitionSpec = {
                    val direction = when {
                        targetState > initialState -> AdvanceDirection.Forward
                        else -> AdvanceDirection.Backward
                    }
                    targetState.enterTransition(direction) togetherWith
                            initialState.exitTransition(direction)
                }
            ) { scene ->
                holder.SaveableStateProvider(scene) {
                    Box(Modifier.fillMaxSize()) {
                        SceneContent(scene, transition)
                    }
                }
            }
        }
    }
}

private class StorySceneScope<T>(
    override val frames: ImmutableList<T>,
    override val transition: Transition<out Frame<T>>,
) : SceneScope<T>

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun <T> SceneContent(
    scene: Scene<T>,
    sceneFrame: Transition<out Animatic.State<*>>,
) {
    val frame = sceneFrame.createChildTransition {
        @Suppress("UNCHECKED_CAST")
        when {
            scene > it.scene -> Frame.Start
            scene < it.scene -> Frame.End
            else -> it.frame as Frame<T>
        }
    }

    val scope = remember(scene, frame) {
        StorySceneScope(
            frames = scene.frames,
            transition = frame,
        )
    }

    scope.Render(scene.content)
}

@Composable
internal fun SceneWrapper(
    format: SceneFormat,
    decorator: Decorator,
    sceneMode: SceneMode,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalSceneMode provides sceneMode,
        LocalDensity provides format.density,
    ) {
        FixedSize(size = format.size, modifier = modifier) {
            decorator.decorate {
                content()
            }
        }
    }
}

@Composable
internal fun SceneWrapper(
    storyboard: Storyboard,
    sceneMode: SceneMode,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalStoryboard provides storyboard,
        LocalSceneMode provides sceneMode,
        LocalDensity provides storyboard.format.density
    ) {
        FixedSize(size = storyboard.format.size, modifier = modifier) {
            storyboard.decorator.decorate {
                content()
            }
        }
    }
}
