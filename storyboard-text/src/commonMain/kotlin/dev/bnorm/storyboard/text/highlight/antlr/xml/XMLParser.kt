// Generated from /Users/brian/code/bnorm/deck.bnorm.dev/storyboard/storyboard-text/antlr/xml/XMLParser.g4 by ANTLR 4.13.1
package dev.bnorm.storyboard.text.highlight.antlr.xml

import com.strumenta.antlrkotlin.runtime.JsName
import org.antlr.v4.kotlinruntime.*
import org.antlr.v4.kotlinruntime.atn.*
import org.antlr.v4.kotlinruntime.atn.ATN.Companion.INVALID_ALT_NUMBER
import org.antlr.v4.kotlinruntime.dfa.*
import org.antlr.v4.kotlinruntime.misc.*
import org.antlr.v4.kotlinruntime.tree.*
import kotlin.jvm.JvmField

@Suppress(
    // This is required as we are using a custom JsName alias that is not recognized by the IDE.
    // No name clashes will happen tho.
    "JS_NAME_CLASH",
    "UNUSED_VARIABLE",
    "ClassName",
    "FunctionName",
    "LocalVariableName",
    "ConstPropertyName",
    "ConvertSecondaryConstructorToPrimary",
    "CanBeVal",
)
internal open class XMLParser(input: TokenStream) : Parser(input) {
    private companion object {
        init {
            RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.runtimeVersion)
        }

