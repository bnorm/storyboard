package dev.bnorm.storyboard.layout.tree

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T> HorizontalTree(
    root: T,
    getChildren: (node: T) -> Collection<T>,
    modifier: Modifier = Modifier,

    // How the nodes are spaced out horizontally.
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(64.dp),
    // How child nodes are aligned with each other.
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    // Ensures the size of the tree will - at minimum - support this spacing between horizontal nodes.
    // Useful when combined with non-SpacedBy arrangements.
    // TODO is this actually useful?
    horizontalMinimumSpacing: Dp = 0.dp,

    // How nodes are spaced out vertically.
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    // How parent nodes are aligned to their children.
    // Also, how children with a taller parent are aligned.
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    // Ensures the size of the tree will - at minimum - support this spacing between horizontal nodes.
    // Useful when combined with non-SpacedBy arrangements.
    // TODO is this actually useful?
    verticalMinimumSpacing: Dp = 0.dp,

    // TODO support packing?
    //  - support top/bottom packing nodes at the same depth?
    //  - support left/right pack nodes against their parent? null horizontalArrangement?
    //  - these may need to be mutually-exclusive?

    // TODO support staggering nodes at the same depth?
    //  - more important in a top-down tree

    content: @Composable (node: T) -> Unit,
) {
    class Node<T>(
        val value: T,
        val children: List<Node<T>>,
    )

    val nodes = remember(root, getChildren) {
        buildList {
            fun collect(value: T): Node<T> {
                val children = getChildren(value).map { collect(it) }
                val node = Node(value, children)
                add(node)
                return node
            }

            collect(root)
        }
    }

    Layout(
        content = {
            for (node in nodes) {
                Box { content(node.value) }
            }
        },
        modifier = modifier,
    ) { measurables, constraints ->
        class PlaceableNode(
            val placeable: Placeable,
            val depth: Int,
            val children: List<PlaceableNode>,
        ) {
            var x = 0
            var y = 0
            var minHeight = 0
        }

        val placeables = buildList {
            val iter = measurables.iterator()

            fun collect(node: Node<T>, depth: Int): PlaceableNode {
                val children = node.children.map { collect(it, depth + 1) }
                val placeable = iter.next().measure(Constraints())
                val node = PlaceableNode(placeable, depth, children)
                add(node)
                return node
            }

            collect(nodes.last(), 0)
        }

        // ====================================
        // Horizontal arrangement and alignment
        // ====================================
        val byDepth = Array(placeables.maxOf { it.depth } + 1) { mutableListOf<PlaceableNode>() }
        for (node in placeables) {
            byDepth[node.depth].add(node)
        }

        val xSpacing = maxOf(horizontalArrangement.spacing, horizontalMinimumSpacing).roundToPx()

        val xSizes = IntArray(byDepth.size) { byDepth[it].maxOf { it.placeable.width } }
        val minWidth = maxOf(xSizes.sumOf { it + xSpacing } - xSpacing, constraints.minWidth)

        val xPositions = IntArray(byDepth.size)
        with(horizontalArrangement) { arrange(minWidth, xSizes, layoutDirection, xPositions) }
        for ((i, nodes) in byDepth.withIndex()) {
            val size = xSizes[i]
            for (node in nodes) {
                node.x = xPositions[i] + horizontalAlignment.align(node.placeable.width, size, layoutDirection)
            }
        }

        // ==================================
        // Vertical arrangement and alignment
        // ==================================
        // TODO there's still some weird things go on here:
        //  - SpacedBetween looks weird
        //  - SpaceEvenly results in double space between cousins
        // TODO do we need to use arrangement within minHeight calculation?

        val ySpacing = maxOf(verticalArrangement.spacing, verticalMinimumSpacing).roundToPx()

        fun heightUp(node: PlaceableNode): Int {
            var childHeight = -ySpacing
            for (node in node.children) {
                childHeight += heightUp(node) + ySpacing
            }
            node.minHeight = maxOf(node.placeable.height, childHeight)
            return node.minHeight
        }

        fun heightDown(node: PlaceableNode, height: Int) {
            val original = node.minHeight
            node.minHeight = height

            val childSpacing = ySpacing * (node.children.size - 1)
            for (child in node.children) {
                val childHeight = (child.minHeight - childSpacing) * height / original + childSpacing
                child.minHeight = childHeight
                heightDown(child, childHeight)
            }
        }

        fun alignChildren(children: List<PlaceableNode>, yOffset: Int, height: Int) {
            if (children.isEmpty()) return

            val ySizes = IntArray(children.size) { children[it].minHeight }
            val yPositions = IntArray(children.size)
            with(verticalArrangement) { arrange(height, ySizes, yPositions) }

            val childrenOffset = if (verticalArrangement.spacing.value > 0f) {
                var min = height
                var max = 0
                for (i in children.indices) {
                    min = minOf(min, yPositions[i])
                    max = maxOf(max, yPositions[i] + ySizes[i])
                }
                verticalAlignment.align(max - min, height)
            } else {
                0
            }

            for (i in yPositions.indices) {
                val child = children[i]
                val yPosition = yOffset + yPositions[i]

                val childOffset = if (child.placeable.height < child.minHeight) {
                    verticalAlignment.align(child.placeable.height, child.minHeight)
                } else {
                    0
                }

                child.y = yPosition + childrenOffset + childOffset
                alignChildren(child.children, yPosition, ySizes[i])
            }
        }

        val root = placeables.last()
        var height = heightUp(root)
        if (verticalArrangement.spacing.value <= 0f) {
            height = maxOf(height, constraints.minHeight)
            heightDown(root, height)
        }

        alignChildren(listOf(root), 0, height)

        layout(minWidth, maxOf(height, constraints.minHeight)) {
            for (node in placeables) {
                node.placeable.place(x = node.x, y = node.y)
            }
        }
    }
}
