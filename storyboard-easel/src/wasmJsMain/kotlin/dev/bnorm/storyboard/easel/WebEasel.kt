package dev.bnorm.storyboard.easel

import androidx.compose.runtime.Composable
import dev.bnorm.storyboard.Decorator
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.overlay.OverlayNavigation
import dev.bnorm.storyboard.easel.overlay.StoryOverlayDecorator
import dev.bnorm.storyboard.plus
import kotlinx.browser.window

@Composable
fun WebEasel(storyboard: () -> Storyboard) {
    val easel = rememberEasel(window, storyboard)
    WebEasel(easel)
}

@Composable
fun WebEasel(
    easel: Easel,
    decorator: Decorator = OverviewDecorator(easel) + StoryOverlayDecorator { OverlayNavigation(easel) },
) {
    Story(
        easel = easel,
        decorator = decorator,
    )
}
