import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.RevealEach
import dev.bnorm.storyboard.layout.template.Body
import dev.bnorm.storyboard.layout.template.Header
import dev.bnorm.storyboard.toValue

@OptIn(ExperimentalTransitionApi::class)
fun StoryboardBuilder.ButtonScene() {
    scene(frameCount = 6) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Header { Text("Buttons") }
            Divider(color = MaterialTheme.colors.primary, thickness = 4.dp)
            Body {
                Column(
                    Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    RevealEach(transition.createChildTransition { it.toValue() }) {
                        item { Text("• Scenes can contain interactive content.") }
                        item { Text("• For example, buttons!") }
                        item { IncreaseDecreaseCounter() }
                        item { Text("• Try them out!") }
                        item { Text("• Using `rememberSaveable` allows state to be preserved across scenes.") }
                        item { Text("• Navigate to the next scene and back to see it in action!") }
                    }
                }
            }
        }
    }
}

@Composable
private fun IncreaseDecreaseCounter() {
    var count by rememberSaveable { mutableIntStateOf(0) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = { count-- }, modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)) {
            Text("-")
        }
        Spacer(Modifier.size(16.dp))
        Box(modifier = Modifier.widthIn(min = 100.dp), contentAlignment = Alignment.Center) {
            Text(
                "$count",
                fontSize = MaterialTheme.typography.h3.fontSize,
            )
        }
        Spacer(Modifier.size(16.dp))
        Button(onClick = { count++ }, modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)) {
            Text("+")
        }
    }
}
