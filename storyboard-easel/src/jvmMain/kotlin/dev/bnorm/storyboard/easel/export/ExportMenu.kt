package dev.bnorm.storyboard.easel.export

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.MenuBarScope
import dev.bnorm.storyboard.Storyboard
import kotlinx.coroutines.launch

@Composable
internal fun MenuBarScope.ExportMenu(exporter: StoryboardPdfExporter, storyboard: Storyboard) {
    val coroutineScope = rememberCoroutineScope()

    Menu("Export") {
        Item(
            text = "PDF",
            enabled = exporter.status == null,
            onClick = {
                coroutineScope.launch {
                    exporter.export(storyboard)
                }
            },
        )
    }
}