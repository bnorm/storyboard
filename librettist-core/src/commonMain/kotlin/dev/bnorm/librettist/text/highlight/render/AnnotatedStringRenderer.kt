package ch.deletescape.highlight.highlight.render

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import ch.deletescape.highlight.core.Highlighter
import ch.deletescape.highlight.core.StyleRenderer
import dev.bnorm.librettist.ShowTheme

fun main() {
    val code = """
       @Test
       fun test() {
           val actual: List<Int> = subject.operation()
           assertEquals(4, actual.size, "Actual: ${'$'}actual !")
       }
   """.trimIndent()

    val highlighter = Highlighter {
        AnnotatedStringRenderer(
            ShowTheme.CodeStyle(
                simple = SpanStyle(Color(0xFFBCBEC4)),
                number = SpanStyle(Color(0xFF2AACB8)),
                keyword = SpanStyle(Color(0xFFCF8E6D)),
                punctuation = SpanStyle(Color(0xFFA1C17E)),
                annotation = SpanStyle(Color(0xFFBBB529)),
                comment = SpanStyle(Color(0xFF7A7E85)),
                string = SpanStyle(Color(0xFF6AAB73)),
            )
        )
    }
    val result = highlighter.highlight("kotlin", code, graceful = false)
    println(result?.result?.spanStyles)
}

class AnnotatedStringRenderer(
    private val codeStyle: ShowTheme.CodeStyle,
) : StyleRenderer<AnnotatedString> {
    private var builder = AnnotatedString.Builder()

    override fun onStart() {
        builder = AnnotatedString.Builder()
        builder.pushStyle(codeStyle.simple)
    }

    override fun onFinish() {
        builder.pop()
    }

    override fun onPushStyle(style: String) {
        val spanStyle = when (style) {
            "keyword", "literal", "function" -> codeStyle.keyword
            "variable", "subst" -> codeStyle.keyword

            "string" -> codeStyle.string
            "meta" -> codeStyle.annotation
            "number" -> codeStyle.number
            "comment", "doctag" -> codeStyle.comment

            "built_in", "title", "type", "params" -> codeStyle.simple
            "identifier" -> codeStyle.simple

            else -> SpanStyle(color = Color.Red)
//            else -> error(style)
        }
        builder.pushStyle(spanStyle)
    }

    override fun onPopStyle() {
        builder.pop()
    }

    override fun onPushCodeBlock(block: CharSequence) {
        builder.append(block)
    }

    override fun onPushSubLanguage(name: String?, code: CharSequence?) {
        error("unsupported")
    }

    override fun onAbort(code: CharSequence) {
        builder = AnnotatedString.Builder().apply {
            withStyle(codeStyle.simple) { append(code) }
        }
    }

    override fun getResult() = builder.toAnnotatedString()
}
