package ch.deletescape.highlight.core

import ch.deletescape.highlight.languages.*
import kotlin.jvm.JvmStatic

/**
 * Main class for code syntax highlighting. Contains all supported languages
 * and two main methods. Use {@link Highlighter#highlight(String, String)} if
 * code language is known or use {@link Highlighter#highlightAuto(String, String[])}
 * to automatically detect code language.
 */
class Highlighter<out T>(private val rendererFactory: StyleRendererFactory<T>) {
    companion object {
        private val languageMap = mutableMapOf<String, Mode>()
        internal val languages = mutableSetOf<String>()

        init {
            registerLanguage("gradle", gradle())
            registerLanguage("groovy", groovy())
            registerLanguage("kotlin", kt())
        }

        fun findLanguage(name: String) = languageMap[name]

        fun hasLanguage(name: String) = languageMap.containsKey(name)

        @JvmStatic
        private fun registerLanguage(name: String, language: Mode) {
            languages += name
            languageMap += name to language
            languageMap += language.aliases.associateWith { language }
        }
    }

    /**
     * Core highlighting function. Accepts a language name, or an alias, and a
     * string with the code to highlight.
     *
     * @param languageName language name
     * @param code code string to highlight
     *
     * @return the given code highlight result
     */
    fun highlight(languageName: String, code: String, graceful: Boolean = true): HighlightResult<T>? {
        // Find Language
        val language = findLanguage(languageName) ?: return null

        // Parse
        val renderer = rendererFactory.create(languageName)
        val parser = HighlightParser(language, rendererFactory, renderer)
        val relevance = parser.highlight(code, false, null, graceful)
        return HighlightResult(relevance, languageName, renderer.getResult())
    }

    /**
     * Highlighting with language detection. Accepts a string with the code to
     * highlight.
     *
     * @param code code string to highlight
     * @param languageSubset list of languages for checking or empty list for all known languages
     *
     * @return the given code highlight result
     */
    fun highlightAuto(
        code: String,
        languageSubset: List<String>? = null,
        graceful: Boolean = true
    ): HighlightResult<T>? {
        val langs = if (languageSubset.isNullOrEmpty()) languages else languageSubset

        var bestRelevance = -1
        var bestLanguageName: String? = null
        var result: T? = null
        for (languageName in langs) {
            val language = findLanguage(languageName) ?: continue

            val renderer = rendererFactory.create(languageName)
            val parser = HighlightParser(language, rendererFactory, renderer)
            val relevance = parser.highlight(code, false, null, graceful)
            if (relevance > bestRelevance) {
                bestRelevance = relevance
                bestLanguageName = languageName
                result = renderer.getResult()
            }
        }

        result ?: return null
        return HighlightResult(bestRelevance, bestLanguageName, result)
    }

    /**
     * Result of code syntax highlighting
     */
    data class HighlightResult<out T>(
        val relevance: Int,
        val language: String?,
        val result: T
    )
}
