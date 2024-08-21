package dev.bnorm.storyboard.easel

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import dev.bnorm.storyboard.core.AdvanceDirection
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.easel.internal.requestFocus

@Composable
fun Storyboard(storyboard: Storyboard, modifier: Modifier = Modifier) {
    val holder = rememberSaveableStateHolder()
    var showOverview by remember { mutableStateOf(false) }

    fun handleKeyEvent(event: KeyEvent): Boolean {
        if (event.type == KeyEventType.KeyUp && event.key == Key.Escape) {
            showOverview = true
            return true
        }
        return false
    }

    Box(modifier = modifier) {
        SharedTransitionLayout {
            AnimatedContent(
                targetState = showOverview,
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) }
            ) { isOverview ->
                if (isOverview) {
                    StoryboardOverview(
                        storyboard = storyboard,
                        onExitOverview = {
                            storyboard.jumpTo(it)
                            showOverview = false
                        },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    holder.SaveableStateProvider(storyboard) {
                        EmbeddedStoryboard(
                            storyboard = storyboard,
                            modifier = Modifier
                                .fillMaxSize()
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = OverviewCurrentSlide),
                                    animatedVisibilityScope = this@AnimatedContent
                                )
                                .requestFocus()
                                .onStoryboardNavigation(storyboard = storyboard)
                                .onKeyEvent { handleKeyEvent(it) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun Modifier.onStoryboardNavigation(storyboard: Storyboard): Modifier {
    var keyHeld = false
    fun handle(event: KeyEvent): Boolean {
        when (event.type) {
            KeyEventType.KeyDown -> {
                val wasHeld = keyHeld
                keyHeld = true

                when (event.key) {
                    Key.DirectionRight,
//                    Key.DirectionDown,
//                    Key.Enter,
//                    Key.Spacebar,
                        -> return storyboard.advance(AdvanceDirection.Forward, jump = wasHeld)

                    Key.DirectionLeft,
//                    Key.DirectionUp,
//                    Key.Backspace,
                        -> return storyboard.advance(AdvanceDirection.Backward, jump = wasHeld)
                }
            }

            KeyEventType.KeyUp -> {
                keyHeld = false
            }
        }

        return false
    }

    return onKeyEvent { handle(it) }
}
