package dev.bnorm.storyboard.easel.template

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.easel.sharedElement

@Composable
fun Header(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(modifier.fillMaxWidth()) {
        ProvideTextStyle(MaterialTheme.typography.h3) {
            content()
        }
    }
}

private object SharedHeaderKey

@Composable
context(_: AnimatedVisibilityScope, scope: SharedTransitionScope)
fun SharedHeader(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Header(
        modifier = modifier.sharedElement(
            sharedContentState = scope.rememberSharedContentState(SharedHeaderKey),
        ),
        content = content
    )
}
