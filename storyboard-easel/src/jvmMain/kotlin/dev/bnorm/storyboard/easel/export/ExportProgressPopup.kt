package dev.bnorm.storyboard.easel.export

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

@Composable
fun ExportProgressPopup(status: ExportStatus) {
    Popup(alignment = Alignment.Center) {
        Surface(
            elevation = 8.dp,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.border(2.dp, MaterialTheme.colors.primary, RoundedCornerShape(8.dp))
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(status.message)
                Spacer(Modifier.padding(4.dp))
                LinearProgressIndicator(status.progress)
            }
        }
    }
}
