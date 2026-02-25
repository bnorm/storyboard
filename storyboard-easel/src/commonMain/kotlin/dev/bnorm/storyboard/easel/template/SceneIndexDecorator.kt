package dev.bnorm.storyboard.easel.template

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import dev.bnorm.storyboard.easel.LocalSceneMode
import dev.bnorm.storyboard.ContentDecorator
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.SceneMode

fun SceneIndexDecorator(currentIndex: State<Storyboard.Index>): ContentDecorator = ContentDecorator { content ->
    content()

    Box(contentAlignment = Alignment.BottomStart, modifier = Modifier.fillMaxSize()) {
        Surface(modifier = Modifier.alpha(0.5f)) {
            SceneIndex(currentIndex, MaterialTheme.typography.caption)
        }
    }
}

@Composable
private fun SceneIndex(currentIndex: State<Storyboard.Index>, style: TextStyle) {
    if (LocalSceneMode.current == SceneMode.Preview) return
    Text(text = currentIndex.value.toString(), style = style)
}
