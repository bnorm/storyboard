import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.example.shared.Bullet
import dev.bnorm.storyboard.layout.decorated.CubicLine
import dev.bnorm.storyboard.layout.decorated.Decoration
import dev.bnorm.storyboard.layout.decorated.DecorationLayout
import dev.bnorm.storyboard.layout.tree.HorizontalTree
import dev.bnorm.storyboard.toState

fun StoryboardBuilder.ConnectedTree() {
    val root = Node(
        index = 0,
        Node(
            index = 1,
            Node(2), Node(3)
        ),
        Node(
            index = 4,
            Node(5), Node(6)
        )
    )

    class State(val root: Node?)

    val indexes = buildList { root.collect { add(it.index) } }.sorted()
    val states = buildList {
        add(State(null))
        for (index in indexes) {
            add(State(root.filter { it <= index }))
        }
    }

    scene(states) {
        val state = transition.createChildTransition { it.toState() }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Text("Connected Tree", style = MaterialTheme.typography.h3)
            Box(
                Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.Black)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    ProvideTextStyle(MaterialTheme.typography.h5) {
                        Bullet("`HorizontalTree` can be used to layout tree structures along the horizontal axis.")
                        Bullet("`DecorationLayout` can be used to \"decorate\" `Composables`:")
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(start = 32.dp)
                        ) {
                            ProvideTextStyle(MaterialTheme.typography.h6) {
                                Bullet("Can connect `Composables` with lines, curves, or arrows.")
                                Bullet("Can surround `Composables` with generic shapes.")
                                Bullet("Can outline `Composables` with specialized borders.")
                                Bullet("A decoration is simply a `Canvas` which knows the bounding box of keyed `Composable`s.")
                            }
                        }
                    }
                }
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(2.dp, Color.Black),
                    color = Color.LightGray,
                    elevation = 8.dp,
                    modifier = Modifier.weight(1f)
                ) {
                    SharedTransitionLayout {
                        state.AnimatedContent(
                            transitionSpec = {
                                fadeIn(tween(300, easing = EaseIn))
                                    .togetherWith(fadeOut(tween(300, easing = EaseOut)))
                            },
                        ) {
                            when (val root = it.root) {
                                null -> Box(modifier = Modifier.fillMaxSize())
                                else -> NodeTree(root, modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                }
            }
        }
    }
}

private class Node(val index: Int, val children: List<Node> = emptyList()) {
    constructor(index: Int, vararg children: Node) : this(index, children.toList())
}

private fun Node.collect(block: (Node) -> Unit) {
    block(this)
    children.forEach { it.collect(block) }
}

private fun Node.filter(predicate: (Int) -> Boolean): Node? {
    if (!predicate(index)) return null

    val filtered = children.mapNotNull { child -> child.filter(predicate) }
    return Node(index, filtered)
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun NodeTree(
    root: Node,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    @Composable
    fun buildDecorations(node: Node): List<Decoration> = buildList {
        for (child in node.children) {
            add(
                CubicLine(
                    startKey = node.index,
                    startAlignment = Alignment.CenterEnd,
                    endKey = child.index,
                    endAlignment = Alignment.CenterStart,
                    color = Color.Black,
                    stroke = Stroke(with(density) { 2.dp.toPx() }),
                    modifier = Modifier.sharedElement(
                        rememberSharedContentState("${node.index}-${child.index}"),
                        boundsTransform = BoundsTransform { _, _ -> tween(300, easing = EaseInOut) },
                    )
                )
            )
            addAll(buildDecorations(child))
        }
    }

    DecorationLayout(
        decorations = buildDecorations(root),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            HorizontalTree(
                root = root,
                getChildren = { it.children },
                horizontalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                key(it.index) {
                    Surface(
                        border = BorderStroke(2.dp, Color.Black),
                        shape = RoundedCornerShape(8.dp),
                        elevation = 8.dp,
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(it.index),
                                boundsTransform = BoundsTransform { _, _ -> tween(300, easing = EaseInOut) },
                            )
                            .decorate(it.index)
                    ) {
                        Text(
                            text = "Node : ${it.index}",
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
