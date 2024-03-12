package ch.deletescape.highlight.core

/**
* Created by Sergej Kravcenko on 5/12/2017.
* Copyright (c) 2017 Sergej Kravcenko
*/
data class Mode(
   // Language
    val aliases: List<String> = emptyList(),
    var case_insensitive: Boolean = false,
   // Mode
    val self: Boolean = false,
    val subLanguage: String? = null,
    val subLanguages: List<String> = emptyList(),
    val starts: Mode? = null,
    var contains: List<Mode> = emptyList(),
    val variants: List<Mode> = emptyList(),
    val beginKeywords: List<Keyword> = emptyList(),
    val keywords: List<Keyword> = emptyList(),
    val className: String? = null,
    var begin: String? = null,
    var end: String? = null,
    val lexemes: String? = null,
    var terminators: Regex? = null,
    val illegal: String? = null,
    var terminatorEnd: String? = null,
    var relevance: Int = -1,
    val skip: Boolean = false,
    val returnBegin: Boolean = false,
    val excludeBegin: Boolean = false,
    val returnEnd: Boolean = false,
    val excludeEnd: Boolean = false,
    val endsWithParent: Boolean = false,
    val endsParent: Boolean = false,
    val endSameAsBegin: Boolean = false,
    // Special
    val exports: Mode? = null,
    val preprocessor: Mode? = null,
    val strings: Mode? = null
) {
  private var compiled:Boolean = false
  internal var compiledKeywords = mapOf<String, Keyword>()
  internal var beginRe: Regex? = null
  internal var endRe : Regex? = null
  internal var lexemesRe : Regex? = null
  internal var illegalRe : Regex? = null
  internal val cachedVariants: List<Mode> by lazy { variants.map { inherit(this, Mode(variants = emptyList()), it) } }

  private fun langRe(re: String) = "(?m" + (if(case_insensitive) "i" else "") + ")" + re

  private fun expandMode(mode: Mode): List<Mode> = when {
      mode.variants.isNotEmpty() -> mode.cachedVariants
      mode.endsWithParent -> listOf(inherit(mode))
      else -> listOf(mode)
  }

   fun compile() {
      compileMode(this, null)
   }

  companion object {
    private val emptyLexemePattern = Regex("\\w+", RegexOption.MULTILINE)
    private val emptyBeginEndPattern = Regex("\\B|\\b", RegexOption.MULTILINE)
    val SELF = Mode(self = true)
    const val IDENT_RE = "[a-zA-Z]\\w*"
    const val UNDERSCORE_IDENT_RE = "[a-zA-Z_]\\w*"
    const val NUMBER_RE = "\\b\\d+(\\.\\d+)?"
    const val C_NUMBER_RE = "(-?)(\\b0[xX][a-fA-F0-9]+|(\\b\\d+(\\.\\d*)?|\\.\\d+)([eE][-+]?\\d+)?)" // 0x..., 0..., decimal, float
    const val BINARY_NUMBER_RE = "\\b(0b[01]+)" // 0b...
    const val RE_STARTERS_RE = "!|!=|!==|%|%=|&|&&|&=|\\*|\\*=|\\+|\\+=|,|-|-=|/=|/|:|;|<<|<<=|<=|<|===|==|=|>>>=|>>=|>=|>>>|>>|>|\\?|\\[|\\{|\\(|\\^|\\^=|\\||\\|=|\\|\\||~"
    val BACKSLASH_ESCAPE = Mode(begin = "\\\\[\\s\\S]", relevance = 0)
    val APOS_STRING_MODE = Mode(className = "string", begin= "\'", end = "\'", illegal= "\\n", contains = listOf(BACKSLASH_ESCAPE))
    val QUOTE_STRING_MODE = Mode(className = "string", begin= "\"", end= "\"", illegal= "\\n", contains= listOf(BACKSLASH_ESCAPE))
    val PHRASAL_WORDS_MODE = Mode(begin = "\\b(a|an|the|are|I'm|isn't|don't|doesn't|won't|but|just|should|pretty|simply|enough|gonna|going|wtf|so|such|will|you|your|they|like|more)\\b")
    fun COMMENT(begin:String?, end:String? = null, inherits:Mode? = null) : Mode {
       var mode = Mode(
          className = "comment",
          begin = begin,
          end = end
       )
       if (inherits != null) {
          mode = inherit(mode, inherits)
       }
      return mode.copy(
         contains = mode.contains + listOf(
            PHRASAL_WORDS_MODE,
            Mode(
               className = "doctag",
               begin = "(?:TODO|FIXME|NOTE|BUG|XXX):",
               relevance = 0
            )
         )
      )
    }
    val C_LINE_COMMENT_MODE = COMMENT("//", "$", null)
    val C_BLOCK_COMMENT_MODE = COMMENT("/\\*", "\\*/", null)
    val HASH_COMMENT_MODE = COMMENT("#", "$", null)
    val NUMBER_MODE = Mode(className = "number", begin = NUMBER_RE, relevance = 0)
    val C_NUMBER_MODE = Mode(className = "number", begin = C_NUMBER_RE, relevance = 0)
    val BINARY_NUMBER_MODE = Mode(className = "number", begin = BINARY_NUMBER_RE, relevance = 0)
    val TITLE_MODE = Mode(className = "title", begin = IDENT_RE, relevance = 0)
    val UNDERSCORE_TITLE_MODE = Mode(className = "title", begin = UNDERSCORE_IDENT_RE, relevance = 0)
    val METHOD_GUARD = Mode(begin = "\\.\\s*$UNDERSCORE_IDENT_RE", relevance = 0)
    val CSS_NUMBER_MODE = Mode(className = "number", begin =(NUMBER_RE + "(" +
                                                            "%|em|ex|ch|rem" +
                                                            "|vw|vh|vmin|vmax" +
                                                            "|cm|mm|in|pt|pc|px" +
                                                            "|deg|grad|rad|turn" +
                                                            "|s|ms" +
                                                            "|Hz|kHz" +
                                                            "|dpi|dpcm|dppx" +
                                                            ")?"), relevance = 0)
    val REGEXP_MODE = Mode(className = "regexp", begin = "\\/", end = "\\/[gimuy]*", illegal = "\\n", contains = listOf(BACKSLASH_ESCAPE, Mode(begin = "\\[", end = "\\]", relevance = 0, contains = listOf(BACKSLASH_ESCAPE))))

    fun inherit(mode:Mode, vararg others:Mode): Mode {
        if (others.isEmpty()) return mode

        val from = if (others.size == 1) others[0]
        else inherit(others[0], *others.copyOfRange(1, others.size))

        return Mode(
            starts = from.starts ?: mode.starts,
            contains = if (from.contains.isNotEmpty()) from.contains else mode.contains,
            variants = if (from.variants.isNotEmpty()) from.variants else mode.variants,
            className = from.className ?: mode.className,
            keywords = if (from.keywords.isNotEmpty()) from.keywords else mode.keywords,
            beginKeywords = if (from.beginKeywords.isNotEmpty()) from.beginKeywords else mode.beginKeywords,
            begin = from.begin ?: mode.begin,
            end = from.end ?: mode.end,
            illegal = from.illegal ?: mode.illegal,
            relevance = if (from.relevance != -1) from.relevance else mode.relevance,
            returnBegin = from.returnBegin || mode.returnBegin,
            excludeBegin = from.excludeBegin || mode.excludeBegin,
            returnEnd = from.returnEnd || mode.returnEnd,
            excludeEnd = from.excludeEnd || mode.excludeEnd,
            endsWithParent = from.endsWithParent || mode.endsWithParent,
            endsParent = from.endsParent || mode.endsParent,
            terminatorEnd = from.terminatorEnd ?: mode.terminatorEnd,
            lexemes = from.lexemes ?: mode.lexemes,
            subLanguage = from.subLanguage ?: mode.subLanguage,
            subLanguages = if (from.subLanguages.isNotEmpty()) from.subLanguages else mode.subLanguages,
            self = from.self || mode.self,
            skip = from.skip || mode.skip,
            endSameAsBegin = from.endSameAsBegin || mode.endSameAsBegin
        )
    }

      // TODO: Find a way to do this without recursion as we get StackOverflow for some languages
      private fun compileMode(mode: Mode, parent: Mode?) {
          if (mode.compiled) {
              return
          }

          mode.compiled = true

          // TODO: Explicitly mark languages as that to ensure we don't mark sub languages as case insensitive by accident
          mode.case_insensitive = parent?.case_insensitive ?: mode.case_insensitive

          // Keywords
          val keywords = if(mode.keywords.isEmpty()) mode.beginKeywords else mode.keywords
          if (keywords.isNotEmpty()) {
              mode.compiledKeywords = keywords.flatMap { group ->
                  val words = if (mode.case_insensitive) group.value.toLowerCase() else group.value
                  words.split(" ").map { word ->
                      val pair = word.split('|')
                      val relevance = pair.getOrNull(1)?.toInt() ?: 1
                      pair[0] to Keyword(pair[0], group.className, relevance)
                  }
              }.toMap()
          }

          // Lexemes
          mode.lexemesRe = if (mode.lexemes == null) emptyLexemePattern else mode.langRe(mode.lexemes).toRegex()

          // Parent
          if (parent != null) {
              if (mode.beginKeywords.isNotEmpty()) {
                  mode.begin = "\\b(" + mode.beginKeywords[0].value.split(" ").joinToString("|") + ")\\b"
              }
              if (mode.begin == null) {
                  mode.begin = "\\B|\\b"
                  mode.beginRe = emptyBeginEndPattern
              } else {
                  mode.beginRe = mode.langRe(mode.begin!!).toRegex()
              }

              if (mode.endSameAsBegin) {
                  mode.end = mode.begin
              }

              if (mode.end == null && !mode.endsWithParent) {
                  mode.end = "\\B|\\b"
                  mode.endRe = emptyBeginEndPattern
              } else if (!mode.end.isNullOrBlank()) {
                  mode.endRe = mode.langRe(mode.end!!).toRegex()
              }

              mode.terminatorEnd = mode.end ?: ""
              if (mode.endsWithParent && parent.terminatorEnd != null) {
                  mode.terminatorEnd += (if (mode.end == null) "" else "|") + parent.terminatorEnd
              }
          }

          // Relevance
          if (mode.relevance == -1) {
              mode.relevance = 1
          }

          // Illegal
          if (mode.illegal != null) {
              mode.illegalRe = mode.langRe(mode.illegal).toRegex()
          }

          // Contains
          mode.contains = mode.contains.flatMap {
              mode.expandMode(if (it.self) mode else it)
          }
          mode.contains.forEach {
                compileMode(it, mode)
          }

          // Starts
          if (mode.starts != null) {
              compileMode(mode.starts, parent)
          }

          // Terminators
          val terminators = (mode.contains.mapNotNull {
              if (it.beginKeywords.isNotEmpty()) "\\.?(?:" + it.begin + ")\\.?" else it.begin
          } + listOfNotNull(
              mode.terminatorEnd,
              mode.illegal
          )).filter { it.isNotBlank() }

          mode.terminators = if (terminators.isNotEmpty()) mode.langRe(terminators.joinToString("|")).toRegex() else null
      }
   }
}