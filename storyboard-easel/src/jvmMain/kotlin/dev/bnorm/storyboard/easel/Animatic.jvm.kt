package dev.bnorm.storyboard.easel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.bnorm.storyboard.Storyboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runInterruptible
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
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
fun rememberAnimatic(
    fileName: String,
    format: StringFormat = Json,
    storyboard: () -> Storyboard,
): Animatic {
    val animatic = rememberAnimatic { storyboard() }
    AnimaticPersistenceEffect(animatic, fileName, format)
    return animatic
}

@Composable
fun AnimaticPersistenceEffect(
    animatic: Animatic,
    fileName: String,
    format: StringFormat = Json,
) {
    // TODO extract this directory to somewhere as a constant?
    val path = Paths.get(".storyboard", "${fileName.lowercase()}.json")
    var loaded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        val index = runInterruptible(Dispatchers.IO) {
            if (path.exists()) {
                val text = path.readText()
                runCatching { format.decodeFromString(StoryboardIndexSerializer, text) }
                    .getOrNull()
            } else {
                path.createParentDirectories()
                null
            }
        }

        if (index != null) {
            val indices = animatic.storyboard.indices
            val searchIndex = indices.binarySearch(index)
            val jumpIndex = when {
                searchIndex >= 0 -> indices[searchIndex]
                else -> indices[(-searchIndex - 1).coerceAtMost(indices.lastIndex)]
            }
            animatic.jumpTo(jumpIndex)
        }

        loaded = true
    }

    if (loaded) {
        LaunchedStoryboardIndexWriter(animatic, path, format)
    }
}

@Composable
private fun LaunchedStoryboardIndexWriter(animatic: Animatic, path: Path, format: StringFormat) {
    val index = animatic.currentIndex
    LaunchedEffect(index, path, format) {
        try {
            runInterruptible(Dispatchers.IO) {
                val text = format.encodeToString(StoryboardIndexSerializer, index)
                path.writeText(text, options = arrayOf(WRITE, CREATE, TRUNCATE_EXISTING))
            }
        } catch (_: IOException) {
        }
    }
}


@OptIn(ExperimentalSerializationApi::class)
internal object StoryboardIndexSerializer : KSerializer<Storyboard.Index> {
    override val descriptor = buildClassSerialDescriptor("StoryboardIndex") {
        element<Int>("sceneIndex")
        element<Int>("frameIndex")
    }

    override fun serialize(encoder: Encoder, value: Storyboard.Index) = encoder.encodeStructure(descriptor) {
        encodeIntElement(descriptor, 0, value.sceneIndex)
        encodeIntElement(descriptor, 1, value.frameIndex)
    }

    override fun deserialize(decoder: Decoder): Storyboard.Index = decoder.decodeStructure(descriptor) {
        var sceneIndex = 0
        var frameIndex = 0
        if (decodeSequentially()) {
            sceneIndex = decodeIntElement(descriptor, 0)
            frameIndex = decodeIntElement(descriptor, 1)
        } else while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> sceneIndex = decodeIntElement(descriptor, 0)
                1 -> frameIndex = decodeIntElement(descriptor, 1)
                CompositeDecoder.DECODE_DONE -> break
                else -> error("Unexpected index: $index")
            }
        }
        Storyboard.Index(sceneIndex, frameIndex)
    }
}
