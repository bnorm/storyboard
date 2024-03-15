package ch.deletescape.highlight.highlight.render

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import ch.deletescape.highlight.core.StyleRenderer
import dev.bnorm.librettist.Highlighting
import dev.bnorm.librettist.ShowTheme

class AnnotatedStringRenderer(
    private val codeStyle: Highlighting,
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
