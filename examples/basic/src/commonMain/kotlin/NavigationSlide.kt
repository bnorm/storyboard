import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.easel.notes.NotesTab
import dev.bnorm.storyboard.easel.template.Body
import dev.bnorm.storyboard.easel.template.Header

fun StoryboardBuilder.NavigationSlide() = slide(
    stateCount = 5,
) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Header { Text("Navigation") }
        Divider(color = MaterialTheme.colors.primary)
        Body {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("After slide gains focus, advance with arrow keys.")
                if (currentState >= 1) Text("    • Slides can also be advanced with the mouse.")
                if (currentState >= 3) Text("    • Press 'Esc' to see overview! (arrows to navigate and 'Enter' to open slide)")
                if (currentState >= 4) Text("    • Press 'F2' to see next slide preview and notes. (Desktop only!)")
            }

            if (currentState >= 2) {
                Text("Down here!", style = MaterialTheme.typography.h6, modifier = Modifier.align(Alignment.BottomEnd))
            }
        }
    }

    NotesTab("Notes") {
        Text("Hello! I'm a note!")
    }
}
