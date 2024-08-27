package dev.bnorm.storyboard.text.highlight

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import dev.bnorm.storyboard.text.highlight.antlr.kotlin.KotlinLexer
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.Token

internal fun highlightKotlin(
    text: String,
    codeStyle: Highlighting,
    identifierStyle: (String) -> SpanStyle? = { null },
): AnnotatedString {
    return buildAnnotatedString {
        withStyle(codeStyle.simple) { append(text) }

        KotlinLexer(CharStreams.fromString(text)).run {
            fun AnnotatedString.Builder.addIdentifierStyle(token: Token) {
                val style = token.text?.let { identifierStyle(it) }
                if (style != null) {
                    addStyle(style, token)
                }
            }

            fun AnnotatedString.Builder.addStyle(token: Token): Token {
                when (token.type) {
                    KotlinLexer.Tokens.FUN,
                    KotlinLexer.Tokens.VAL,
                    KotlinLexer.Tokens.VAR,
                    KotlinLexer.Tokens.PACKAGE,
                    KotlinLexer.Tokens.CLASS,
                    KotlinLexer.Tokens.INTERFACE,
                    KotlinLexer.Tokens.TYPE_ALIAS,
                    KotlinLexer.Tokens.FOR,
                    KotlinLexer.Tokens.RETURN,
                    KotlinLexer.Tokens.IN,
                    KotlinLexer.Tokens.OPERATOR,
                    KotlinLexer.Tokens.NullLiteral,
                        -> addStyle(codeStyle.keyword, token)

                    KotlinLexer.Tokens.BooleanLiteral,
                    KotlinLexer.Tokens.IntegerLiteral,
                    KotlinLexer.Tokens.HexLiteral,
                    KotlinLexer.Tokens.BinLiteral,
                    KotlinLexer.Tokens.CharacterLiteral,
                    KotlinLexer.Tokens.RealLiteral,
                    KotlinLexer.Tokens.LongLiteral,
                    KotlinLexer.Tokens.UnsignedLiteral,
                        -> addStyle(codeStyle.number, token)

                    KotlinLexer.Tokens.LineComment,
                    KotlinLexer.Tokens.Inside_Comment,
                    KotlinLexer.Tokens.DelimitedComment,
                        -> addStyle(codeStyle.comment, token)

                    KotlinLexer.Tokens.LineStrRef,
                        -> {
                        addStyle(codeStyle.keyword, token.startIndex, token.startIndex + 1)
                        val style = token.text?.let { identifierStyle(it.substring(1)) }
                        if (style != null) {
                            addStyle(style, token)
                        }
                    }

                    KotlinLexer.Tokens.QUOTE_OPEN,
                    KotlinLexer.Tokens.TRIPLE_QUOTE_OPEN,
                    KotlinLexer.Tokens.QUOTE_CLOSE,
                    KotlinLexer.Tokens.TRIPLE_QUOTE_CLOSE,
                    KotlinLexer.Tokens.LineStrText,
                        -> addStyle(codeStyle.string, token)

                    KotlinLexer.Tokens.Identifier,
                        -> addIdentifierStyle(token)

                    KotlinLexer.Tokens.AT_NO_WS,
                    KotlinLexer.Tokens.AT_PRE_WS,
                        -> {
                        addStyle(codeStyle.annotation, token)
                        val next = nextToken()
                        if (next.type == KotlinLexer.Tokens.Identifier) {
                            addStyle(codeStyle.annotation, next)
                        } else if (token.type >= 0) {
                            return addStyle(token)
                        }
                        return next
                    }
                }

                return token
            }

            do {
                val token = addStyle(nextToken())
            } while (token.type >= 0)
        }
    }
}

private fun AnnotatedString.Builder.addStyle(spanStyle: SpanStyle, token: Token) {
    addStyle(spanStyle, token.startIndex, token.stopIndex + 1)
}