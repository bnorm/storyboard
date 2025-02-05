package dev.bnorm.storyboard.easel

import androidx.compose.runtime.*
import androidx.compose.ui.window.WindowState
import dev.bnorm.storyboard.core.Storyboard
import dev.bnorm.storyboard.easel.internal.WindowStateSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
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

@Stable
@Serializable
class DesktopState(
    @Serializable(with = WindowStateSerializer::class)
    val storyboard: WindowState = WindowState(),
    @Serializable(with = WindowStateSerializer::class)
    val notes: WindowState = WindowState(),
)

@Composable
fun rememberDesktopState(storyboard: Storyboard, format: StringFormat = Json): DesktopState? {
    return rememberDesktopState(Paths.get(".storyboard", "${storyboard.title}.json"), format)
}

@Composable
fun rememberDesktopState(path: Path, format: StringFormat = Json): DesktopState? {
    var state by remember { mutableStateOf<DesktopState?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            runInterruptible {
                if (path.exists()) {
                    val text = path.readText()
                    state = runCatching { format.decodeFromString(DesktopState.serializer(), text) }
                        .getOrElse { DesktopState() }
                } else {
                    path.createParentDirectories()
                    state = DesktopState()
                }
            }
        }
    }

    // Launch writer in a separate composeable so *this* composable does not rerun each time.
    LaunchedStateWriter(state, path, format)

    return state
}

@Composable
private fun LaunchedStateWriter(state: DesktopState?, file: Path, format: StringFormat) {
    state ?: return

    var hash = state.storyboard.hash()
    hash = 31 * hash + state.notes.hash()

    LaunchedEffect(hash, file) {
        try {
            withContext(Dispatchers.IO) {
                runInterruptible {
                    val text = format.encodeToString(DesktopState.serializer(), state)
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
