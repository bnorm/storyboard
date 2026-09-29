package dev.bnorm.storyboard

import androidx.collection.ScatterMap
import androidx.collection.mutableScatterMapOf
import org.eclipse.tm4e.core.grammar.IGrammar
import org.eclipse.tm4e.core.grammar.IStateStack
import org.eclipse.tm4e.core.grammar.IToken
import org.eclipse.tm4e.core.grammar.ITokenizeLineResult
import org.eclipse.tm4e.core.internal.grammar.StateStack
import org.eclipse.tm4e.core.registry.IGrammarSource
import org.eclipse.tm4e.core.registry.IRegistryOptions
import org.eclipse.tm4e.core.registry.Registry
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

actual fun parse(code: String, language: Language): List<Token> {
    // Keep logs clean.
    Logger.getLogger(org.eclipse.tm4e.core.internal.rule.RuleFactory::class.qualifiedName).level = Level.SEVERE
    return when (language) {
        is Custom -> {
            language.parser.parse(code)
        }

        else -> {
            val grammar = languageGrammar(language = language)
            check(grammar != null) { "Unable to load grammar for $language. Have you registered the grammar ?" }

            // Keeps track of incomplete parse state between lines.
            var state: IStateStack = StateStack.NULL
            val tokens = mutableListOf<Token>()
            code.lines().forEachIndexed { lineNumber, line ->
                val result: ITokenizeLineResult<Array<IToken>> = grammar.tokenizeLine(
                    /* lineText = */ line,
                    /* prevState = */ state,
                    /* timeLimit = */ PARSE_DURATION
                )
                // This should never really happen
                require(!result.isStoppedEarly)
                result.tokens.forEach { token ->
                    // TM4E sometimes implicitly adds line-endings to lines when using RegExp matchers.
                    // For e.g. Comments are matched with a `<pattern>$` and therefore an implicit
                    // line ending is added. We therefore need to clamp start and end indexes.
                    val startIndex = token.startIndex.coerceIn(0, line.length)
                    val endIndex = token.endIndex.coerceIn(startIndex, line.length)
                    val content = line.substring(startIndex = startIndex, endIndex = endIndex)
                    val scopes = token.scopes
                    // The primary scope is always the last one.
                    val scope = scopes.last()
                    val depth = scopes.size
                    tokens += Token(
                        content = content,
                        scope = scope,
                        depth = depth,
                        language = language,
                        allScopes = scopes,
                        lineNumber = lineNumber,
                        startIndex = startIndex,
                        endIndex = endIndex
                    )
                }
                // Update state
                state = result.ruleStack
            }
            tokens
        }
    }
}

/** How long does E4TM parse until before giving up. */
// In practice, this should never take this long.
internal val PARSE_DURATION = 1.minutes.toJavaDuration()

internal fun languageGrammar(language: Language): IGrammar? {
    val registry = LanguageRegistry(language)
    return registry.loadGrammar(language.scopeName())
}

/**
 * A registry of known languages that can parsed and tokenized.
 *
 * To add a new language, do the following:
 * * Obtain the grammar file from: https://github.com/shikijs/textmate-grammars-themes/tree/main/packages/tm-grammars/raw
 * * The `scopeName` is a part of the `JSON` with the key `scopeName`.
 * * Copy the grammar file into `resources`
 * * Create a mapping from the `Language` to the name of the grammar `resource`.
 */
val GRAMMAR_REGISTRY: ScatterMap<Language, Lazy<IGrammarSource>> = mutableScatterMapOf(
    Json to grammarResource(name = "json.tmLanguage.json"),
    Kotlin to grammarResource(name = "kotlin.tmLanguage.json"),
    Xml to grammarResource("xml.tmLanguage.json"),
    ProtobufV3 to grammarResource("proto.tmLanguage.json"),
)

internal fun grammarResource(
    name: String,
    contentType: IGrammarSource.ContentType = IGrammarSource.ContentType.JSON
): Lazy<IGrammarSource> {
    return lazy(mode = LazyThreadSafetyMode.PUBLICATION) {
        val stream = Thread.currentThread().contextClassLoader.getResourceAsStream(name)

        val grammarText = stream.use { stream!!.bufferedReader().readText() }
        // JSON content type, given we are pulling in grammars from Shiki.
        val grammar = IGrammarSource.fromString(contentType, grammarText)
        require(grammar != null) { "Unable to load grammar for $name" }
        grammar
    }
}

class LanguageRegistry(private val language: Language) : Registry(object : IRegistryOptions {
    override fun getGrammarSource(scopeName: String): IGrammarSource {
        return GRAMMAR_REGISTRY[language]?.value ?: throw IllegalStateException("Unregistered Language $language")
    }
})
