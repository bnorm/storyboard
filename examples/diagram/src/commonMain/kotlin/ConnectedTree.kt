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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.rememberSharedContentState
import dev.bnorm.storyboard.easel.sharedBounds
import dev.bnorm.storyboard.easel.sharedElement
import dev.bnorm.storyboard.example.shared.Bullet
import dev.bnorm.storyboard.example.shared.JetBrainsMono
import dev.bnorm.storyboard.layout.decorated.CubicLine
import dev.bnorm.storyboard.layout.decorated.Decoration
import dev.bnorm.storyboard.layout.decorated.DecorationLayout
import dev.bnorm.storyboard.layout.tree.HorizontalTree
import dev.bnorm.storyboard.toValue
import kotlin.math.floor

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

    class SceneState(val root: Node?)

    val states = buildList {
        val indexes = buildList { root.collect { add(it.index) } }.sorted()

        add(SceneState(null))
        for (index in indexes) {
            add(SceneState(root.filter { it <= index }))
        }
        for (index in indexes) {
            add(SceneState(root.highlight { it == index }.also { println(it) }))
        }
        add(SceneState(root))
    }

    scene(states) {
        val state = transition.createChildTransition { it.toValue() }

        val progress = remember { Animatable(0f) }
        LaunchedEffect(Unit) {
            progress.animateTo(1f, animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing)))
        }

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
                        val mono = SpanStyle(fontFamily = JetBrainsMono)
                        fun AnnotatedString.Builder.appendMono(text: String): Unit = withStyle(mono) { append(text) }

                        Bullet(buildAnnotatedString {
                            appendMono("HorizontalTree")
                            append(" can be used to layout tree structures along the horizontal axis.")
                        })
                        Bullet(buildAnnotatedString {
                            appendMono("DecorationLayout")
                            append(" can be used to \"decorate\" ")
                            appendMono("Composables")
                            append(":")
                        })
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(start = 32.dp)
                        ) {
                            ProvideTextStyle(MaterialTheme.typography.h6) {
                                Bullet(buildAnnotatedString {
                                    append("Can connect ")
                                    appendMono("Composable")
                                    append("s with lines, curves, or arrows.")
                                })
                                Bullet(buildAnnotatedString {
                                    append("Can surround ")
                                    appendMono("Composable")
                                    append("s with generic shapes.")
                                })
                                Bullet(buildAnnotatedString {
                                    append("Can outline ")
                                    appendMono("Composable")
                                    append("s with specialized borders.")
                                })
                                Bullet(buildAnnotatedString {
                                    append("A decoration is simply a ")
                                    appendMono("Canvas")
                                    append(" which knows the bounding box of keyed ")
                                    appendMono("Composable")
                                    append("s.")
                                })
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
                                else -> NodeTree(root, progress.asState(), modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class Node(val index: Int, val highlight: Boolean, val children: List<Node> = emptyList()) {
    constructor(index: Int, vararg children: Node) : this(index, false, children.toList())
}

private fun Node.collect(block: (Node) -> Unit) {
    block(this)
    children.forEach { it.collect(block) }
}

private fun Node.filter(predicate: (Int) -> Boolean): Node? {
    if (!predicate(index)) return null

    return Node(index, highlight, children.mapNotNull { child -> child.filter(predicate) })
}

private fun Node.highlight(predicate: (Int) -> Boolean): Node {
    return Node(index, predicate(index), children.map { child -> child.highlight(predicate) })
}

@Composable
context(_: AnimatedVisibilityScope, _: SharedTransitionScope)
private fun NodeTree(
    root: Node,
    progress: State<Float>,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    @Composable
    fun buildDecorations(node: Node): List<Decoration> = buildList {
        if (node.highlight) {
            add(
                SpinningOval(
                    key = node.index,
                    scale = 1.5f,
                    progress = progress,
                )
            )
        }
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
                        rememberSharedContentState("line:${node.index}-${child.index}"),
                        boundsTransform = { _, _ -> tween(300, easing = EaseInOut) },
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
                                boundsTransform = { _, _ -> tween(300, easing = EaseInOut) },
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

// Inspired by https://gist.github.com/riggaroo/4c6abc7d29e511831c2ceb68697b913f
private fun SpinningOval(
    key: Any,
    scale: Float,
    progress: State<Float>, // 0f..1f
    modifier: Modifier = Modifier,
): Decoration = Decoration(modifier) { container ->
    val rect = container.getBoundingBox(key = key) ?: return@Decoration

    val path = Path().apply {
        val size = rect.size * scale
        val offset = Offset(size.width - rect.width, size.height - rect.height) / 2f
        addOval(Rect(rect.topLeft - offset, size))
    }

    val pathMeasure = PathMeasure()
    pathMeasure.setPath(path, false)
    val length = pathMeasure.length
    val iter = Path()
    val segments = 100
    for (i in 0..<segments) {
        val startFraction = i.toFloat() / segments
        val endFraction = (i.toFloat() + 1f) / segments

        iter.reset()
        pathMeasure.getSegment(length * startFraction, length * endFraction, iter)

        val startColor = interpolateColors((progress.value + startFraction) % 1f, colors)
        val endColor = interpolateColors((progress.value + endFraction) % 1f, colors)
        drawPath(
            path = iter,
            brush = Brush.linearGradient(listOf(startColor, endColor)),
            style = Stroke(width = 4f, cap = StrokeCap.Square),
        )
    }
}

private fun interpolateColors(
    animationValue: Float,
    colorsInput: List<Color>,
): Color {
    if (animationValue == 1f) return colorsInput.last()

    val scaledAnimationValue = animationValue * (colorsInput.size - 1)
    val oldColor = colorsInput[scaledAnimationValue.toInt()]
    val newColor = colorsInput[(scaledAnimationValue + 1f).toInt()]
    val newScaledAnimationValue = scaledAnimationValue - floor(scaledAnimationValue)
    return lerp(start = oldColor, stop = newColor, fraction = newScaledAnimationValue)
}

private val colors = listOf(
    Color.Gray,
    Color.LightGray,
    Color.Gray,
    Color.LightGray,
    Color.Gray,
)
