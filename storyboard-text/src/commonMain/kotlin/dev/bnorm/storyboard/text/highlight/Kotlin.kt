package dev.bnorm.storyboard.text.highlight

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import dev.bnorm.storyboard.text.highlight.antlr.kotlin.KotlinLexer
import dev.bnorm.storyboard.text.highlight.antlr.kotlin.KotlinParser
import dev.bnorm.storyboard.text.highlight.antlr.kotlin.KotlinParser.Tokens
import dev.bnorm.storyboard.text.highlight.antlr.kotlin.KotlinParserBaseListener
import org.antlr.v4.kotlinruntime.*
import org.antlr.v4.kotlinruntime.tree.ParseTreeWalker
import org.antlr.v4.kotlinruntime.tree.TerminalNode

// TODO support passing the scope to qualifier?
//  - do we really want to rebuild call resolution?
//  - could probably handle like 99% of cases without a ton of code...
// TODO for faster startup, should we support some kind of pre-highlighted file-cached string?
//  - would this actually be faster since we'd need to fetch it?
//  - would it be better to just use `by lazy {}`?
//  - or maybe some sort of background thread that does this work?
//  - GlobalScope?
//  - some custom scope with limited parallelism (single threaded?) to avoid startup burst?
//  - maybe this should all be managed outside the library; seems like there are plenty of options.
//  - for chrome on a laptop, large samples can take >5sec to highlight
//    - suspendable to aid in background loading?
internal fun highlightKotlin(
    text: String,
    codeStyle: CodeStyle,
    scope: CodeScope,
    identifierStyle: (String) -> SpanStyle? = { null },
): AnnotatedString {
    return buildAnnotatedString {
        withStyle(codeStyle.simple) { append(text) }

        val formatListener = object : KotlinParserBaseListener() {
            private var scopes = ArrayDeque<CodeScope>().apply {
                addFirst(scope)
            }

            override fun enterKotlinFile(ctx: KotlinParser.KotlinFileContext) {
                scopes.addFirst(CodeScope.File)
            }

            override fun exitKotlinFile(ctx: KotlinParser.KotlinFileContext) {
                scopes.removeLast()
            }

            override fun enterClassBody(ctx: KotlinParser.ClassBodyContext) {
                scopes.addFirst(CodeScope.Class)
            }

            override fun exitClassBody(ctx: KotlinParser.ClassBodyContext) {
                scopes.removeLast()
            }

            override fun enterFunctionBody(ctx: KotlinParser.FunctionBodyContext) {
                scopes.addFirst(CodeScope.Function)
            }

            override fun exitFunctionBody(ctx: KotlinParser.FunctionBodyContext) {
                scopes.removeLast()
            }

            override fun enterModifier(ctx: KotlinParser.ModifierContext) {
                addStyle(codeStyle.keyword, ctx)
            }

            override fun enterPrimaryConstructor(ctx: KotlinParser.PrimaryConstructorContext) {
                ctx.CONSTRUCTOR()?.let { addStyle(codeStyle.keyword, it.symbol) }
            }

            override fun enterSecondaryConstructor(ctx: KotlinParser.SecondaryConstructorContext) {
                addStyle(codeStyle.keyword, ctx.CONSTRUCTOR().symbol)
            }

            override fun enterAnonymousInitializer(ctx: KotlinParser.AnonymousInitializerContext) {
                addStyle(codeStyle.keyword, ctx.INIT().symbol)
            }

            override fun enterLiteralConstant(ctx: KotlinParser.LiteralConstantContext) {
                when {
                    ctx.BooleanLiteral() != null -> addStyle(codeStyle.keyword, ctx)
                    ctx.NullLiteral() != null -> addStyle(codeStyle.keyword, ctx)
                    ctx.CharacterLiteral() != null -> addStyle(codeStyle.string, ctx)
                    else -> addStyle(codeStyle.number, ctx)
                }
            }

            override fun enterFunctionDeclaration(ctx: KotlinParser.FunctionDeclarationContext) {
                addStyle(codeStyle.functionDeclaration, ctx.simpleIdentifier())
            }

            override fun enterClassParameter(ctx: KotlinParser.ClassParameterContext) {
                if (ctx.VAL() != null || ctx.VAR() != null) {
                    addStyle(codeStyle.property, ctx.simpleIdentifier())
                }
            }

            override fun enterVariableDeclaration(ctx: KotlinParser.VariableDeclarationContext) {
                if (scopes.first() == CodeScope.Function) return // Local scope
                addStyle(codeStyle.property, ctx.simpleIdentifier())
            }

            override fun enterPrimaryExpression(ctx: KotlinParser.PrimaryExpressionContext) {
                // Expression without a qualifier.
                ctx.simpleIdentifier()?.let {
                    val style = identifierStyle(it.text)
                    if (style != null) addStyle(style, it)
                }
            }

            override fun enterNavigationSuffix(ctx: KotlinParser.NavigationSuffixContext) {
                // Expression with a qualifier.
                ctx.simpleIdentifier()?.let {
                    val style = identifierStyle(it.text)
                    if (style != null) addStyle(style, it)
                }
            }

            override fun enterDirectlyAssignableExpression(ctx: KotlinParser.DirectlyAssignableExpressionContext) {
                // Direct assignment to a variable.
                ctx.simpleIdentifier()?.let {
                    val style = identifierStyle(it.text)
                    if (style != null) addStyle(style, it)
                }
            }

            override fun enterSingleAnnotation(ctx: KotlinParser.SingleAnnotationContext) {
                ctx.AT_NO_WS()?.let { addStyle(codeStyle.annotation, it.symbol) }
                ctx.AT_PRE_WS()?.let { addStyle(codeStyle.annotation, it.symbol) }
            }

            override fun enterUnescapedAnnotation(ctx: KotlinParser.UnescapedAnnotationContext) {
                val userType = ctx.constructorInvocation()?.userType() ?: ctx.userType()
                val stopIndex = (userType?.stop?.stopIndex ?: ctx.start!!.stopIndex) + 1
                addStyle(codeStyle.annotation, ctx.start!!.startIndex, stopIndex)
            }

            override fun enterStringLiteral(ctx: KotlinParser.StringLiteralContext) {
                addStyle(codeStyle.string, ctx.start!!.startIndex, ctx.start!!.stopIndex + 1)
                addStyle(codeStyle.string, ctx.stop!!.startIndex, ctx.stop!!.stopIndex + 1)
            }

            override fun enterLineStringContent(ctx: KotlinParser.LineStringContentContext) {
                ctx.LineStrRef()?.let {
                    addStyle(codeStyle.keyword, it.symbol.startIndex, it.symbol.startIndex + 1)
                    val style = identifierStyle(it.text.substring(1)) ?: codeStyle.simple
                    addStyle(style, it.symbol.startIndex + 1, it.symbol.stopIndex + 1)
                }
                ctx.LineStrEscapedChar()?.let { addStyle(codeStyle.keyword, it.symbol) }
                ctx.LineStrText()?.let { addStyle(codeStyle.string, it.symbol) }
            }

            override fun enterLineStringExpression(ctx: KotlinParser.LineStringExpressionContext) {
                addStyle(codeStyle.keyword, ctx)
                addStyle(codeStyle.simple, ctx.expression())
            }

            override fun enterMultiLineStringContent(ctx: KotlinParser.MultiLineStringContentContext) {
                ctx.MultiLineStrRef()?.let {
                    addStyle(codeStyle.keyword, it.symbol.startIndex, it.symbol.startIndex + 1)
                    val style = identifierStyle(ctx.text.substring(1)) ?: codeStyle.simple
                    addStyle(style, it.symbol.startIndex + 1, it.symbol.stopIndex + 1)
                }
                ctx.MultiLineStringQuote()?.let { addStyle(codeStyle.string, it.symbol) }
                ctx.MultiLineStrText()?.let { addStyle(codeStyle.string, it.symbol) }
            }

            override fun enterMultiLineStringExpression(ctx: KotlinParser.MultiLineStringExpressionContext) {
                addStyle(codeStyle.keyword, ctx)
                addStyle(codeStyle.simple, ctx.expression())
            }

            override fun enterValueArgument(ctx: KotlinParser.ValueArgumentContext) {
                ctx.simpleIdentifier()?.let { addStyle(codeStyle.namedArgument, it) }
                ctx.ASSIGNMENT()?.let { addStyle(codeStyle.namedArgument, it.symbol) }
            }

            override fun enterLabel(ctx: KotlinParser.LabelContext) {
                addStyle(codeStyle.label, ctx.simpleIdentifier())
                ctx.AT_NO_WS()?.let { addStyle(codeStyle.label, it.symbol) }
                ctx.AT_POST_WS()?.let { addStyle(codeStyle.label, it.symbol) }
            }

            override fun visitTerminal(node: TerminalNode) {
                val symbol = node.symbol
                when (symbol.type) {
                    Tokens.AS_SAFE,
                    Tokens.GET,
                    Tokens.SET,
                    Tokens.FUN,
                    Tokens.OBJECT,
                    Tokens.PACKAGE,
                    Tokens.CLASS,
                    Tokens.INTERFACE,
                    Tokens.TYPE_ALIAS,
                    Tokens.BY,
                    Tokens.VAL,
                    Tokens.VAR,
                    Tokens.COMPANION,
                    Tokens.THIS,
                    Tokens.IF,
                    Tokens.ELSE,
                    Tokens.WHEN,
                    Tokens.TRY,
                    Tokens.CATCH,
                    Tokens.FINALLY,
                    Tokens.FOR,
                    Tokens.DO,
                    Tokens.WHILE,
                    Tokens.THROW,
                    Tokens.RETURN,
                    Tokens.CONTINUE,
                    Tokens.BREAK,
                    Tokens.AS,
                    Tokens.IS,
                    Tokens.IN,
                    Tokens.NOT_IS,
                    Tokens.NOT_IN,
                        -> addStyle(codeStyle.keyword, symbol)
                    Tokens.RETURN_AT,
                    Tokens.CONTINUE_AT,
                    Tokens.BREAK_AT -> {
                        val indexOfAt = symbol.text!!.indexOf("@")
                        addStyle(codeStyle.keyword, symbol.startIndex, symbol.startIndex + indexOfAt)
                        addStyle(codeStyle.label, symbol.startIndex + indexOfAt, symbol.stopIndex + 1)
                    }
                }
            }
        }

        // Make sure the text ends with a new-line.
        val stream = CharStreams.fromString(text + "\n")
        val lexer = KotlinLexer(stream)

        // The parser ignores code comments,
        // so create a custom source which handles highlighting them.
        val source = object : TokenSource by lexer {
            override fun nextToken(): Token {
                val token = lexer.nextToken()
                when (token.type) {
                    Tokens.LineComment,
                    Tokens.Inside_Comment,
                    Tokens.DelimitedComment,
                        -> addStyle(codeStyle.comment, token)
                }
                return token
            }
        }

        val parser = KotlinParser(CommonTokenStream(source))
        val context = when (scope) {
            CodeScope.File -> parser.script()
            CodeScope.Class -> parser.classMemberDeclarations()
            CodeScope.Function -> parser.statements()
        }

        val walker = ParseTreeWalker()
        walker.walk(formatListener, context)
    }
}

private fun AnnotatedString.Builder.addStyle(style: SpanStyle, ctx: ParserRuleContext) {
    addStyle(style, ctx.start!!.startIndex, ctx.stop!!.stopIndex + 1)
}

private fun AnnotatedString.Builder.addStyle(spanStyle: SpanStyle, token: Token) {
    addStyle(spanStyle, token.startIndex, token.stopIndex + 1)
}
