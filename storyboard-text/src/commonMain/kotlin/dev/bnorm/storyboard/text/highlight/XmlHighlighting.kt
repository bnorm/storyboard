package dev.bnorm.storyboard.text.highlight

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import dev.bnorm.storyboard.text.highlight.antlr.xml.XMLLexer
import dev.bnorm.storyboard.text.highlight.antlr.xml.XMLParser
import dev.bnorm.storyboard.text.highlight.antlr.xml.XMLParserBaseListener
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.Token
import org.antlr.v4.kotlinruntime.tree.ParseTreeWalker

@Immutable
class XmlHighlighting(
    val attributeName: SpanStyle,
    val attributeValue: SpanStyle,
    val comment: SpanStyle,
    val entityReference: SpanStyle,
    val prologue: SpanStyle,
    val tag: SpanStyle,
    val tagData: SpanStyle,
    val tagName: SpanStyle,
) : Highlighting {
    companion object {
        fun build(
            base: SpanStyle = SpanStyle(),
            builder: Builder.() -> Unit,
        ): XmlHighlighting {
            return Builder(
                base = XmlHighlighting(
                    attributeName = base,
                    attributeValue = base,
                    comment = base,
                    entityReference = base,
                    prologue = base,
                    tag = base,
                    tagData = base,
                    tagName = base,
                )
            ).apply(builder).build()
        }
    }

    class Builder internal constructor(base: XmlHighlighting) {
        var attributeName: SpanStyle = base.attributeName
        var attributeValue: SpanStyle = base.attributeValue
        var comment: SpanStyle = base.comment
        var entityReference: SpanStyle = base.entityReference
        var prologue: SpanStyle = base.prologue
        var tag: SpanStyle = base.tag
        var tagData: SpanStyle = base.tagData
        var tagName: SpanStyle = base.tagName

        fun build(): XmlHighlighting {
            return XmlHighlighting(
                attributeName = attributeName,
                attributeValue = attributeValue,
                comment = comment,
                entityReference = entityReference,
                prologue = prologue,
                tag = tag,
                tagData = tagData,
                tagName = tagName,
            )
        }
    }

    override fun style(text: String): AnnotatedString {
        return buildAnnotatedString {
            append(text)

            val formatListener = object : XMLParserBaseListener() {
                override fun enterDocument(ctx: XMLParser.DocumentContext) {
                }

                override fun enterProlog(ctx: XMLParser.PrologContext) {
                    addStyle(prologue, ctx)
                }

                override fun enterContent(ctx: XMLParser.ContentContext) {
                    addStyle(tagData, ctx)
                }

                override fun enterElement(ctx: XMLParser.ElementContext) {
                    ctx.OPEN().forEach { addStyle(tagName, it.symbol) }
                    ctx.Name().forEach { addStyle(tagName, it.symbol) }
                    ctx.CLOSE().forEach { addStyle(tagName, it.symbol) }
                    ctx.SLASH()?.let { addStyle(tagName, it.symbol) }
                    ctx.SLASH_CLOSE()?.let { addStyle(tagName, it.symbol) }

                }

                override fun enterReference(ctx: XMLParser.ReferenceContext) {
                    addStyle(entityReference, ctx)
                }

                override fun enterAttribute(ctx: XMLParser.AttributeContext) {
                    addStyle(attributeName, ctx.Name().symbol)
                    addStyle(attributeValue, ctx.EQUALS().symbol)
                    addStyle(attributeValue, ctx.STRING().symbol)
                }

                override fun enterChardata(ctx: XMLParser.ChardataContext) {
                }

                override fun enterMisc(ctx: XMLParser.MiscContext) {
                    ctx.COMMENT()?.let { addStyle(comment, it.symbol) }
                }
            }

            // Make sure the text ends with a new-line.
            val stream = CharStreams.fromString(text + "\n")
            val lexer = XMLLexer(stream)
            val parser = XMLParser(CommonTokenStream(lexer))
            parser.content()

            val walker = ParseTreeWalker()
            walker.walk(formatListener, parser.document())
        }
    }
}

private fun AnnotatedString.Builder.addStyle(style: SpanStyle, ctx: ParserRuleContext) {
    addStyle(style, ctx.start!!.startIndex, ctx.stop!!.stopIndex + 1)
}

private fun AnnotatedString.Builder.addStyle(spanStyle: SpanStyle, token: Token) {
    addStyle(spanStyle, token.startIndex, token.stopIndex + 1)
}
