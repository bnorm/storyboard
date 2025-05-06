package dev.bnorm.storyboard.easel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.movableContentWithReceiverOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.MenuBarScope
import androidx.compose.ui.window.Window
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.assist.*
import dev.bnorm.storyboard.easel.export.ExportMenu
import dev.bnorm.storyboard.easel.export.StoryboardPdfExporter
import dev.bnorm.storyboard.easel.overlay.OverlayNavigation
import dev.bnorm.storyboard.easel.overlay.StoryOverlayScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalStoryStateApi::class)
@Composable
fun ApplicationScope.DesktopStoryEasel(storyboard: Storyboard) {
    val storyState = rememberStoryState()
    storyState.updateStoryboard(storyboard)
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
    val desktopState = rememberDesktopState(storyState.storyboard)
    val assistantState = remember(storyState, captions) { StoryAssistantState(storyState, captions) }
    val exporter = remember(storyState.storyboard) { StoryboardPdfExporter() }

    if (desktopState == null) {
        // Need a window to keep the application from closing.
        // TODO splash screen?
        Window(onCloseRequest = ::exitApplication, visible = false) {}
        return
    }

    val menuBar = movableContentWithReceiverOf<MenuBarScope> {
        DesktopMenu(storyState.storyboard, desktopState.story) {
            StoryAssistantMenu(assistantState)
            ExportMenu(exporter, storyState)
        }
    }

    CompositionLocalProvider(LocalCaptions provides assistantState.captions) {
        StoryEaselWindow(
            storyState = storyState,
            onCloseRequest = ::exitApplication,
            windowState = desktopState.story,
            menuBar = menuBar,
            overlay = overlay,
            exporter = exporter,
        )
    }

    StoryAssistantWindow(
        assistantState = assistantState,
        menuBar = menuBar,
        windowState = desktopState.assistant,
    )
}

