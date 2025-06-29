import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.easel.template.Body
import dev.bnorm.storyboard.easel.template.Header
import dev.bnorm.storyboard.easel.template.SceneEnter
import dev.bnorm.storyboard.easel.template.SceneExit
import dev.bnorm.storyboard.example.shared.INTELLIJ_DARK_CODE_STYLE
import dev.bnorm.storyboard.example.shared.JetBrainsMono
import dev.bnorm.storyboard.text.highlight.CodeStyle
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toState

data class CodeSceneState(
    val description: String,
    val code: List<AnnotatedString> = emptyList(),
)

fun StoryboardBuilder.CodeScene() = scene(
    listOf(
        CodeSceneState(
            description = "",
        ),
        CodeSceneState(
            description = "We can render Kotlin code!",
        ),
        CodeSceneState(
            description = "We can render Kotlin code!",
            code = INTELLIJ_DARK_CODE_STYLE.highlight(
                "fun main() {", "}"
            ),
        ),
        CodeSceneState(
            description = "We can transform Kotlin code!",
            code = INTELLIJ_DARK_CODE_STYLE.highlight(
                "fun main() {", "\n",
                "    // Print a ", "newline\n",
                "    println(", ")\n",
                "}"
            ),
        ),
        CodeSceneState(
            description = "What is this, magic?!",
            code = INTELLIJ_DARK_CODE_STYLE.highlight(
                "fun main() {", "\n",
                "    // Print a ", "string and ", "newline\n",
                "    println(", "\"Hello, World!\"", ")\n",
                "}",
                finalizer = {
                    it.focusOn("\"Hello, World!\"", focused = SpanStyle(fontSize = 36.sp))
                }
            ),
        ),
        CodeSceneState(
            description = "Cool!",
            code = INTELLIJ_DARK_CODE_STYLE.highlight(
                "fun main() {", "\n",
                "    println(", "\"Hello, World!\"", ")\n",
                "}"
            ),
        ),
    ),
    enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
    exitTransition = SceneExit(alignment = Alignment.CenterEnd),
) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Header { Text("Code Scene") }
        HorizontalDivider(color = MaterialTheme.colorScheme.primary)
        Body {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                val currentState = transition.currentState.toState()
                Text(currentState.description)

                @OptIn(ExperimentalTransitionApi::class)
                val code = transition.createChildTransition { it.toState().code }
                ProvideTextStyle(TextStyle(fontFamily = JetBrainsMono)) {
                    MagicText(code, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

private fun CodeStyle.highlight(
    vararg text: String,
    finalizer: (AnnotatedString) -> AnnotatedString = { it },
): List<AnnotatedString> {
    val merged = text.joinToString("")

    val highlighted = finalizer(
        merged.highlight(
            codeStyle = this,
            language = Language.Kotlin,
            identifierStyle = {
                when (it) {
                    "main" -> functionDeclaration
                    "println" -> staticFunctionCall
                    else -> null
                }
            },
        )
    )

    require(highlighted.length == merged.length) { "incorrect finalizer" }

    val split = buildList {
        var index = 0
        for (element in text) {
            this.add(highlighted.subSequence(index, index + element.length))
            index += element.length
        }
    }

    return split
}

private fun AnnotatedString.focusOn(
    subString: String,
    focused: SpanStyle = SpanStyle(),
    unfocused: SpanStyle = SpanStyle(color = Color.Gray.copy(alpha = 0.5f)),
): AnnotatedString {
    val start = indexOf(subString).takeIf { it >= 0 } ?: return this
    val end = start + subString.length
    val length = length

    return buildAnnotatedString {
        append(this@focusOn.text)

        if (start != 0) {
            for (range in subSequence(0, start).spanStyles) {
                addStyle(range.item + unfocused, range.start, range.end)
            }
            addStyle(unfocused, 0, start)
        }

        for (range in subSequence(start, end).spanStyles) {
            addStyle(range.item + focused, start + range.start, start + range.end)
        }

        if (end != length) {
            for (range in subSequence(end, length).spanStyles) {
                addStyle(range.item + unfocused, end + range.start, end + range.end)
            }
            addStyle(unfocused, end, length)
        }
    }
}
