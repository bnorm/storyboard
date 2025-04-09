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
    codeStyle: CodeStyle,
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
                    KotlinLexer.Tokens.AS_SAFE,
                    KotlinLexer.Tokens.GET,
                    KotlinLexer.Tokens.SET,
                    KotlinLexer.Tokens.FUN,
                    KotlinLexer.Tokens.OBJECT,
                    KotlinLexer.Tokens.PACKAGE,
                    KotlinLexer.Tokens.CLASS,
                    KotlinLexer.Tokens.INTERFACE,
                    KotlinLexer.Tokens.TYPE_ALIAS,
                    KotlinLexer.Tokens.CONSTRUCTOR,
                    KotlinLexer.Tokens.BY,
                    KotlinLexer.Tokens.VAL,
                    KotlinLexer.Tokens.VAR,
                    KotlinLexer.Tokens.COMPANION,
                    KotlinLexer.Tokens.THIS,
                    KotlinLexer.Tokens.IF,
                    KotlinLexer.Tokens.ELSE,
                    KotlinLexer.Tokens.WHEN,
                    KotlinLexer.Tokens.TRY,
                    KotlinLexer.Tokens.CATCH,
                    KotlinLexer.Tokens.FINALLY,
                    KotlinLexer.Tokens.FOR,
                    KotlinLexer.Tokens.DO,
                    KotlinLexer.Tokens.WHILE,
                    KotlinLexer.Tokens.THROW,
                    KotlinLexer.Tokens.RETURN,
                    KotlinLexer.Tokens.AS,
                    KotlinLexer.Tokens.IS,
                    KotlinLexer.Tokens.IN,
                    KotlinLexer.Tokens.NOT_IS,
                    KotlinLexer.Tokens.NOT_IN,
                    KotlinLexer.Tokens.PUBLIC,
                    KotlinLexer.Tokens.PRIVATE,
                    KotlinLexer.Tokens.PROTECTED,
                    KotlinLexer.Tokens.INTERNAL,
                    KotlinLexer.Tokens.ENUM,
                    KotlinLexer.Tokens.SEALED,
                    KotlinLexer.Tokens.ANNOTATION,
                    KotlinLexer.Tokens.DATA,
                    KotlinLexer.Tokens.OPERATOR,
                    KotlinLexer.Tokens.OVERRIDE,
                    KotlinLexer.Tokens.ABSTRACT,
                    KotlinLexer.Tokens.NullLiteral,
                        -> addStyle(codeStyle.keyword, token)

                    KotlinLexer.Tokens.BooleanLiteral,
                        -> addStyle(codeStyle.keyword, token)

                    KotlinLexer.Tokens.RealLiteral,
                    KotlinLexer.Tokens.FloatLiteral,
                    KotlinLexer.Tokens.DoubleLiteral,
                    KotlinLexer.Tokens.IntegerLiteral,
                    KotlinLexer.Tokens.HexLiteral,
                    KotlinLexer.Tokens.BinLiteral,
                    KotlinLexer.Tokens.UnsignedLiteral,
                    KotlinLexer.Tokens.LongLiteral,
                        -> addStyle(codeStyle.number, token)

                    KotlinLexer.Tokens.CharacterLiteral,
                        -> addStyle(codeStyle.string, token)

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
                    KotlinLexer.Tokens.QUOTE_CLOSE,
                    KotlinLexer.Tokens.LineStrText,
                        -> addStyle(codeStyle.string, token)

                    KotlinLexer.Tokens.TRIPLE_QUOTE_OPEN,
                    KotlinLexer.Tokens.TRIPLE_QUOTE_CLOSE,
                        -> {
                        addStyle(codeStyle.string, token)
                        while (true) {
                            val next = nextToken()
                            // TODO handle string templates
                            addStyle(codeStyle.string, next)
                            if (next.type == KotlinLexer.Tokens.TRIPLE_QUOTE_CLOSE) {
                                return next
                            }
                        }
                    }

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
