package ch.deletescape.highlight.languages
import ch.deletescape.highlight.core.*
import ch.deletescape.highlight.core.Mode

/*
 Language = Kotlin
 Author = Sergey Mashkov <cy6erGn0m@gmail.com>
 */

/**
 * This function was automatically generated, avoid directly editing it if possible!
 * Origin highlight.js/src/languages/kotlin.js MD5 <8ba7358b5bcbd0a594e77e58b8c96b2e>
 */
internal fun kt(): Mode {
    var KEYWORDS = listOf(
        keyword(
            className = "keyword",
            value =
                "abstract as val var vararg get set class object open private protected public noinline crossinline dynamic final enum if else do while for when throw try catch finally import package is in fun override companion reified inline lateinit init interface annotation data sealed internal infix operator out by constructor super tailrec where const inner suspend typealias external trait volatile transient native default"
        ),
        keyword(
            className = "built_in",
            value =
                "Byte Short Char Int Long Boolean Float Double Void Unit Nothing"
        ),
        keyword(
            className = "literal",
            value =
                "true false null"
        ),
        // TODO soft keywords: expect actual
    ).flatten()
    var KEYWORDS_WITH_LABEL = Mode(
        className = "keyword",
        begin =
            """\b(break|continue|return|this)\b""",
        starts = Mode(
            contains = listOf(
                Mode(
                    className = "symbol",
                    begin =
                        """@\w+"""
                )
            )
        )
    )
    var LABEL = Mode(
        className = "symbol",
        begin = hljs.UNDERSCORE_IDENT_RE + "@"
    )
    // for string templates
    var SUBST = Mode(
        className = "subst",
        begin = "\\$\\{",
        end = "}",
        contains = listOf(hljs.C_NUMBER_MODE)
    )
    var VARIABLE = Mode(
        className = "variable",
        begin = "\\$",
        starts = Mode(
            contains = listOf(
                Mode(
                    className = "identifier",
                    begin = hljs.UNDERSCORE_IDENT_RE,
                )
            )
        ),
    )
    var STRING = Mode(
        className = "string",
        variants = listOf(
            Mode(
                begin = "\"\"\"",
                end = "\"\"\"",
                contains = listOf(VARIABLE, SUBST)
            ),
            // Can't use built-in modes easily, as we want to use STRING in the meta
            // context as "meta-string" and there's no syntax to remove explicitly set
            // classNames in built-in modes.
            Mode(
                begin = "'",
                end = "'",
                illegal =
                    """\n""",
                contains = listOf(hljs.BACKSLASH_ESCAPE)
            ),
            Mode(
                begin = "\"",
                end = "\"",
                illegal =
                    """\n""",
                contains = listOf(hljs.BACKSLASH_ESCAPE, VARIABLE, SUBST)
            )
        )
    )
    SUBST.contains += listOf(STRING)
    var ANNOTATION_USE_SITE = Mode(
        className = "meta",
        begin = "@(?:file|property|field|get|set|receiver|param|setparam|delegate)\\s*:(?:\\s*" +
            hljs.UNDERSCORE_IDENT_RE + ")?"
    )
    var ANNOTATION = Mode(
        className = "meta",
        begin = "@" +
            hljs.UNDERSCORE_IDENT_RE,
        contains = listOf(
            Mode(
                begin =
                    """\(""",
                end =
                    """\)""",
                contains = listOf(
                    hljs.inherit(STRING, Mode(className = "meta-string"))
                )
            )
        )
    )
    // https://kotlinlang.org/docs/reference/whatsnew11.html#underscores-in-numeric-literals
    // According to the doc above, the number mode of kotlin is the same as java 8,
    // so the code below is copied from java.js
    var KOTLIN_NUMBER_RE = "\\b(0[bB]([01]+[01_]+[01]+|[01]+)|0[xX]([a-fA-F0-9]+[a-fA-F0-9_]+[a-fA-F0-9]+|[a-fA-F0-9]+)|(([\\d]+[\\d_]+[\\d]+|[\\d]+)(\\.([\\d]+[\\d_]+[\\d]+|[\\d]+))?|\\.([\\d]+[\\d_]+[\\d]+|[\\d]+))([eE][-+]?\\d+)?)[lLfF]?"
    var KOTLIN_NUMBER_MODE = Mode(
        className = "number",
        begin = KOTLIN_NUMBER_RE,
        relevance = 0
    )
    var KOTLIN_NESTED_COMMENT = hljs.COMMENT(
        "/\\*",
        "\\*/",
        Mode(contains = listOf(hljs.C_BLOCK_COMMENT_MODE))
    )

    var KOTLIN_PAREN_TYPE = Mode(
        variants = listOf(
            Mode(
                className = "type",
                begin = hljs.UNDERSCORE_IDENT_RE
            ),
            Mode(
                begin =
                    """\(""",
                end =
                    """\)""",
                contains = listOf() // defined later
            )
        )
    )
    var KOTLIN_PAREN_TYPE2 = KOTLIN_PAREN_TYPE
    KOTLIN_PAREN_TYPE2.variants[1].contains = listOf(KOTLIN_PAREN_TYPE)
    KOTLIN_PAREN_TYPE.variants[1].contains = listOf(KOTLIN_PAREN_TYPE2)

    return Mode(
        aliases = listOf("kt"),
        keywords = KEYWORDS,
        contains = listOf(
            hljs.COMMENT(
                "/\\*\\*",
                "\\*/",
                Mode(
                    relevance = 0,
                    contains = listOf(
                        Mode(
                            className = "doctag",
                            begin = "@[A-Za-z]+"
                        )
                    )
                )
            ),
            hljs.C_LINE_COMMENT_MODE,
            KOTLIN_NESTED_COMMENT,
            KEYWORDS_WITH_LABEL,
            LABEL,
            ANNOTATION_USE_SITE,
            ANNOTATION,
            Mode(
                className = "function",
                beginKeywords = keywords("fun"),
                end = "[(]|$",
                returnBegin = true,
                excludeEnd = true,
                keywords = KEYWORDS,
                illegal =
                    """fun\s+(<.*>)?[^\s\(]+(\s+[^\s\(]+)\s*=""",
                relevance = 5,
                contains = listOf(
                    Mode(
                        begin = hljs.UNDERSCORE_IDENT_RE + "\\s*\\(",
                        returnBegin = true,
                        relevance = 0,
                        contains = listOf(hljs.UNDERSCORE_TITLE_MODE)
                    ),
                    Mode(
                        className = "type",
                        begin =
                            """<""",
                        end =
                            """>""",
                        keywords = keywords("reified"),
                        relevance = 0
                    ),
                    Mode(
                        className = "params",
                        begin =
                            """\(""",
                        end =
                            """\)""",
                        endsParent = true,
                        keywords = KEYWORDS,
                        relevance = 0,
                        contains = listOf(
                            Mode(
                                begin =
                                    """:""",
                                end =
                                    """[=,\/]""",
                                endsWithParent = true,
                                contains = listOf(
                                    KOTLIN_PAREN_TYPE,
                                    hljs.C_LINE_COMMENT_MODE,
                                    KOTLIN_NESTED_COMMENT
                                ),
                                relevance = 0
                            ),
                            hljs.C_LINE_COMMENT_MODE,
                            KOTLIN_NESTED_COMMENT,
                            ANNOTATION_USE_SITE,
                            ANNOTATION,
                            STRING,
                            hljs.C_NUMBER_MODE
                        )
                    ),
                    KOTLIN_NESTED_COMMENT
                )
            ),
            Mode(
                className = "class",
                beginKeywords = keywords("class interface trait"),
                end =
                    """[:\{(]|$""", // remove "trait" when removed from KEYWORDS
                excludeEnd = true,
                illegal = "extends implements",
                contains = listOf(
                    Mode(beginKeywords = keywords("public protected internal private constructor")),
                    hljs.UNDERSCORE_TITLE_MODE,
                    Mode(
                        className = "type",
                        begin =
                            """<""",
                        end =
                            """>""",
                        excludeBegin = true,
                        excludeEnd = true,
                        relevance = 0
                    ),
                    Mode(
                        className = "type",
                        begin =
                            """[,:]\s*""",
                        end =
                            """[<\(,]|$""",
                        excludeBegin = true,
                        returnEnd = true
                    ),
                    ANNOTATION_USE_SITE,
                    ANNOTATION
                )
            ),
            STRING,
            Mode(
                className = "meta",
                begin = "^#!/usr/bin/env",
                end = "$",
                illegal = "\n"
            ),
            KOTLIN_NUMBER_MODE
        )
    )
}
