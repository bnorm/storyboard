import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.scene

fun StoryboardBuilder.NextScene() {
    scene {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text("What will you build?", fontSize = MaterialTheme.typography.h2.fontSize)
        }
    }
}
