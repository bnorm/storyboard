package dev.bnorm.storyboard.easel

import androidx.compose.runtime.*
import androidx.compose.ui.window.WindowState
import dev.bnorm.storyboard.easel.internal.WindowStateSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption.*
import kotlin.io.path.createParentDirectories
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Composable
fun rememberWindowState(fileName: String, format: StringFormat = Json): WindowState? {
    return rememberWindowState(Paths.get(".storyboard", "$fileName.json"), format)
}

@Composable
fun rememberWindowState(path: Path, format: StringFormat = Json): WindowState? {
    var state by remember { mutableStateOf<WindowState?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            runInterruptible {
                if (path.exists()) {
                    val text = path.readText()
                    state = runCatching { format.decodeFromString(WindowStateSerializer, text) }
                        .getOrElse { WindowState() }
                } else {
                    path.createParentDirectories()
                    state = WindowState()
                }
            }
        }
    }

    // Launch writer in a separate composable so *this* composable does not rerun each time.
    LaunchedStateWriter(state, path, format)

    return state
}

@Composable
private fun LaunchedStateWriter(state: WindowState?, file: Path, format: StringFormat) {
    state ?: return
    LaunchedEffect(state.hash(), file) {
        try {
            withContext(Dispatchers.IO) {
                runInterruptible {
                    val text = format.encodeToString(WindowStateSerializer, state)
                    file.writeText(text, options = arrayOf(WRITE, CREATE, TRUNCATE_EXISTING))
                }
            }
        } catch (_: IOException) {
        }
    }
}

private fun WindowState.hash(): Int {
    var result = placement.hashCode()
    result = 31 * result + isMinimized.hashCode()
    result = 31 * result + position.hashCode()
    result = 31 * result + size.hashCode()
    return result
}
