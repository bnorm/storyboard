package dev.bnorm.storyboard.easel.internal

import androidx.compose.ui.unit.DpSize

internal val DpSize.aspectRatio: Float
    get() = width / height
