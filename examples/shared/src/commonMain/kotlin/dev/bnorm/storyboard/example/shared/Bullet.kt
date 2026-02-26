package dev.bnorm.storyboard.example.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun Bullet(text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("•")
        Text(text)
    }
}

@Composable
fun Bullet(text: AnnotatedString) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("•")
        Text(text)
    }
}
