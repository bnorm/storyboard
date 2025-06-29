package dev.bnorm.storyboard.layout.decorated

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.node.LayoutAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo

class Decoration(
    internal val modifier: Modifier = Modifier,
    private val onDraw: DrawScope.(container: DecoratedContainer) -> Unit,
) {
    internal fun draw(scope: DrawScope, container: DecoratedContainer) {
        scope.onDraw(container)
    }
}

sealed interface DecorationScope {
    fun Modifier.decorate(key: Any): Modifier
}

sealed interface DecoratedContainer {
    fun getBoundingBox(key: Any): Rect?
}

@Composable
fun DecorationLayout(
    modifier: Modifier = Modifier,
    decorations: List<Decoration> = emptyList(),
    content: @Composable DecorationScope.() -> Unit,
) {
    val container = remember { DecorationLayoutContainer() }
    Box(modifier.onPlaced { container.root = it }) {
        container.content()

        for (decoration in decorations) {
            Canvas(decoration.modifier.matchParentSize()) {
                decoration.draw(this@Canvas, container)
            }
        }
    }
}

private class DecorationLayoutContainer : DecoratedContainer, DecorationScope {
    var root by mutableStateOf<LayoutCoordinates?>(null)
    val elements = mutableStateMapOf<Any, LayoutCoordinates>()

    override fun getBoundingBox(key: Any): Rect? {
        val coordinates = elements[key] ?: return null
        return root?.localBoundingBoxOf(coordinates)
    }

    override fun Modifier.decorate(key: Any): Modifier {
        return this then DecoratedElement(
            onPlaced = {
                when (it) {
                    null -> elements.remove(key)
                    else -> elements.put(key, it)
                }
            }
        )
    }
}

internal data class DecoratedElement(
    val onPlaced: (LayoutCoordinates?) -> Unit,
) : ModifierNodeElement<DecoratedNode>() {
    override fun create() = DecoratedNode(callback = onPlaced)

    override fun update(node: DecoratedNode) {
        node.callback = onPlaced
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "onPlaced"
        properties["onPlaced"] = onPlaced
    }
}

internal class DecoratedNode(
    var callback: (LayoutCoordinates?) -> Unit,
) : LayoutAwareModifierNode, Modifier.Node() {

    override fun onPlaced(coordinates: LayoutCoordinates) {
        callback(coordinates)
    }

    override fun onDetach() {
        callback(null)
    }
}
