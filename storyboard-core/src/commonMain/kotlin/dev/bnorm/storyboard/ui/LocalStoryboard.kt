package dev.bnorm.storyboard.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import dev.bnorm.storyboard.core.Storyboard

val LocalStoryboard = compositionLocalOf<Storyboard> { error("No Storyboard provided") }

@Composable
fun ProvideStoryboard(storyboard: Storyboard, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalStoryboard provides storyboard) {
        content()
    }
}
