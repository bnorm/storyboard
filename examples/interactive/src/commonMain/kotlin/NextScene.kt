import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.NextScene() {
    scene {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text("What will you build?", fontSize = MaterialTheme.typography.displayMedium.fontSize)
        }
    }
}
