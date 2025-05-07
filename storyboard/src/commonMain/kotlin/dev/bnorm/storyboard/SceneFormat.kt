package dev.bnorm.storyboard

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize

class SceneFormat(
    /** Defines the pixel dimensions of each scene in a Storyboard. */
    val size: IntSize,
    /** Defines the pixel density of Storyboard scenes.*/
    val density: Density,
) {
    companion object {
        val Default = SceneFormat(
            size = IntSize(1920, 1080),
            density = Density(2f),
        )
    }
}

fun SceneFormat.toDpSize(): DpSize = with(density) { DpSize(size.width.toDp(), size.height.toDp()) }
