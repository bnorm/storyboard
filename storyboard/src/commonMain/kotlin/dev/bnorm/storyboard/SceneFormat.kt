package dev.bnorm.storyboard

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import kotlin.jvm.JvmField

public class SceneFormat(
    /** Defines the pixel dimensions of each scene in a Storyboard. */
    public val size: IntSize,
    /** Defines the pixel density of Storyboard scenes.*/
    public val density: Density,
) {
    public companion object {
        @JvmField
        public val Default: SceneFormat = SceneFormat(
            size = IntSize(1920, 1080),
            density = Density(2f),
        )
    }
}

public fun SceneFormat.toDpSize(): DpSize =
    with(density) { DpSize(size.width.toDp(), size.height.toDp()) }