        private const val SERIALIZED_ATN: String =
            "\u0004\u0001\u0012\u0062\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0001\u0000\u0003\u0000\u0012\u0008\u0000\u0001\u0000\u0005\u0000\u0015\u0008\u0000\u000a\u0000\u000c\u0000\u0018\u0009\u0000\u0001\u0000\u0001\u0000\u0005\u0000\u001c\u0008\u0000\u000a\u0000\u000c\u0000\u001f\u0009\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0005\u0001\u0025\u0008\u0001\u000a\u0001\u000c\u0001\u0028\u0009\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0003\u0002\u002d\u0008\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002\u0034\u0008\u0002\u0001\u0002\u0003\u0002\u0037\u0008\u0002\u0005\u0002\u0039\u0008\u0002\u000a\u0002\u000c\u0002\u003c\u0009\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003\u0041\u0008\u0003\u000a\u0003\u000c\u0003\u0044\u0009\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003\u0050\u0008\u0003\u000a\u0003\u000c\u0003\u0053\u0009\u0003\u0001\u0003\u0003\u0003\u0056\u0008\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0000\u0000\u0008\u0000\u0002\u0004\u0006\u0008\u000a\u000c\u000e\u0000\u0003\u0001\u0000\u0004\u0005\u0002\u0000\u0006\u0006\u0009\u0009\u0003\u0000\u0001\u0001\u0006\u0006\u0012\u0012\u0067\u0000\u0011\u0001\u0000\u0000\u0000\u0002\u0022\u0001\u0000\u0000\u0000\u0004\u002c\u0001\u0000\u0000\u0000\u0006\u0055\u0001\u0000\u0000\u0000\u0008\u0057\u0001\u0000\u0000\u0000\u000a\u0059\u0001\u0000\u0000\u0000\u000c\u005d\u0001\u0000\u0000\u0000\u000e\u005f\u0001\u0000\u0000\u0000\u0010\u0012\u0003\u0002\u0001\u0000\u0011\u0010\u0001\u0000\u0000\u0000\u0011\u0012\u0001\u0000\u0000\u0000\u0012\u0016\u0001\u0000\u0000\u0000\u0013\u0015\u0003\u000e\u0007\u0000\u0014\u0013\u0001\u0000\u0000\u0000\u0015\u0018\u0001\u0000\u0000\u0000\u0016\u0014\u0001\u0000\u0000\u0000\u0016\u0017\u0001\u0000\u0000\u0000\u0017\u0019\u0001\u0000\u0000\u0000\u0018\u0016\u0001\u0000\u0000\u0000\u0019\u001d\u0003\u0006\u0003\u0000\u001a\u001c\u0003\u000e\u0007\u0000\u001b\u001a\u0001\u0000\u0000\u0000\u001c\u001f\u0001\u0000\u0000\u0000\u001d\u001b\u0001\u0000\u0000\u0000\u001d\u001e\u0001\u0000\u0000\u0000\u001e\u0020\u0001\u0000\u0000\u0000\u001f\u001d\u0001\u0000\u0000\u0000\u0020\u0021\u0005\u0000\u0000\u0001\u0021\u0001\u0001\u0000\u0000\u0000\u0022\u0026\u0005\u0008\u0000\u0000\u0023\u0025\u0003\u000a\u0005\u0000\u0024\u0023\u0001\u0000\u0000\u0000\u0025\u0028\u0001\u0000\u0000\u0000\u0026\u0024\u0001\u0000\u0000\u0000\u0026\u0027\u0001\u0000\u0000\u0000\u0027\u0029\u0001\u0000\u0000\u0000\u0028\u0026\u0001\u0000\u0000\u0000\u0029\u002a\u0005\u000b\u0000\u0000\u002a\u0003\u0001\u0000\u0000\u0000\u002b\u002d\u0003\u000c\u0006\u0000\u002c\u002b\u0001\u0000\u0000\u0000\u002c\u002d\u0001\u0000\u0000\u0000\u002d\u003a\u0001\u0000\u0000\u0000\u002e\u0034\u0003\u0006\u0003\u0000\u002f\u0034\u0003\u0008\u0004\u0000\u0030\u0034\u0005\u0002\u0000\u0000\u0031\u0034\u0005\u0012\u0000\u0000\u0032\u0034\u0005\u0001\u0000\u0000\u0033\u002e\u0001\u0000\u0000\u0000\u0033\u002f\u0001\u0000\u0000\u0000\u0033\u0030\u0001\u0000\u0000\u0000\u0033\u0031\u0001\u0000\u0000\u0000\u0033\u0032\u0001\u0000\u0000\u0000\u0034\u0036\u0001\u0000\u0000\u0000\u0035\u0037\u0003\u000c\u0006\u0000\u0036\u0035\u0001\u0000\u0000\u0000\u0036\u0037\u0001\u0000\u0000\u0000\u0037\u0039\u0001\u0000\u0000\u0000\u0038\u0033\u0001\u0000\u0000\u0000\u0039\u003c\u0001\u0000\u0000\u0000\u003a\u0038\u0001\u0000\u0000\u0000\u003a\u003b\u0001\u0000\u0000\u0000\u003b\u0005\u0001\u0000\u0000\u0000\u003c\u003a\u0001\u0000\u0000\u0000\u003d\u003e\u0005\u0007\u0000\u0000\u003e\u0042\u0005\u0010\u0000\u0000\u003f\u0041\u0003\u000a\u0005\u0000\u0040\u003f\u0001\u0000\u0000\u0000\u0041\u0044\u0001\u0000\u0000\u0000\u0042\u0040\u0001\u0000\u0000\u0000\u0042\u0043\u0001\u0000\u0000\u0000\u0043\u0045\u0001\u0000\u0000\u0000\u0044\u0042\u0001\u0000\u0000\u0000\u0045\u0046\u0005\u000a\u0000\u0000\u0046\u0047\u0003\u0004\u0002\u0000\u0047\u0048\u0005\u0007\u0000\u0000\u0048\u0049\u0005\u000d\u0000\u0000\u0049\u004a\u0005\u0010\u0000\u0000\u004a\u004b\u0005\u000a\u0000\u0000\u004b\u0056\u0001\u0000\u0000\u0000\u004c\u004d\u0005\u0007\u0000\u0000\u004d\u0051\u0005\u0010\u0000\u0000\u004e\u0050\u0003\u000a\u0005\u0000\u004f\u004e\u0001\u0000\u0000\u0000\u0050\u0053\u0001\u0000\u0000\u0000\u0051\u004f\u0001\u0000\u0000\u0000\u0051\u0052\u0001\u0000\u0000\u0000\u0052\u0054\u0001\u0000\u0000\u0000\u0053\u0051\u0001\u0000\u0000\u0000\u0054\u0056\u0005\u000c\u0000\u0000\u0055\u003d\u0001\u0000\u0000\u0000\u0055\u004c\u0001\u0000\u0000\u0000\u0056\u0007\u0001\u0000\u0000\u0000\u0057\u0058\u0007\u0000\u0000\u0000\u0058\u0009\u0001\u0000\u0000\u0000\u0059\u005a\u0005\u0010\u0000\u0000\u005a\u005b\u0005\u000e\u0000\u0000\u005b\u005c\u0005\u000f\u0000\u0000\u005c\u000b\u0001\u0000\u0000\u0000\u005d\u005e\u0007\u0001\u0000\u0000\u005e\u000d\u0001\u0000\u0000\u0000\u005f\u0060\u0007\u0002\u0000\u0000\u0060\u000f\u0001\u0000\u0000\u0000\u000b\u0011\u0016\u001d\u0026\u002c\u0033\u0036\u003a\u0042\u0051\u0055"

