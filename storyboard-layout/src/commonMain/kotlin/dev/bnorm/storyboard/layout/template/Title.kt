package dev.bnorm.storyboard.layout.template

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Title(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(modifier) {
        ProvideTextStyle(MaterialTheme.typography.h1) {
            content()
        }
    }
}

private object SharedTitleKey

@Composable
context(animatedVisibilityScope: AnimatedVisibilityScope, sharedTransitionScope: SharedTransitionScope)
fun SharedTitle(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    with(sharedTransitionScope) {
        Title(
            modifier = modifier.sharedElement(
                sharedContentState = rememberSharedContentState(SharedTitleKey),
                animatedVisibilityScope = animatedVisibilityScope,
            ),
            content = content
        )
    }
}
