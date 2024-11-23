import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.easel.template.Body
import dev.bnorm.storyboard.easel.template.Header
import dev.bnorm.storyboard.easel.template.SlideEnter
import dev.bnorm.storyboard.easel.template.SlideExit
import dev.bnorm.storyboard.example.shared.JetBrainsMono
import dev.bnorm.storyboard.text.highlight.Highlighting
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight
import dev.bnorm.storyboard.text.magic.MagicText

data class CodeSlideState(
    val description: String,
    val code: @Composable () -> List<AnnotatedString> = { emptyList() },
)

fun StoryboardBuilder.CodeSlide() = slide(
    CodeSlideState(
        description = "",
    ),
    CodeSlideState(
        description = "We can render Kotlin code!",
    ),
    CodeSlideState(
        description = "We can render Kotlin code!",
        code = {
            Highlighting.current.highlight(
                "fun main() {", "}"
            )
        },
    ),
    CodeSlideState(
        description = "We can transform Kotlin code!",
        code = {
            Highlighting.current.highlight(
                "fun main() {", "\n",
                "    // Print a ", "newline\n",
                "    println(", ")\n",
                "}"
            )
        },
    ),
    CodeSlideState(
        description = "What is this, magic?!",
        code = {
            Highlighting.current.highlight(
                "fun main() {", "\n",
                "    // Print a ", "string and ", "newline\n",
                "    println(", "\"Hello, World!\"", ")\n",
                "}",
                finalizer = {
                    it.focusOn("\"Hello, World!\"", focused = SpanStyle(fontSize = 36.sp))
                }
            )
        },
    ),
    CodeSlideState(
        description = "Cool!",
        code = {
            Highlighting.current.highlight(
                "fun main() {", "\n",
                "    println(", "\"Hello, World!\"", ")\n",
                "}"
            )
        },
    ),
    enterTransition = SlideEnter(alignment = Alignment.CenterEnd),
    exitTransition = SlideExit(alignment = Alignment.CenterEnd),
) {
    val jetBrainsMono = JetBrainsMono
    val highlighting = Highlighting.build {
        simple += SpanStyle(color = Color(0xFFBCBEC4), fontFamily = jetBrainsMono)
        number = simple + SpanStyle(color = Color(0xFF2AACB8))
        keyword = simple + SpanStyle(color = Color(0xFFCF8E6D))
        punctuation = simple + SpanStyle(color = Color(0xFFA1C17E))
        annotation = simple + SpanStyle(color = Color(0xFFBBB529))
        comment = simple + SpanStyle(color = Color(0xFF7A7E85))
        string = simple + SpanStyle(color = Color(0xFF6AAB73))
        property = simple + SpanStyle(color = Color(0xFFC77DBB))
        functionDeclaration = simple + SpanStyle(color = Color(0xFF56A8F5))
        extensionFunctionCall = simple + SpanStyle(color = Color(0xFF56A8F5), fontStyle = FontStyle.Italic)
        staticFunctionCall = simple + SpanStyle(fontStyle = FontStyle.Italic)
        typeParameters = simple + SpanStyle(color = Color(0xFF16BAAC))
    }

    Highlighting(highlighting) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Header { Text("Code Slide") }
            Divider(color = MaterialTheme.colors.primary)
            Body {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    val currentState = currentState
                    Text(currentState.description)

                    @OptIn(ExperimentalTransitionApi::class)
                    val code = state.createChildTransition {
                        it.toState().code.invoke()
                    }
                    MagicText(code, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

private fun Highlighting.highlight(
    vararg text: String,
    finalizer: (AnnotatedString) -> AnnotatedString = { it },
): List<AnnotatedString> {
    // TODO is there a way to cache these results so they are not always recomputed?

    val merged = text.joinToString("")

    val highlighted = finalizer(
        merged.highlight(
            highlighting = this,
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
