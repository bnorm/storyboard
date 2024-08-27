import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import dev.bnorm.storyboard.core.StoryboardBuilder
import dev.bnorm.storyboard.core.slide
import dev.bnorm.storyboard.easel.template.Body
import dev.bnorm.storyboard.easel.template.SlideRtlEnter
import dev.bnorm.storyboard.easel.template.SlideRtlExit
import dev.bnorm.storyboard.easel.template.Title
import dev.bnorm.storyboard.text.highlight.Highlighting
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight
import dev.bnorm.storyboard.text.magic.MagicText

data class CodeSlideState(
    val description: String,
    val code: (@Composable () -> AnnotatedString)? = null,
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
            rememberHighlighting(
                """
                    fun main() {}
                """.trimIndent()
            )
        },
    ),
    CodeSlideState(
        description = "We can transform Kotlin code!",
        code = {
            rememberHighlighting(
                """
                    fun main() {
                        // Print a newline
                        println()
                    }
                """.trimIndent()
            )
        },
    ),
    CodeSlideState(
        description = "What is this, magic?!",
        code = {
            rememberHighlighting(
                """
                    fun main() {
                        // Print a string
                        println("Hello, World!")
                    }
                """.trimIndent()
            ).focusOn("\"Hello, World!\"")
        },
    ),
    CodeSlideState(
        description = "Cool!",
        code = {
            rememberHighlighting(
                """
                    fun main() {
                        println("Hello, World!")
                    }
                """.trimIndent()
            )
        },
    ),
    enterTransition = SlideRtlEnter,
    exitTransition = SlideRtlExit,
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
            Title { Text("Code Slide") }
            Divider(color = MaterialTheme.colors.primary)
            Body {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    val currentState = currentState
                    Text(currentState.description)
                    if (currentState.code != null) {
                        MagicText(currentState.code(), modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberHighlighting(
    text: String,
    identifierStyle: (Highlighting, String) -> SpanStyle? = { _, _ -> null },
): AnnotatedString {
    val highlighting = Highlighting.current
    return rememberSaveable(highlighting, text) {
        text.highlight(
            highlighting = highlighting,
            language = Language.Kotlin,
            identifierStyle = {
                identifierStyle(highlighting, it) ?: when (it) {
                    "main" -> highlighting.functionDeclaration
                    "println" -> highlighting.staticFunctionCall
                    else -> null
                }
            },
        )
    }
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
