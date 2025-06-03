package dev.bnorm.storyboard.easel.template

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import dev.bnorm.storyboard.easel.LocalSceneMode
import dev.bnorm.storyboard.SceneDecorator
import dev.bnorm.storyboard.easel.SceneMode
import dev.bnorm.storyboard.easel.StoryState

fun SceneIndexDecorator(state: StoryState): SceneDecorator = SceneDecorator { content ->
    content()

    Box(contentAlignment = Alignment.BottomStart, modifier = Modifier.fillMaxSize()) {
        Surface(modifier = Modifier.alpha(0.5f)) {
            SceneIndex(state, MaterialTheme.typography.caption)
        }
    }
}

@Composable
private fun SceneIndex(state: StoryState, style: TextStyle) {
    if (LocalSceneMode.current == SceneMode.Preview) return
    Text(text = state.currentIndex.toString(), style = style)
}