        private val ATN = ATNDeserializer().deserialize(SERIALIZED_ATN.toCharArray())

        private val DECISION_TO_DFA = Array(ATN.numberOfDecisions) {
            DFA(ATN.getDecisionState(it)!!, it)
        }

        private val SHARED_CONTEXT_CACHE = PredictionContextCache()
        private val RULE_NAMES: Array<String> = arrayOf(
            "document", "prolog", "content", "element", "reference", "attribute", 
            "chardata", "misc"
        )

        private val LITERAL_NAMES: Array<String?> = arrayOf(
            null, null, null, null, null, null, null, "'<'", null, null, 
            "'>'", null, "'/>'", "'/'", "'='"
        )

        private val SYMBOLIC_NAMES: Array<String?> = arrayOf(
            null, "COMMENT", "CDATA", "DTD", "EntityRef", "CharRef", "SEA_WS", 
            "OPEN", "XMLDeclOpen", "TEXT", "CLOSE", "SPECIAL_CLOSE", "SLASH_CLOSE", 
            "SLASH", "EQUALS", "STRING", "Name", "S", "PI"
        )

        private val VOCABULARY = VocabularyImpl(LITERAL_NAMES, SYMBOLIC_NAMES)

        private val TOKEN_NAMES: Array<String> = Array(SYMBOLIC_NAMES.size) {
            VOCABULARY.getLiteralName(it)
                ?: VOCABULARY.getSymbolicName(it)
                ?: "<INVALID>"
        }
    }

    public object Tokens {
        public const val EOF: Int = -1
        public const val COMMENT: Int = 1
        public const val CDATA: Int = 2
        public const val DTD: Int = 3
        public const val EntityRef: Int = 4
        public const val CharRef: Int = 5
        public const val SEA_WS: Int = 6
        public const val OPEN: Int = 7
        public const val XMLDeclOpen: Int = 8
        public const val TEXT: Int = 9
        public const val CLOSE: Int = 10
        public const val SPECIAL_CLOSE: Int = 11
        public const val SLASH_CLOSE: Int = 12
        public const val SLASH: Int = 13
        public const val EQUALS: Int = 14
        public const val STRING: Int = 15
        public const val Name: Int = 16
        public const val S: Int = 17
        public const val PI: Int = 18
    }

    public object Rules {
        public const val Document: Int = 0
        public const val Prolog: Int = 1
        public const val Content: Int = 2
        public const val Element: Int = 3
        public const val Reference: Int = 4
        public const val Attribute: Int = 5
        public const val Chardata: Int = 6
        public const val Misc: Int = 7
    }

    override var interpreter: ParserATNSimulator =
        @Suppress("LeakingThis")
        ParserATNSimulator(this, ATN, DECISION_TO_DFA, SHARED_CONTEXT_CACHE)

    override val grammarFileName: String =
        "XMLParser.g4"

    @Deprecated("Use vocabulary instead", replaceWith = ReplaceWith("vocabulary"))
    override val tokenNames: Array<String> =
        TOKEN_NAMES

    override val ruleNames: Array<String> =
        RULE_NAMES

    override val atn: ATN =
        ATN

    override val vocabulary: Vocabulary =
        VOCABULARY

    override val serializedATN: String =
        SERIALIZED_ATN

    /* Named actions */

    /* Funcs */
    public open class DocumentContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Document

        public fun element(): ElementContext = getRuleContext(ElementContext::class, 0)!!
        public fun EOF(): TerminalNode = getToken(Tokens.EOF, 0)!!
        public fun prolog(): PrologContext? = getRuleContext(PrologContext::class, 0)
        public fun misc(): List<MiscContext> = getRuleContexts(MiscContext::class)
        public fun misc(i: Int): MiscContext? = getRuleContext(MiscContext::class, i)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is XMLParserListener) {
                listener.enterDocument(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is XMLParserListener) {
                listener.exitDocument(this)
            }
        }
    }


    public fun document(): DocumentContext {
        var _localctx = DocumentContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 0, Rules.Document)
        var _la: Int

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 17
            errorHandler.sync(this)
            _la = _input.LA(1)

            if (_la == Tokens.XMLDeclOpen) {
                this.state = 16
                prolog()

            }
            this.state = 22
            errorHandler.sync(this)
            _la = _input.LA(1)

            while ((((_la) and 0x3f.inv()) == 0 && ((1L shl _la) and 262210L) != 0L)) {
                this.state = 19
                misc()

                this.state = 24
                errorHandler.sync(this)
                _la = _input.LA(1)
            }
            this.state = 25
            element()

            this.state = 29
            errorHandler.sync(this)
            _la = _input.LA(1)

            while ((((_la) and 0x3f.inv()) == 0 && ((1L shl _la) and 262210L) != 0L)) {
                this.state = 26
                misc()

                this.state = 31
                errorHandler.sync(this)
                _la = _input.LA(1)
            }
            this.state = 32
            match(Tokens.EOF)

        }
        catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        }
        finally {
            exitRule()
        }

        return _localctx
    }

    public open class PrologContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Prolog

        public fun XMLDeclOpen(): TerminalNode = getToken(Tokens.XMLDeclOpen, 0)!!
        public fun SPECIAL_CLOSE(): TerminalNode = getToken(Tokens.SPECIAL_CLOSE, 0)!!
        public fun attribute(): List<AttributeContext> = getRuleContexts(AttributeContext::class)
        public fun attribute(i: Int): AttributeContext? = getRuleContext(AttributeContext::class, i)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is XMLParserListener) {
                listener.enterProlog(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is XMLParserListener) {
                listener.exitProlog(this)
            }
        }
    }


    public fun prolog(): PrologContext {
        var _localctx = PrologContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 2, Rules.Prolog)
        var _la: Int

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 34
            match(Tokens.XMLDeclOpen)

            this.state = 38
            errorHandler.sync(this)
            _la = _input.LA(1)

            while (_la == Tokens.Name) {
                this.state = 35
                attribute()

                this.state = 40
                errorHandler.sync(this)
                _la = _input.LA(1)
            }
            this.state = 41
            match(Tokens.SPECIAL_CLOSE)

        }
        catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        }
        finally {
            exitRule()
        }

        return _localctx
    }

    public open class ContentContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Content

        public fun chardata(): List<ChardataContext> = getRuleContexts(ChardataContext::class)
        public fun chardata(i: Int): ChardataContext? = getRuleContext(ChardataContext::class, i)
        public fun element(): List<ElementContext> = getRuleContexts(ElementContext::class)
        public fun element(i: Int): ElementContext? = getRuleContext(ElementContext::class, i)
        public fun reference(): List<ReferenceContext> = getRuleContexts(ReferenceContext::class)
        public fun reference(i: Int): ReferenceContext? = getRuleContext(ReferenceContext::class, i)
        public fun CDATA(): List<TerminalNode> = getTokens(Tokens.CDATA)
        public fun CDATA(i: Int): TerminalNode? = getToken(Tokens.CDATA, i)
        public fun PI(): List<TerminalNode> = getTokens(Tokens.PI)
        public fun PI(i: Int): TerminalNode? = getToken(Tokens.PI, i)
        public fun COMMENT(): List<TerminalNode> = getTokens(Tokens.COMMENT)
        public fun COMMENT(i: Int): TerminalNode? = getToken(Tokens.COMMENT, i)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is XMLParserListener) {
                listener.enterContent(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is XMLParserListener) {
                listener.exitContent(this)
            }
        }
    }


    public fun content(): ContentContext {
        var _localctx = ContentContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 4, Rules.Content)
        var _la: Int

        try {
            var _alt: Int
            enterOuterAlt(_localctx, 1)
            this.state = 44
            errorHandler.sync(this)
            _la = _input.LA(1)

            if (_la == Tokens.SEA_WS || _la == Tokens.TEXT) {
                this.state = 43
                chardata()

            }
            this.state = 58
            errorHandler.sync(this)
            _alt = interpreter.adaptivePredict(_input, 7, context)

            while (_alt != 2 && _alt != INVALID_ALT_NUMBER) {
                if (_alt == 1 ) {
                    this.state = 51
                    errorHandler.sync(this)

                    when (_input.LA(1)) {
                        Tokens.OPEN -> /*LL1AltBlock*/ {
                            this.state = 46
                            element()

                        }Tokens.EntityRef, Tokens.CharRef -> /*LL1AltBlock*/ {
                            this.state = 47
                            reference()

                        }Tokens.CDATA -> /*LL1AltBlock*/ {
                            this.state = 48
                            match(Tokens.CDATA)

                        }Tokens.PI -> /*LL1AltBlock*/ {
                            this.state = 49
                            match(Tokens.PI)

                        }Tokens.COMMENT -> /*LL1AltBlock*/ {
                            this.state = 50
                            match(Tokens.COMMENT)

                        }
                        else -> throw NoViableAltException(this)
                    }
                    this.state = 54
                    errorHandler.sync(this)
                    _la = _input.LA(1)

                    if (_la == Tokens.SEA_WS || _la == Tokens.TEXT) {
                        this.state = 53
                        chardata()

                    } 
                }

                this.state = 60
                errorHandler.sync(this)
                _alt = interpreter.adaptivePredict(_input, 7, context)
            }
        }
        catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        }
        finally {
            exitRule()
        }

        return _localctx
    }

    public open class ElementContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Element

        public fun OPEN(): List<TerminalNode> = getTokens(Tokens.OPEN)
        public fun OPEN(i: Int): TerminalNode? = getToken(Tokens.OPEN, i)
        public fun Name(): List<TerminalNode> = getTokens(Tokens.Name)
        public fun Name(i: Int): TerminalNode? = getToken(Tokens.Name, i)
        public fun CLOSE(): List<TerminalNode> = getTokens(Tokens.CLOSE)
        public fun CLOSE(i: Int): TerminalNode? = getToken(Tokens.CLOSE, i)
        public fun content(): ContentContext? = getRuleContext(ContentContext::class, 0)
        public fun SLASH(): TerminalNode? = getToken(Tokens.SLASH, 0)
        public fun attribute(): List<AttributeContext> = getRuleContexts(AttributeContext::class)
        public fun attribute(i: Int): AttributeContext? = getRuleContext(AttributeContext::class, i)
        public fun SLASH_CLOSE(): TerminalNode? = getToken(Tokens.SLASH_CLOSE, 0)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is XMLParserListener) {
                listener.enterElement(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is XMLParserListener) {
                listener.exitElement(this)
            }
        }
    }


    public fun element(): ElementContext {
        var _localctx = ElementContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 6, Rules.Element)
        var _la: Int

        try {
            this.state = 85
            errorHandler.sync(this)

            when (interpreter.adaptivePredict(_input, 10, context)) {
                1 -> {
                    enterOuterAlt(_localctx, 1)
                    this.state = 61
                    match(Tokens.OPEN)

                    this.state = 62
                    match(Tokens.Name)

                    this.state = 66
                    errorHandler.sync(this)
                    _la = _input.LA(1)

                    while (_la == Tokens.Name) {
                        this.state = 63
                        attribute()

                        this.state = 68
                        errorHandler.sync(this)
                        _la = _input.LA(1)
                    }
                    this.state = 69
                    match(Tokens.CLOSE)

                    this.state = 70
                    content()

                    this.state = 71
                    match(Tokens.OPEN)

                    this.state = 72
                    match(Tokens.SLASH)

                    this.state = 73
                    match(Tokens.Name)

                    this.state = 74
                    match(Tokens.CLOSE)

                }2 -> {
                    enterOuterAlt(_localctx, 2)
                    this.state = 76
                    match(Tokens.OPEN)

                    this.state = 77
                    match(Tokens.Name)

                    this.state = 81
                    errorHandler.sync(this)
                    _la = _input.LA(1)

                    while (_la == Tokens.Name) {
                        this.state = 78
                        attribute()

                        this.state = 83
                        errorHandler.sync(this)
                        _la = _input.LA(1)
                    }
                    this.state = 84
                    match(Tokens.SLASH_CLOSE)

                }
            }
        }
        catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        }
        finally {
            exitRule()
        }

        return _localctx
    }

    public open class ReferenceContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Reference

        public fun EntityRef(): TerminalNode? = getToken(Tokens.EntityRef, 0)
        public fun CharRef(): TerminalNode? = getToken(Tokens.CharRef, 0)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is XMLParserListener) {
                listener.enterReference(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is XMLParserListener) {
                listener.exitReference(this)
            }
        }
    }


    public fun reference(): ReferenceContext {
        var _localctx = ReferenceContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 8, Rules.Reference)
        var _la: Int

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 87
            _la = _input.LA(1)

            if (!(_la == Tokens.EntityRef || _la == Tokens.CharRef)) {
                errorHandler.recoverInline(this)
            }
            else {
                if (_input.LA(1) == Tokens.EOF) {
                    isMatchedEOF = true
                }

                errorHandler.reportMatch(this)
                consume()
            }
        }
        catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        }
        finally {
            exitRule()
        }

        return _localctx
    }

    public open class AttributeContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Attribute

        public fun Name(): TerminalNode = getToken(Tokens.Name, 0)!!
        public fun EQUALS(): TerminalNode = getToken(Tokens.EQUALS, 0)!!
        public fun STRING(): TerminalNode = getToken(Tokens.STRING, 0)!!

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is XMLParserListener) {
                listener.enterAttribute(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is XMLParserListener) {
                listener.exitAttribute(this)
            }
        }
    }


    public fun attribute(): AttributeContext {
        var _localctx = AttributeContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 10, Rules.Attribute)

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 89
            match(Tokens.Name)

            this.state = 90
            match(Tokens.EQUALS)

            this.state = 91
            match(Tokens.STRING)

        }
        catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        }
        finally {
            exitRule()
        }

        return _localctx
    }

    public open class ChardataContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Chardata

        public fun TEXT(): TerminalNode? = getToken(Tokens.TEXT, 0)
        public fun SEA_WS(): TerminalNode? = getToken(Tokens.SEA_WS, 0)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is XMLParserListener) {
                listener.enterChardata(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is XMLParserListener) {
                listener.exitChardata(this)
            }
        }
    }


    public fun chardata(): ChardataContext {
        var _localctx = ChardataContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 12, Rules.Chardata)
        var _la: Int

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 93
            _la = _input.LA(1)

            if (!(_la == Tokens.SEA_WS || _la == Tokens.TEXT)) {
                errorHandler.recoverInline(this)
            }
            else {
                if (_input.LA(1) == Tokens.EOF) {
                    isMatchedEOF = true
                }

                errorHandler.reportMatch(this)
                consume()
            }
        }
        catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        }
        finally {
            exitRule()
        }

        return _localctx
    }

    public open class MiscContext : ParserRuleContext {
        override val ruleIndex: Int = Rules.Misc

        public fun COMMENT(): TerminalNode? = getToken(Tokens.COMMENT, 0)
        public fun PI(): TerminalNode? = getToken(Tokens.PI, 0)
        public fun SEA_WS(): TerminalNode? = getToken(Tokens.SEA_WS, 0)

        public constructor(parent: ParserRuleContext?, invokingState: Int) : super(parent, invokingState) {
        }

        override fun enterRule(listener: ParseTreeListener) {
            if (listener is XMLParserListener) {
                listener.enterMisc(this)
            }
        }

        override fun exitRule(listener: ParseTreeListener) {
            if (listener is XMLParserListener) {
                listener.exitMisc(this)
            }
        }
    }


    public fun misc(): MiscContext {
        var _localctx = MiscContext(context, state)
        var _token: Token?
        var _ctx: RuleContext?

        enterRule(_localctx, 14, Rules.Misc)
        var _la: Int

        try {
            enterOuterAlt(_localctx, 1)
            this.state = 95
            _la = _input.LA(1)

            if (!((((_la) and 0x3f.inv()) == 0 && ((1L shl _la) and 262210L) != 0L))) {
                errorHandler.recoverInline(this)
            }
            else {
                if (_input.LA(1) == Tokens.EOF) {
                    isMatchedEOF = true
                }

                errorHandler.reportMatch(this)
                consume()
            }
        }
        catch (re: RecognitionException) {
            _localctx.exception = re
            errorHandler.reportError(this, re)
            errorHandler.recover(this, re)
        }
        finally {
            exitRule()
        }

        return _localctx
    }
}
