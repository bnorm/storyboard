package dev.bnorm.storyboard.easel

import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.Scene

interface SceneFrame<T> {
    val scene: Scene<T>
    val frame: Frame<T>
}
