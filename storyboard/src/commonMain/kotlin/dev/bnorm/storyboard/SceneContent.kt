package dev.bnorm.storyboard

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable

public fun interface SceneContent<T> {
    @Composable
    context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
    public fun SceneScope<T>.Content()
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
public fun <T> SceneScope<T>.Render(content: SceneContent<T>) {
    with(content) { Content() }
}
