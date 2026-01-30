package dev.bnorm.storyboard.easel

import androidx.compose.runtime.Composable
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.overlay.OverlayNavigation
import dev.bnorm.storyboard.easel.overlay.StoryOverlayDecorator
import kotlinx.browser.window

@Composable
fun WebEasel(storyboard: () -> Storyboard) {
    val easel = rememberEasel(window, storyboard)
    WebEasel(easel)
}

@Composable
fun WebEasel(
    easel: Easel,
    decorator: SceneDecorator = StoryOverlayDecorator { OverlayNavigation(easel) },
) {
    StoryEasel(
        easel = easel,
        decorator = decorator,
    )
}
