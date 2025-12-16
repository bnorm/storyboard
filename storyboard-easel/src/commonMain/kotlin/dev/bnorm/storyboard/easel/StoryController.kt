package dev.bnorm.storyboard.easel

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import dev.bnorm.storyboard.AdvanceDirection
import dev.bnorm.storyboard.Storyboard
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// TODO better name? - just Story?
interface StoryController {
    val storyboard: Storyboard
    val currentIndex: Storyboard.Index
    val targetIndex: Storyboard.Index
    val storyDistance: Float
    val storyProgress: Float
    val advancementDistance: Float
    val advancementProgress: Float
    suspend fun advance(direction: AdvanceDirection): Boolean
    suspend fun jumpTo(index: Storyboard.Index): Boolean
}

@Composable
fun Modifier.onStoryNavigation(storyController: StoryController): Modifier {
    val handle = rememberKeyHandler(storyController)
    return onKeyEvent { handle(it) }
}

@Composable
fun rememberKeyHandler(storyController: StoryController): (KeyEvent) -> Boolean {
    val coroutineScope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    fun handle(event: KeyEvent): Boolean {
        when (event.type) {
            KeyEventType.KeyDown -> {
                when (event.key) {
                    Key.DirectionRight,
//                    Key.DirectionDown,
//                    Key.Enter,
//                    Key.Spacebar,
                        -> {
                        job?.cancel()
                        job = coroutineScope.launch {
                            storyController.advance(AdvanceDirection.Forward)
                            job = null
                        }
                        return true
                    }

                    Key.DirectionLeft,
//                    Key.DirectionUp,
//                    Key.Backspace,
                        -> {
                        job?.cancel()
                        job = coroutineScope.launch {
                            storyController.advance(AdvanceDirection.Backward)
                            job = null
                        }
                        return true
                    }
                }
            }
        }

        return false
    }

    return ::handle
}
