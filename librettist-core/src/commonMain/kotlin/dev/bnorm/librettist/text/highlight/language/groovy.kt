package ch.deletescape.highlight.languages

import ch.deletescape.highlight.core.*
/*
 Language = Groovy
 Author = Guillaume Laforge <glaforge@gmail.com>
 Website = http = //glaforge.appspot.com
 Description = Groovy programming language implementation inspired from Vsevolod's Java mode
 */
/**
 * This function was automatically generated, avoid directly editing it if possible!
 * Origin highlight.js/src/languages/groovy.js MD5 <41805acf6c6c3722f0b8eb065b2fe0b8>
 */
internal fun groovy(): Mode {
    return Mode(
        keywords = listOf(
            keyword(
                className = "literal",
                value = "true false null"
            ),
            keyword(
                className = "keyword",
                value =
                "byte short char int long boolean float double void def as in assert trait super this abstract static volatile transient public private protected synchronized final class interface enum if else for while switch case break default continue throw throws try catch finally implements extends new import package return instanceof"
            )
        ).flatten(),
        contains = listOf(
            hljs.COMMENT(
                "/\\*\\*",
                "\\*/",
                Mode(
                    relevance = 0,
                    contains = listOf(
                        Mode(
                            // eat up @'s in emails to prevent them to be recognized as doctags
                            begin =
                            """\w+@""",
                            relevance = 0
                        ),
                        Mode(
                            className = "doctag",
                            begin = "@[A-Za-z]+"
                        )
                    )
                )
            ),
            hljs.C_LINE_COMMENT_MODE,
            hljs.C_BLOCK_COMMENT_MODE,
            Mode(
                className = "string",
                begin = "\"\"\"",
                end = "\"\"\""
            ),
            Mode(
                className = "string",
                begin = "'''",
                end = "'''"
            ),
            Mode(
                className = "string",
                begin = "\\${'$'}/",
                end = "/\\${'$'}",
                relevance = 10
            ),
            hljs.APOS_STRING_MODE,
            Mode(
                className = "regexp",
                begin =
                """~?\/[^\/\n]+\/""",
                contains = listOf(
                    hljs.BACKSLASH_ESCAPE
                )
            ),
            hljs.QUOTE_STRING_MODE,
            Mode(
                className = "meta",
                begin = "^#!/usr/bin/env",
                end = "${'$'}",
                illegal = "\n"
            ),
            hljs.BINARY_NUMBER_MODE,
            Mode(
                className = "class",
                beginKeywords = keywords("class interface trait enum"),
                end = "\\{",
                illegal = ":",
                contains = listOf(
                    Mode(beginKeywords = keywords("extends implements")),
                    hljs.UNDERSCORE_TITLE_MODE
                )
            ),
            hljs.C_NUMBER_MODE,
            Mode(
                className = "meta",
                begin = "@[A-Za-z]+"
            ),
            Mode(
                // highlight map keys and named parameters as strings
                className = "string",
                begin =
                """[^\?]{0}[A-Za-z0-9_${'$'}]+ *:"""
            ),
            Mode(
                // catch middle element of the ternary operator
                // to avoid highlight it as a label, named parameter, or map key
                begin =
                """\?""",
                end =
                """:"""
            ),
            Mode(
                // highlight labeled statements
                className = "symbol",
                begin = "^\\s*[A-Za-z0-9_${'$'}]+:",
                relevance = 0
            )
        ),
        illegal =
        """#|<\/"""
    )
}
