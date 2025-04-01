package dev.bnorm.storyboard.easel.template

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.easel.sharedElement

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
context(_: AnimatedVisibilityScope, scope: SharedTransitionScope)
fun SharedTitle(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Title(
        modifier = modifier.sharedElement(
            sharedContentState = scope.rememberSharedContentState(SharedTitleKey),
        ),
        content = content
    )
}
