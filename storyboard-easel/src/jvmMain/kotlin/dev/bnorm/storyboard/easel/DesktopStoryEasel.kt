package dev.bnorm.storyboard.easel

import androidx.compose.animation.core.Transition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.*
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.assist.*
import dev.bnorm.storyboard.easel.export.ExportMenu
import dev.bnorm.storyboard.easel.export.ExportProgressPopup
import dev.bnorm.storyboard.easel.export.StoryboardPdfExporter
import dev.bnorm.storyboard.easel.overlay.OverlayNavigation
import dev.bnorm.storyboard.easel.overlay.StoryOverlayScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalStoryStateApi::class)
@Composable
fun ApplicationScope.DesktopStoryEasel(storyboard: Storyboard) {
    val storyState = rememberStoryState { storyboard }
    DesktopStoryEasel(storyState)
}

/**
 * This function is designed to be used in combination with Compose Hot-Reload.
 * Make sure a [Storyboard] is already attached to the [StoryState] with [StoryState.updateStoryboard].
 * Each time the storyboard is changed, it can be updated on the state to not revert the story to the first index.
 */
@Composable
@ExperimentalStoryStateApi
fun ApplicationScope.DesktopStoryEasel(
    storyState: StoryState,
    overlay: @Composable StoryOverlayScope.() -> Unit = {
        OverlayNavigation(storyState)
    },
    captions: ImmutableList<Caption> = persistentListOf(),
) {
    DesktopStoryEasel(storyState, storyState.rememberTransition(), overlay, captions)
}

/**
 * This function is designed to be used in combination with Compose Hot-Reload.
 * Make sure a [Storyboard] is already attached to the [StoryState] with [StoryState.updateStoryboard].
 * Each time the storyboard is changed, it can be updated on the state to not revert the story to the first index.
 */
@Composable
@ExperimentalStoryStateApi
fun ApplicationScope.DesktopStoryEasel(
    storyState: StoryState,
    transition: Transition<SceneFrame<*>>,
    overlay: @Composable StoryOverlayScope.() -> Unit = {
        OverlayNavigation(storyState)
    },
    captions: ImmutableList<Caption> = persistentListOf(),
    menuBar: @Composable MenuBarScope.() -> Unit = {},
) {
    val desktopState = rememberDesktopState(storyState.storyboard)
    val assistantState = remember(storyState, captions) { StoryAssistantState(storyState, captions) }
    val exporter = remember(storyState.storyboard) { StoryboardPdfExporter() }

    if (desktopState == null) {
        // Need a window to keep the application from closing.
        // TODO splash screen?
        Window(onCloseRequest = ::exitApplication, visible = false) {}
        return
    }

    CompositionLocalProvider(LocalCaptions provides assistantState.captions) {
        Window(
            onCloseRequest = { exitApplication() },
            state = desktopState.story,
            title = storyState.storyboard.title,
        ) {
            MenuBar {
                DesktopMenu(storyState.storyboard, desktopState.story, window) {
                    StoryAssistantMenu(assistantState)
                    ExportMenu(exporter, storyState.storyboard)
                    menuBar()
                }
            }

            StoryEasel(
                storyController = storyState,
                transition = transition,
                overlay = {
                    // Only display overlay navigation when *not* fullscreen.
                    if (desktopState.story.placement != WindowPlacement.Fullscreen) {
                        overlay()
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
            )

            exporter.status?.let { ExportProgressPopup(it) }
        }
    }

    StoryAssistantWindow(
        assistantState = assistantState,
        menuBar = {
            DesktopMenu(storyState.storyboard, desktopState.story, window = null) {
                StoryAssistantMenu(assistantState)
                ExportMenu(exporter, storyState.storyboard)
                menuBar()
            }
        },
        windowState = desktopState.assistant,
    )
}
