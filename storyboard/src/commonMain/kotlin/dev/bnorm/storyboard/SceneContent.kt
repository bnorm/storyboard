package dev.bnorm.storyboard

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable

fun interface SceneContent<T> {
    @Composable
    context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
    fun SceneScope<T>.Content()
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
fun <T> SceneScope<T>.Render(content: SceneContent<T>) {
    with(content) { Content() }
}
