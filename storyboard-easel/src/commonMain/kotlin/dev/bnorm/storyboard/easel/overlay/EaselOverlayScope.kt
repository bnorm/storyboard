package dev.bnorm.storyboard.easel.overlay

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.ui.Modifier

interface EaselOverlayScope : BoxScope {
    /**
     * Enables tracking of the pointer when it enters or exists a Composable.
     * This allows the overlay to decrease the transparency of the overlay even more
     * when the pointer is over an element in the overlay.
     */
    fun Modifier.overlayElement(): Modifier
}
