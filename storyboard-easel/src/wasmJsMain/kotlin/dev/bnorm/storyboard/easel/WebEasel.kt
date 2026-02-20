package dev.bnorm.storyboard.easel

import androidx.compose.runtime.Composable
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.overlay.EaselOverlay
import dev.bnorm.storyboard.easel.overlay.OverlayNavigation
import dev.bnorm.storyboard.easel.overview.EaselOverview
import kotlinx.browser.window

@Composable
fun WebEasel(storyboard: () -> Storyboard) {
    WebEasel(rememberAnimatic(window, storyboard))
}

@Composable
fun WebEasel(animatic: Animatic) {
    EaselOverview(animatic) {
        EaselOverlay(overlay = { OverlayNavigation(animatic) }) {
            Easel(animatic)
        }
    }
}
