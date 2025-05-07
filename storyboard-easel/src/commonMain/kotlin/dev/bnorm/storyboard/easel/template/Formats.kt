package dev.bnorm.storyboard.easel.template

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import dev.bnorm.storyboard.SceneFormat

/**
 * Replicates the format of Google Slides.
 *
 * Google Slides measures slides in inches, with a default size of 10 inches by 5.63 inches.
 * However, when zoom is set to 100%, this is visually the same as `960.dp` by `540.dp` in Compose.
 * To map measurements, the density is set such that `1.dp` is the same as 1 inch.
 *
 * Font sizes in Google Slides are represented in points,
 * with 100 points resulting in a text box with a height of 1.68 inches.
 * To map font sizes, the font scale is set such that `100.sp` results in a
 * ***[measured][androidx.compose.ui.text.TextMeasurer]*** height of `1.68.dp`.
 */
val SceneFormat.Companion.GoogleSlides: SceneFormat
    get() = SceneFormat(
        size = IntSize(960, 540),
        density = Density(
            // 1.dp = 1 inch = 96 pixels.
            density = 96f,
            // 100.sp = 1.45.dp which measures to 168.dp.
            fontScale = .0145f,
        ),
    )

/**
 * Replicates the format of Apple Keynote.
 *
 * Apple Keynote measures slides in points, with a default size of 1920 points by 1080 points.
 * To map measurements, the density is set such that `1.dp` is the same as 1 point.
 *
 * Font sizes in Keynote are also represented in points.
 * To map font sizes, the font scale is set such that `1.sp` is the same as 1 point.
 */
val SceneFormat.Companion.Keynote: SceneFormat
    get() = SceneFormat(
        size = IntSize(1920, 1080),
        density = Density(
            density = 1f,
            fontScale = 1f,
        ),
    )
