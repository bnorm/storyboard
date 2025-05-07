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
fun Story(storyState: StoryState, modifier: Modifier = Modifier) {
    val holder = rememberSaveableStateHolder()
    CompositionLocalProvider(LocalStoryboard provides storyState.storyboard) {
        SceneWrapper(storyState.storyboard.format, storyState.storyboard.decorator, SceneMode.Story, modifier) {
            val stateFrame = storyState.rememberTransition()
            stateFrame.createChildTransition { it.scene }.AnimatedContent(
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
                        SceneContent(scene, stateFrame)
                    }
                }
            }
        }
    }
}

private class StorySceneScope<T>(
    override val states: ImmutableList<T>,
    override val transition: Transition<out Frame<T>>,
) : SceneScope<T>

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun <T> SceneContent(
    scene: Scene<T>,
    storyFrame: Transition<StoryState.StoryFrame<*>>,
) {
    val frame = storyFrame.createChildTransition {
        @Suppress("UNCHECKED_CAST")
        when {
            scene > it.scene -> Frame.Start
            scene < it.scene -> Frame.End
            else -> it.frame as Frame<T>
        }
    }

    val scope = remember(scene, frame) {
        StorySceneScope(
            states = scene.states,
            transition = frame,
        )
    }

    scope.Render(scene.content)
}

@Composable
internal fun SceneWrapper(
    format: SceneFormat,
    decorator: SceneDecorator,
    sceneMode: SceneMode,
    modifier: Modifier = Modifier,
    content: @Composable context(SharedTransitionScope) () -> Unit,
) {
    FixedSize(size = format.size, modifier = modifier) {
        CompositionLocalProvider(
            LocalSceneMode provides sceneMode,
            LocalDensity provides format.density
        ) {
            decorator.decorate {
                SharedTransitionLayout {
                    content()
                }
            }
        }
    }
}
