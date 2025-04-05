package dev.bnorm.storyboard

import dev.bnorm.storyboard.DisplayType.Preview
import dev.bnorm.storyboard.DisplayType.Story

/**
 * How a scene is being displayed. Currently, there are two types: [Story] and [Preview].
 */
// TODO review the enum value names
enum class DisplayType {
    /** Displaying a scene as part of a larger story. */
    Story,

    /** Displaying a scene individually for review. */
    Preview,
}
