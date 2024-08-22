@file:OptIn(ExperimentalSerializationApi::class)

package dev.bnorm.storyboard.easel.internal

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*

internal object DpSizeSerializer : KSerializer<DpSize> {
    override val descriptor = buildClassSerialDescriptor("DpSize") {
        element<Float>("width")
        element<Float>("height")
    }

    override fun serialize(encoder: Encoder, value: DpSize) = encoder.encodeStructure(descriptor) {
        encodeFloatElement(descriptor, 0, value.width.value)
        encodeFloatElement(descriptor, 1, value.height.value)
    }

    override fun deserialize(decoder: Decoder): DpSize = decoder.decodeStructure(descriptor) {
        var width = -1.0f
        var height = -1.0f
        if (decodeSequentially()) {
            width = decodeFloatElement(descriptor, 0)
            height = decodeFloatElement(descriptor, 1)
        } else while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> width = decodeFloatElement(descriptor, 0)
                1 -> height = decodeFloatElement(descriptor, 1)
                CompositeDecoder.DECODE_DONE -> break
                else -> error("Unexpected index: $index")
            }
        }
        require(width >= 0.0f && height >= 0.0f)
        DpSize(width.dp, height.dp)
    }
}

// TODO polymorphic based on subclasses of WindowPosition
internal object WindowPositionSerializer : KSerializer<WindowPosition> {
    override val descriptor = buildClassSerialDescriptor("WindowPosition") {
        element<Int>("x")
        element<Int>("y")
    }

    override fun serialize(encoder: Encoder, value: WindowPosition) = encoder.encodeStructure(descriptor) {
        encodeFloatElement(descriptor, 0, value.x.value)
        encodeFloatElement(descriptor, 1, value.y.value)
    }

    override fun deserialize(decoder: Decoder): WindowPosition = decoder.decodeStructure(descriptor) {
        var x = -1.0f
        var y = -1.0f
        if (decodeSequentially()) {
            x = decodeFloatElement(descriptor, 0)
            y = decodeFloatElement(descriptor, 1)
        } else while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> x = decodeFloatElement(descriptor, 0)
                1 -> y = decodeFloatElement(descriptor, 1)
                CompositeDecoder.DECODE_DONE -> break
                else -> error("Unexpected index: $index")
            }
        }
        require(x >= 0.0f && y >= 0.0f)
        WindowPosition(x.dp, y.dp)
    }
}

internal object WindowStateSerializer : KSerializer<WindowState> {
    override val descriptor = buildClassSerialDescriptor("WindowState") {
        element<String>("placement")
        element<Boolean>("isMinimized")
        element("position", WindowPositionSerializer.descriptor, isOptional = true)
        element("size", DpSizeSerializer.descriptor)
    }

    override fun serialize(encoder: Encoder, value: WindowState) = encoder.encodeStructure(descriptor) {
        encodeStringElement(descriptor, 0, value.placement.name)
        encodeBooleanElement(descriptor, 1, value.isMinimized)
        if (value.position != WindowPosition.PlatformDefault) {
            encodeSerializableElement(descriptor, 2, WindowPositionSerializer, value.position)
        }
        encodeSerializableElement(descriptor, 3, DpSizeSerializer, value.size)
    }

    override fun deserialize(decoder: Decoder): WindowState = decoder.decodeStructure(descriptor) {
        var placement = WindowPlacement.Floating
        var isMinimized = false
        var position: WindowPosition = WindowPosition.PlatformDefault
        var size = DpSize(800.dp, 600.dp)
        if (decodeSequentially()) {
            placement = WindowPlacement.valueOf(decodeStringElement(descriptor, 0))
            isMinimized = decodeBooleanElement(descriptor, 1)
            position = decodeSerializableElement(descriptor, 2, WindowPositionSerializer)
            size = decodeSerializableElement(descriptor, 3, DpSizeSerializer)
        } else while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> placement = WindowPlacement.valueOf(decodeStringElement(descriptor, 0))
                1 -> isMinimized = decodeBooleanElement(descriptor, 1)
                2 -> position = decodeSerializableElement(descriptor, 2, WindowPositionSerializer)
                3 -> size = decodeSerializableElement(descriptor, 3, DpSizeSerializer)
                CompositeDecoder.DECODE_DONE -> break
                else -> error("Unexpected index: $index")
            }
        }
        WindowState(placement, isMinimized, position, size)
    }
}
