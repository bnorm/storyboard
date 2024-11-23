package dev.bnorm.storyboard.easel.template

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.core.SlideScope

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
fun SlideScope<*>.SharedHeader(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Header(
        modifier = modifier.sharedElement(
            state = rememberSharedContentState(SharedHeaderKey),
            animatedVisibilityScope = this
        ),
        content = content
    )
}
