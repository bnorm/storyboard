@file:OptIn(ExperimentalWasmJsInterop::class)

package dev.bnorm.storyboard

import androidx.collection.ScatterMap
import androidx.collection.mutableScatterMapOf

val THEME = JSON.parse(SolarizedDarkTheme.toJsString())
const val THEME_NAME = "solarized-dark"

val LANGUAGE_REGISTRY: ScatterMap<Language, JsAny> = mutableScatterMapOf(
    Json to JSON.parse(JsonGrammar.toJsString()),
    Xml to JSON.parse(XmlGrammar.toJsString()),
    Kotlin to JSON.parse(KotlinGrammar.toJsString()),
    ProtobufV3 to JSON.parse(ProtobufV3Grammar.toJsString())
)

val LANGUAGE_NAMES: ScatterMap<Language, String> = mutableScatterMapOf(
    Json to "json",
    Xml to "xml",
    Kotlin to "kotlin",
    ProtobufV3 to "proto"
)

// Grammars

@org.intellij.lang.annotations.Language("json")
private const val JsonGrammar = """
{
  "displayName": "JSON",
  "information_for_contributors": [
    "This file has been converted from https://github.com/microsoft/vscode-JSON.tmLanguage/blob/master/JSON.tmLanguage",
    "If you want to provide a fix or improvement, please create a pull request against the original repository.",
    "Once accepted there, we are happy to receive an update request."
  ],
  "name": "json",
  "patterns": [
    {
      "include": "#value"
    }
  ],
  "repository": {
    "array": {
      "begin": "\\[",
      "beginCaptures": {
        "0": {
          "name": "punctuation.definition.array.begin.json"
        }
      },
      "end": "\\]",
      "endCaptures": {
        "0": {
          "name": "punctuation.definition.array.end.json"
        }
      },
      "name": "meta.structure.array.json",
      "patterns": [
        {
          "include": "#value"
        },
        {
          "match": ",",
          "name": "punctuation.separator.array.json"
        },
        {
          "match": "[^\\s\\]]",
          "name": "invalid.illegal.expected-array-separator.json"
        }
      ]
    },
    "comments": {
      "patterns": [
        {
          "begin": "/\\*\\*(?!/)",
          "captures": {
            "0": {
              "name": "punctuation.definition.comment.json"
            }
          },
          "end": "\\*/",
          "name": "comment.block.documentation.json"
        },
        {
          "begin": "/\\*",
          "captures": {
            "0": {
              "name": "punctuation.definition.comment.json"
            }
          },
          "end": "\\*/",
          "name": "comment.block.json"
        },
        {
          "captures": {
            "1": {
              "name": "punctuation.definition.comment.json"
            }
          },
          "match": "(//).*$\\n?",
          "name": "comment.line.double-slash.js"
        }
      ]
    },
    "constant": {
      "match": "\\b(?:true|false|null)\\b",
      "name": "constant.language.json"
    },
    "number": {
      "match": "(?x)        # turn on extended mode\n  -?        # an optional minus\n  (?:\n    0       # a zero\n    |       # ...or...\n    [1-9]   # a 1-9 character\n    \\d*     # followed by zero or more digits\n  )\n  (?:\n    (?:\n      \\.    # a period\n      \\d+   # followed by one or more digits\n    )?\n    (?:\n      [eE]  # an e character\n      [+-]? # followed by an option +/-\n      \\d+   # followed by one or more digits\n    )?      # make exponent optional\n  )?        # make decimal portion optional",
      "name": "constant.numeric.json"
    },
    "object": {
      "begin": "\\{",
      "beginCaptures": {
        "0": {
          "name": "punctuation.definition.dictionary.begin.json"
        }
      },
      "end": "\\}",
      "endCaptures": {
        "0": {
          "name": "punctuation.definition.dictionary.end.json"
        }
      },
      "name": "meta.structure.dictionary.json",
      "patterns": [
        {
          "comment": "the JSON object key",
          "include": "#objectkey"
        },
        {
          "include": "#comments"
        },
        {
          "begin": ":",
          "beginCaptures": {
            "0": {
              "name": "punctuation.separator.dictionary.key-value.json"
            }
          },
          "end": "(,)|(?=\\})",
          "endCaptures": {
            "1": {
              "name": "punctuation.separator.dictionary.pair.json"
            }
          },
          "name": "meta.structure.dictionary.value.json",
          "patterns": [
            {
              "comment": "the JSON object value",
              "include": "#value"
            },
            {
              "match": "[^\\s,]",
              "name": "invalid.illegal.expected-dictionary-separator.json"
            }
          ]
        },
        {
          "match": "[^\\s\\}]",
          "name": "invalid.illegal.expected-dictionary-separator.json"
        }
      ]
    },
    "objectkey": {
      "begin": "\"",
      "beginCaptures": {
        "0": {
          "name": "punctuation.support.type.property-name.begin.json"
        }
      },
      "end": "\"",
      "endCaptures": {
        "0": {
          "name": "punctuation.support.type.property-name.end.json"
        }
      },
      "name": "string.json support.type.property-name.json",
      "patterns": [
        {
          "include": "#stringcontent"
        }
      ]
    },
    "string": {
      "begin": "\"",
      "beginCaptures": {
        "0": {
          "name": "punctuation.definition.string.begin.json"
        }
      },
      "end": "\"",
      "endCaptures": {
        "0": {
          "name": "punctuation.definition.string.end.json"
        }
      },
      "name": "string.quoted.double.json",
      "patterns": [
        {
          "include": "#stringcontent"
        }
      ]
    },
    "stringcontent": {
      "patterns": [
        {
          "match": "(?x)                # turn on extended mode\n  \\\\                # a literal backslash\n  (?:               # ...followed by...\n    [\"\\\\/bfnrt]     # one of these characters\n    |               # ...or...\n    u               # a u\n    [0-9a-fA-F]{4}) # and four hex digits",
          "name": "constant.character.escape.json"
        },
        {
          "match": "\\\\.",
          "name": "invalid.illegal.unrecognized-string-escape.json"
        }
      ]
    },
    "value": {
      "patterns": [
        {
          "include": "#constant"
        },
        {
          "include": "#number"
        },
        {
          "include": "#string"
        },
        {
          "include": "#array"
        },
        {
          "include": "#object"
        },
        {
          "include": "#comments"
        }
      ]
    }
  },
  "scopeName": "source.json",
  "version": "https://github.com/microsoft/vscode-JSON.tmLanguage/commit/9bd83f1c252b375e957203f21793316203f61f70"
}
"""

@org.intellij.lang.annotations.Language("json")
private const val KotlinGrammar = $$"""
{
  "$schema": "https://raw.githubusercontent.com/martinring/tmlanguage/master/tmlanguage.json",
  "displayName": "Kotlin",
  "fileTypes": [
    "kt",
    "kts"
  ],
  "name": "kotlin",
  "patterns": [
    {
      "include": "#import"
    },
    {
      "include": "#package"
    },
    {
      "include": "#code"
    }
  ],
  "repository": {
    "annotation-simple": {
      "match": "(?<!\\w)@[\\w\\.]+\\b(?!:)",
      "name": "entity.name.type.annotation.kotlin"
    },
    "annotation-site": {
      "begin": "(?<!\\w)(@\\w+):\\s*(?!\\[)",
      "beginCaptures": {
        "1": {
          "name": "entity.name.type.annotation-site.kotlin"
        }
      },
      "end": "$",
      "patterns": [
        {
          "include": "#unescaped-annotation"
        }
      ]
    },
    "annotation-site-list": {
      "begin": "(?<!\\w)(@\\w+):\\s*\\[",
      "beginCaptures": {
        "1": {
          "name": "entity.name.type.annotation-site.kotlin"
        }
      },
      "end": "\\]",
      "patterns": [
        {
          "include": "#unescaped-annotation"
        }
      ]
    },
    "binary-literal": {
      "match": "0(b|B)[01][01_]*",
      "name": "constant.numeric.binary.kotlin"
    },
    "boolean-literal": {
      "match": "\\b(true|false)\\b",
      "name": "constant.language.boolean.kotlin"
    },
    "character": {
      "begin": "'",
      "end": "'",
      "name": "string.quoted.single.kotlin",
      "patterns": [
        {
          "match": "\\\\.",
          "name": "constant.character.escape.kotlin"
        }
      ]
    },
    "class-declaration": {
      "captures": {
        "1": {
          "name": "keyword.hard.class.kotlin"
        },
        "2": {
          "name": "entity.name.type.class.kotlin"
        },
        "3": {
          "patterns": [
            {
              "include": "#type-parameter"
            }
          ]
        }
      },
      "match": "\\b(class|(?:fun\\s+)?interface)\\s+(\\b\\w+\\b|`[^`]+`)\\s*(?<GROUP><([^<>]|\\g<GROUP>)+>)?"
    },
    "code": {
      "patterns": [
        {
          "include": "#comments"
        },
        {
          "include": "#keywords"
        },
        {
          "include": "#annotation-simple"
        },
        {
          "include": "#annotation-site-list"
        },
        {
          "include": "#annotation-site"
        },
        {
          "include": "#class-declaration"
        },
        {
          "include": "#object"
        },
        {
          "include": "#type-alias"
        },
        {
          "include": "#function"
        },
        {
          "include": "#variable-declaration"
        },
        {
          "include": "#type-constraint"
        },
        {
          "include": "#type-annotation"
        },
        {
          "include": "#function-call"
        },
        {
          "include": "#method-reference"
        },
        {
          "include": "#key"
        },
        {
          "include": "#string"
        },
        {
          "include": "#string-empty"
        },
        {
          "include": "#string-multiline"
        },
        {
          "include": "#character"
        },
        {
          "include": "#lambda-arrow"
        },
        {
          "include": "#operators"
        },
        {
          "include": "#self-reference"
        },
        {
          "include": "#decimal-literal"
        },
        {
          "include": "#hex-literal"
        },
        {
          "include": "#binary-literal"
        },
        {
          "include": "#boolean-literal"
        },
        {
          "include": "#null-literal"
        }
      ]
    },
    "comment-block": {
      "begin": "/\\*(?!\\*)",
      "end": "\\*/",
      "name": "comment.block.kotlin"
    },
    "comment-javadoc": {
      "patterns": [
        {
          "begin": "/\\*\\*",
          "end": "\\*/",
          "name": "comment.block.javadoc.kotlin",
          "patterns": [
            {
              "match": "@(return|constructor|receiver|sample|see|author|since|suppress)\\b",
              "name": "keyword.other.documentation.javadoc.kotlin"
            },
            {
              "captures": {
                "1": {
                  "name": "keyword.other.documentation.javadoc.kotlin"
                },
                "2": {
                  "name": "variable.parameter.kotlin"
                }
              },
              "match": "(@param|@property)\\s+(\\S+)"
            },
            {
              "captures": {
                "1": {
                  "name": "keyword.other.documentation.javadoc.kotlin"
                },
                "2": {
                  "name": "variable.parameter.kotlin"
                }
              },
              "match": "(@param)\\[(\\S+)\\]"
            },
            {
              "captures": {
                "1": {
                  "name": "keyword.other.documentation.javadoc.kotlin"
                },
                "2": {
                  "name": "entity.name.type.class.kotlin"
                }
              },
              "match": "(@(?:exception|throws))\\s+(\\S+)"
            },
            {
              "captures": {
                "1": {
                  "name": "keyword.other.documentation.javadoc.kotlin"
                },
                "2": {
                  "name": "entity.name.type.class.kotlin"
                },
                "3": {
                  "name": "variable.parameter.kotlin"
                }
              },
              "match": "{(@link)\\s+(\\S+)?#([\\w$]+\\s*\\([^\\(\\)]*\\)).*}"
            }
          ]
        }
      ]
    },
    "comment-line": {
      "begin": "//",
      "end": "$",
      "name": "comment.line.double-slash.kotlin"
    },
    "comments": {
      "patterns": [
        {
          "include": "#comment-line"
        },
        {
          "include": "#comment-block"
        },
        {
          "include": "#comment-javadoc"
        }
      ]
    },
    "control-keywords": {
      "match": "\\b(if|else|while|do|when|try|throw|break|continue|return|for)\\b",
      "name": "keyword.control.kotlin"
    },
    "decimal-literal": {
      "match": "\\b\\d[\\d_]*(\\.[\\d_]+)?((e|E)\\d+)?(u|U)?(L|F|f)?\\b",
      "name": "constant.numeric.decimal.kotlin"
    },
    "function": {
      "captures": {
        "1": {
          "name": "keyword.hard.fun.kotlin"
        },
        "2": {
          "patterns": [
            {
              "include": "#type-parameter"
            }
          ]
        },
        "4": {
          "name": "entity.name.type.class.extension.kotlin"
        },
        "5": {
          "name": "entity.name.function.declaration.kotlin"
        }
      },
      "match": "\\b(fun)\\b\\s*(?<GROUP><([^<>]|\\g<GROUP>)+>)?\\s*(?:(?:(\\w+)\\.)?(\\b\\w+\\b|`[^`]+`))?"
    },
    "function-call": {
      "captures": {
        "1": {
          "name": "entity.name.function.call.kotlin"
        },
        "2": {
          "patterns": [
            {
              "include": "#type-parameter"
            }
          ]
        }
      },
      "match": "\\??\\.?(\\b\\w+\\b|`[^`]+`)\\s*(?<GROUP><([^<>]|\\g<GROUP>)+>)?\\s*(?=[({])"
    },
    "hard-keywords": {
      "match": "\\b(as|typeof|is|in)\\b",
      "name": "keyword.hard.kotlin"
    },
    "hex-literal": {
      "match": "0(x|X)[A-Fa-f0-9][A-Fa-f0-9_]*(u|U)?",
      "name": "constant.numeric.hex.kotlin"
    },
    "import": {
      "begin": "\\b(import)\\b\\s*",
      "beginCaptures": {
        "1": {
          "name": "keyword.soft.kotlin"
        }
      },
      "contentName": "entity.name.package.kotlin",
      "end": ";|$",
      "name": "meta.import.kotlin",
      "patterns": [
        {
          "include": "#comments"
        },
        {
          "include": "#hard-keywords"
        },
        {
          "match": "\\*",
          "name": "variable.language.wildcard.kotlin"
        }
      ]
    },
    "key": {
      "captures": {
        "1": {
          "name": "variable.parameter.kotlin"
        },
        "2": {
          "name": "keyword.operator.assignment.kotlin"
        }
      },
      "match": "\\b(\\w=)\\s*(=)"
    },
    "keywords": {
      "patterns": [
        {
          "include": "#prefix-modifiers"
        },
        {
          "include": "#postfix-modifiers"
        },
        {
          "include": "#soft-keywords"
        },
        {
          "include": "#hard-keywords"
        },
        {
          "include": "#control-keywords"
        }
      ]
    },
    "lambda-arrow": {
      "match": "->",
      "name": "storage.type.function.arrow.kotlin"
    },
    "method-reference": {
      "captures": {
        "1": {
          "name": "entity.name.function.reference.kotlin"
        }
      },
      "match": "\\??::(\\b\\w+\\b|`[^`]+`)"
    },
    "null-literal": {
      "match": "\\bnull\\b",
      "name": "constant.language.null.kotlin"
    },
    "object": {
      "captures": {
        "1": {
          "name": "keyword.hard.object.kotlin"
        },
        "2": {
          "name": "entity.name.type.object.kotlin"
        }
      },
      "match": "\\b(object)(?:\\s+(\\b\\w+\\b|`[^`]+`))?"
    },
    "operators": {
      "patterns": [
        {
          "match": "(===?|\\!==?|<=|>=|<|>)",
          "name": "keyword.operator.comparison.kotlin"
        },
        {
          "match": "([+*/%-]=)",
          "name": "keyword.operator.assignment.arithmetic.kotlin"
        },
        {
          "match": "(=)",
          "name": "keyword.operator.assignment.kotlin"
        },
        {
          "match": "([+*/%-])",
          "name": "keyword.operator.arithmetic.kotlin"
        },
        {
          "match": "(!|&&|\\|\\|)",
          "name": "keyword.operator.logical.kotlin"
        },
        {
          "match": "(--|\\+\\+)",
          "name": "keyword.operator.increment-decrement.kotlin"
        },
        {
          "match": "(\\.\\.)",
          "name": "keyword.operator.range.kotlin"
        }
      ]
    },
    "package": {
      "begin": "\\b(package)\\b\\s*",
      "beginCaptures": {
        "1": {
          "name": "keyword.hard.package.kotlin"
        }
      },
      "contentName": "entity.name.package.kotlin",
      "end": ";|$",
      "name": "meta.package.kotlin",
      "patterns": [
        {
          "include": "#comments"
        }
      ]
    },
    "postfix-modifiers": {
      "match": "\\b(where|by|get|set)\\b",
      "name": "storage.modifier.other.kotlin"
    },
    "prefix-modifiers": {
      "match": "\\b(abstract|final|enum|open|annotation|sealed|data|override|final|lateinit|private|protected|public|internal|inner|companion|noinline|crossinline|vararg|reified|tailrec|operator|infix|inline|external|const|suspend|value)\\b",
      "name": "storage.modifier.other.kotlin"
    },
    "self-reference": {
      "match": "\\b(this|super)(@\\w+)?\\b",
      "name": "variable.language.this.kotlin"
    },
    "soft-keywords": {
      "match": "\\b(init|catch|finally|field)\\b",
      "name": "keyword.soft.kotlin"
    },
    "string": {
      "begin": "(?<!\")\"(?!\")",
      "end": "\"",
      "name": "string.quoted.double.kotlin",
      "patterns": [
        {
          "match": "\\\\.",
          "name": "constant.character.escape.kotlin"
        },
        {
          "include": "#string-escape-simple"
        },
        {
          "include": "#string-escape-bracketed"
        }
      ]
    },
    "string-empty": {
      "match": "(?<!\")\"\"(?!\")",
      "name": "string.quoted.double.kotlin"
    },
    "string-escape-bracketed": {
      "begin": "(?<!\\\\)(\\$\\{)",
      "beginCaptures": {
        "1": {
          "name": "punctuation.definition.template-expression.begin"
        }
      },
      "end": "(\\})",
      "endCaptures": {
        "1": {
          "name": "punctuation.definition.template-expression.end"
        }
      },
      "name": "meta.template.expression.kotlin",
      "patterns": [
        {
          "include": "#code"
        }
      ]
    },
    "string-escape-simple": {
      "match": "(?<!\\\\)\\$\\w+\\b",
      "name": "variable.string-escape.kotlin"
    },
    "string-multiline": {
      "begin": "\"\"\"",
      "end": "\"\"\"",
      "name": "string.quoted.double.kotlin",
      "patterns": [
        {
          "match": "\\\\.",
          "name": "constant.character.escape.kotlin"
        },
        {
          "include": "#string-escape-simple"
        },
        {
          "include": "#string-escape-bracketed"
        }
      ]
    },
    "type-alias": {
      "captures": {
        "1": {
          "name": "keyword.hard.typealias.kotlin"
        },
        "2": {
          "name": "entity.name.type.kotlin"
        },
        "3": {
          "patterns": [
            {
              "include": "#type-parameter"
            }
          ]
        }
      },
      "match": "\\b(typealias)\\s+(\\b\\w+\\b|`[^`]+`)\\s*(?<GROUP><([^<>]|\\g<GROUP>)+>)?"
    },
    "type-annotation": {
      "captures": {
        "0": {
          "patterns": [
            {
              "include": "#type-parameter"
            }
          ]
        }
      },
      "match": "(?<![:?]):\\s*(\\w|\\?|\\s|->|(?<GROUP>[<(]([^<>()\"']|\\g<GROUP>)+[)>]))+"
    },
    "type-parameter": {
      "patterns": [
        {
          "match": "\\b\\w+\\b",
          "name": "entity.name.type.kotlin"
        },
        {
          "match": "\\b(in|out)\\b",
          "name": "storage.modifier.kotlin"
        }
      ]
    },
    "unescaped-annotation": {
      "match": "\\b[\\w\\.]+\\b",
      "name": "entity.name.type.annotation.kotlin"
    },
    "variable-declaration": {
      "captures": {
        "1": {
          "name": "keyword.hard.kotlin"
        },
        "2": {
          "patterns": [
            {
              "include": "#type-parameter"
            }
          ]
        }
      },
      "match": "\\b(val|var)\\b\\s*(?<GROUP><([^<>]|\\g<GROUP>)+>)?"
    }
  },
  "scopeName": "source.kotlin"
}
"""

@org.intellij.lang.annotations.Language("json")
private const val XmlGrammar = """
{
  "displayName": "XML",
  "information_for_contributors": [
    "This file has been converted from https://github.com/atom/language-xml/blob/master/grammars/xml.cson",
    "If you want to provide a fix or improvement, please create a pull request against the original repository.",
    "Once accepted there, we are happy to receive an update request."
  ],
  "name": "xml",
  "patterns": [
    {
      "begin": "(<\\?)\\s*([-_a-zA-Z0-9]+)",
      "captures": {
        "1": {
          "name": "punctuation.definition.tag.xml"
        },
        "2": {
          "name": "entity.name.tag.xml"
        }
      },
      "end": "(\\?>)",
      "name": "meta.tag.preprocessor.xml",
      "patterns": [
        {
          "match": " ([a-zA-Z-]+)",
          "name": "entity.other.attribute-name.xml"
        },
        {
          "include": "#doublequotedString"
        },
        {
          "include": "#singlequotedString"
        }
      ]
    },
    {
      "begin": "(<!)(DOCTYPE)\\s+([:a-zA-Z_][:a-zA-Z0-9_.-]*)",
      "captures": {
        "1": {
          "name": "punctuation.definition.tag.xml"
        },
        "2": {
          "name": "keyword.other.doctype.xml"
        },
        "3": {
          "name": "variable.language.documentroot.xml"
        }
      },
      "end": "\\s*(>)",
      "name": "meta.tag.sgml.doctype.xml",
      "patterns": [
        {
          "include": "#internalSubset"
        }
      ]
    },
    {
      "include": "#comments"
    },
    {
      "begin": "(<)((?:([-_a-zA-Z0-9]+)(:))?([-_a-zA-Z0-9:]+))(?=(\\s[^>]*)?></\\2>)",
      "beginCaptures": {
        "1": {
          "name": "punctuation.definition.tag.xml"
        },
        "2": {
          "name": "entity.name.tag.xml"
        },
        "3": {
          "name": "entity.name.tag.namespace.xml"
        },
        "4": {
          "name": "punctuation.separator.namespace.xml"
        },
        "5": {
          "name": "entity.name.tag.localname.xml"
        }
      },
      "end": "(>)(</)((?:([-_a-zA-Z0-9]+)(:))?([-_a-zA-Z0-9:]+))(>)",
      "endCaptures": {
        "1": {
          "name": "punctuation.definition.tag.xml"
        },
        "2": {
          "name": "punctuation.definition.tag.xml"
        },
        "3": {
          "name": "entity.name.tag.xml"
        },
        "4": {
          "name": "entity.name.tag.namespace.xml"
        },
        "5": {
          "name": "punctuation.separator.namespace.xml"
        },
        "6": {
          "name": "entity.name.tag.localname.xml"
        },
        "7": {
          "name": "punctuation.definition.tag.xml"
        }
      },
      "name": "meta.tag.no-content.xml",
      "patterns": [
        {
          "include": "#tagStuff"
        }
      ]
    },
    {
      "begin": "(</?)(?:([-\\w\\.]+)((:)))?([-\\w\\.:]+)",
      "captures": {
        "1": {
          "name": "punctuation.definition.tag.xml"
        },
        "2": {
          "name": "entity.name.tag.namespace.xml"
        },
        "3": {
          "name": "entity.name.tag.xml"
        },
        "4": {
          "name": "punctuation.separator.namespace.xml"
        },
        "5": {
          "name": "entity.name.tag.localname.xml"
        }
      },
      "end": "(/?>)",
      "name": "meta.tag.xml",
      "patterns": [
        {
          "include": "#tagStuff"
        }
      ]
    },
    {
      "include": "#entity"
    },
    {
      "include": "#bare-ampersand"
    },
    {
      "begin": "<%@",
      "beginCaptures": {
        "0": {
          "name": "punctuation.section.embedded.begin.xml"
        }
      },
      "end": "%>",
      "endCaptures": {
        "0": {
          "name": "punctuation.section.embedded.end.xml"
        }
      },
      "name": "source.java-props.embedded.xml",
      "patterns": [
        {
          "match": "page|include|taglib",
          "name": "keyword.other.page-props.xml"
        }
      ]
    },
    {
      "begin": "<%[!=]?(?!--)",
      "beginCaptures": {
        "0": {
          "name": "punctuation.section.embedded.begin.xml"
        }
      },
      "end": "(?!--)%>",
      "endCaptures": {
        "0": {
          "name": "punctuation.section.embedded.end.xml"
        }
      },
      "name": "source.java.embedded.xml",
      "patterns": [
        {
          "include": "source.java"
        }
      ]
    },
    {
      "begin": "<!\\[CDATA\\[",
      "beginCaptures": {
        "0": {
          "name": "punctuation.definition.string.begin.xml"
        }
      },
      "end": "]]>",
      "endCaptures": {
        "0": {
          "name": "punctuation.definition.string.end.xml"
        }
      },
      "name": "string.unquoted.cdata.xml"
    }
  ],
  "repository": {
    "EntityDecl": {
      "begin": "(<!)(ENTITY)\\s+(%\\s+)?([:a-zA-Z_][:a-zA-Z0-9_.-]*)(\\s+(?:SYSTEM|PUBLIC)\\s+)?",
      "captures": {
        "1": {
          "name": "punctuation.definition.tag.xml"
        },
        "2": {
          "name": "keyword.other.entity.xml"
        },
        "3": {
          "name": "punctuation.definition.entity.xml"
        },
        "4": {
          "name": "variable.language.entity.xml"
        },
        "5": {
          "name": "keyword.other.entitytype.xml"
        }
      },
      "end": "(>)",
      "patterns": [
        {
          "include": "#doublequotedString"
        },
        {
          "include": "#singlequotedString"
        }
      ]
    },
    "bare-ampersand": {
      "match": "&",
      "name": "invalid.illegal.bad-ampersand.xml"
    },
    "comments": {
      "patterns": [
        {
          "begin": "<%--",
          "captures": {
            "0": {
              "name": "punctuation.definition.comment.xml"
            },
            "end": "--%>",
            "name": "comment.block.xml"
          }
        },
        {
          "begin": "<!--",
          "captures": {
            "0": {
              "name": "punctuation.definition.comment.xml"
            }
          },
          "end": "-->",
          "name": "comment.block.xml",
          "patterns": [
            {
              "begin": "--(?!>)",
              "captures": {
                "0": {
                  "name": "invalid.illegal.bad-comments-or-CDATA.xml"
                }
              }
            }
          ]
        }
      ]
    },
    "doublequotedString": {
      "begin": "\"",
      "beginCaptures": {
        "0": {
          "name": "punctuation.definition.string.begin.xml"
        }
      },
      "end": "\"",
      "endCaptures": {
        "0": {
          "name": "punctuation.definition.string.end.xml"
        }
      },
      "name": "string.quoted.double.xml",
      "patterns": [
        {
          "include": "#entity"
        },
        {
          "include": "#bare-ampersand"
        }
      ]
    },
    "entity": {
      "captures": {
        "1": {
          "name": "punctuation.definition.constant.xml"
        },
        "3": {
          "name": "punctuation.definition.constant.xml"
        }
      },
      "match": "(&)([:a-zA-Z_][:a-zA-Z0-9_.-]*|#[0-9]+|#x[0-9a-fA-F]+)(;)",
      "name": "constant.character.entity.xml"
    },
    "internalSubset": {
      "begin": "(\\[)",
      "captures": {
        "1": {
          "name": "punctuation.definition.constant.xml"
        }
      },
      "end": "(\\])",
      "name": "meta.internalsubset.xml",
      "patterns": [
        {
          "include": "#EntityDecl"
        },
        {
          "include": "#parameterEntity"
        },
        {
          "include": "#comments"
        }
      ]
    },
    "parameterEntity": {
      "captures": {
        "1": {
          "name": "punctuation.definition.constant.xml"
        },
        "3": {
          "name": "punctuation.definition.constant.xml"
        }
      },
      "match": "(%)([:a-zA-Z_][:a-zA-Z0-9_.-]*)(;)",
      "name": "constant.character.parameter-entity.xml"
    },
    "singlequotedString": {
      "begin": "'",
      "beginCaptures": {
        "0": {
          "name": "punctuation.definition.string.begin.xml"
        }
      },
      "end": "'",
      "endCaptures": {
        "0": {
          "name": "punctuation.definition.string.end.xml"
        }
      },
      "name": "string.quoted.single.xml",
      "patterns": [
        {
          "include": "#entity"
        },
        {
          "include": "#bare-ampersand"
        }
      ]
    },
    "tagStuff": {
      "patterns": [
        {
          "captures": {
            "1": {
              "name": "entity.other.attribute-name.namespace.xml"
            },
            "2": {
              "name": "entity.other.attribute-name.xml"
            },
            "3": {
              "name": "punctuation.separator.namespace.xml"
            },
            "4": {
              "name": "entity.other.attribute-name.localname.xml"
            }
          },
          "match": "(?:^|\\s+)(?:([-\\w.]+)((:)))?([-\\w.:]+)\\s*="
        },
        {
          "include": "#doublequotedString"
        },
        {
          "include": "#singlequotedString"
        }
      ]
    }
  },
  "scopeName": "text.xml",
  "version": "https://github.com/atom/language-xml/commit/7bc75dfe779ad5b35d9bf4013d9181864358cb49"
}
"""

@org.intellij.lang.annotations.Language("json")
private const val ProtobufV3Grammar = """
    {
  "displayName": "Protocol Buffer 3",
  "fileTypes": [
    "proto"
  ],
  "name": "proto",
  "patterns": [
    {
      "include": "#comments"
    },
    {
      "include": "#syntax"
    },
    {
      "include": "#package"
    },
    {
      "include": "#import"
    },
    {
      "include": "#optionStmt"
    },
    {
      "include": "#message"
    },
    {
      "include": "#enum"
    },
    {
      "include": "#service"
    }
  ],
  "repository": {
    "comments": {
      "patterns": [
        {
          "begin": "/\\*",
          "end": "\\*/",
          "name": "comment.block.proto"
        },
        {
          "begin": "//",
          "end": "$\\n?",
          "name": "comment.line.double-slash.proto"
        }
      ]
    },
    "constants": {
      "match": "\\b(true|false|max|[A-Z_]+)\\b",
      "name": "constant.language.proto"
    },
    "enum": {
      "begin": "(enum)(\\s+)([A-Za-z][A-Za-z0-9_]*)(\\s*)(\\{)?",
      "beginCaptures": {
        "1": {
          "name": "keyword.other.proto"
        },
        "3": {
          "name": "entity.name.class.proto"
        }
      },
      "end": "\\}",
      "patterns": [
        {
          "include": "#reserved"
        },
        {
          "include": "#optionStmt"
        },
        {
          "include": "#comments"
        },
        {
          "begin": "([A-Za-z][A-Za-z0-9_]*)\\s*(=)\\s*(-?0[xX][0-9a-fA-F]+|-?[0-9]+)",
          "beginCaptures": {
            "1": {
              "name": "variable.other.proto"
            },
            "2": {
              "name": "keyword.operator.assignment.proto"
            },
            "3": {
              "name": "constant.numeric.proto"
            }
          },
          "end": "(;)",
          "endCaptures": {
            "1": {
              "name": "punctuation.terminator.proto"
            }
          },
          "patterns": [
            {
              "include": "#fieldOptions"
            }
          ]
        }
      ]
    },
    "field": {
      "begin": "\\s*(optional|repeated|required)?\\s*(\\.?[\\w.]+)\\s+(\\w+)\\s*(=)\\s*(0[xX][0-9a-fA-F]+|[0-9]+)",
      "beginCaptures": {
        "1": {
          "name": "storage.modifier.proto"
        },
        "2": {
          "name": "storage.type.proto"
        },
        "3": {
          "name": "variable.other.proto"
        },
        "4": {
          "name": "keyword.operator.assignment.proto"
        },
        "5": {
          "name": "constant.numeric.proto"
        }
      },
      "end": "(;)",
      "endCaptures": {
        "1": {
          "name": "punctuation.terminator.proto"
        }
      },
      "patterns": [
        {
          "include": "#fieldOptions"
        }
      ]
    },
    "fieldOptions": {
      "begin": "\\[",
      "end": "\\]",
      "patterns": [
        {
          "include": "#constants"
        },
        {
          "include": "#number"
        },
        {
          "include": "#string"
        },
        {
          "include": "#subMsgOption"
        },
        {
          "include": "#optionName"
        }
      ]
    },
    "ident": {
      "match": "\\.?[A-Za-z][A-Za-z0-9_.]*",
      "name": "entity.name.class.proto"
    },
    "import": {
      "captures": {
        "1": {
          "name": "keyword.other.proto"
        },
        "2": {
          "name": "keyword.other.proto"
        },
        "3": {
          "name": "string.quoted.double.proto.import"
        },
        "4": {
          "name": "punctuation.terminator.proto"
        }
      },
      "match": "\\s*(import)\\s+(weak|public)?\\s*(\"[^\"]+\")\\s*(;)"
    },
    "kv": {
      "begin": "(\\w+)\\s*(:)",
      "beginCaptures": {
        "1": {
          "name": "keyword.other.proto"
        },
        "2": {
          "name": "punctuation.separator.key-value.proto"
        }
      },
      "end": "(;)|,|(?=[}/_a-zA-Z])",
      "endCaptures": {
        "1": {
          "name": "punctuation.terminator.proto"
        }
      },
      "patterns": [
        {
          "include": "#constants"
        },
        {
          "include": "#number"
        },
        {
          "include": "#string"
        },
        {
          "include": "#subMsgOption"
        }
      ]
    },
    "mapfield": {
      "begin": "\\s*(map)\\s*(<)\\s*(\\.?[\\w.]+)\\s*,\\s*(\\.?[\\w.]+)\\s*(>)\\s+(\\w+)\\s*(=)\\s*(\\d+)",
      "beginCaptures": {
        "1": {
          "name": "storage.type.proto"
        },
        "2": {
          "name": "punctuation.definition.typeparameters.begin.proto"
        },
        "3": {
          "name": "storage.type.proto"
        },
        "4": {
          "name": "storage.type.proto"
        },
        "5": {
          "name": "punctuation.definition.typeparameters.end.proto"
        },
        "6": {
          "name": "variable.other.proto"
        },
        "7": {
          "name": "keyword.operator.assignment.proto"
        },
        "8": {
          "name": "constant.numeric.proto"
        }
      },
      "end": "(;)",
      "endCaptures": {
        "1": {
          "name": "punctuation.terminator.proto"
        }
      },
      "patterns": [
        {
          "include": "#fieldOptions"
        }
      ]
    },
    "message": {
      "begin": "(message|extend)(\\s+)([A-Za-z_][A-Za-z0-9_.]*)(\\s*)(\\{)?",
      "beginCaptures": {
        "1": {
          "name": "keyword.other.proto"
        },
        "3": {
          "name": "entity.name.class.message.proto"
        }
      },
      "end": "\\}",
      "patterns": [
        {
          "include": "#reserved"
        },
        {
          "include": "${'$'}self"
        },
        {
          "include": "#enum"
        },
        {
          "include": "#optionStmt"
        },
        {
          "include": "#comments"
        },
        {
          "include": "#oneof"
        },
        {
          "include": "#field"
        },
        {
          "include": "#mapfield"
        }
      ]
    },
    "method": {
      "begin": "(rpc)\\s+([A-Za-z][A-Za-z0-9_]*)",
      "beginCaptures": {
        "1": {
          "name": "keyword.other.proto"
        },
        "2": {
          "name": "entity.name.function"
        }
      },
      "end": "\\}|(;)",
      "endCaptures": {
        "1": {
          "name": "punctuation.terminator.proto"
        }
      },
      "patterns": [
        {
          "include": "#comments"
        },
        {
          "include": "#optionStmt"
        },
        {
          "include": "#rpcKeywords"
        },
        {
          "include": "#ident"
        }
      ]
    },
    "number": {
      "match": "\\b((0(x|X)[0-9a-fA-F]*)|(([0-9]+\\.?[0-9]*)|(\\.[0-9]+))((e|E)(\\+|-)?[0-9]+)?)\\b",
      "name": "constant.numeric.proto"
    },
    "oneof": {
      "begin": "(oneof)\\s+([A-Za-z][A-Za-z0-9_]*)\\s*\\{?",
      "beginCaptures": {
        "1": {
          "name": "keyword.other.proto"
        },
        "2": {
          "name": "variable.other.proto"
        }
      },
      "end": "\\}",
      "patterns": [
        {
          "include": "#optionStmt"
        },
        {
          "include": "#comments"
        },
        {
          "include": "#field"
        }
      ]
    },
    "optionName": {
      "captures": {
        "1": {
          "name": "support.other.proto"
        },
        "2": {
          "name": "support.other.proto"
        },
        "3": {
          "name": "support.other.proto"
        }
      },
      "match": "(\\w+|\\(\\w+(\\.\\w+)*\\))(\\.\\w+)*"
    },
    "optionStmt": {
      "begin": "(option)\\s+(\\w+|\\(\\w+(\\.\\w+)*\\))(\\.\\w+)*\\s*(=)",
      "beginCaptures": {
        "1": {
          "name": "keyword.other.proto"
        },
        "2": {
          "name": "support.other.proto"
        },
        "3": {
          "name": "support.other.proto"
        },
        "4": {
          "name": "support.other.proto"
        },
        "5": {
          "name": "keyword.operator.assignment.proto"
        }
      },
      "end": "(;)",
      "endCaptures": {
        "1": {
          "name": "punctuation.terminator.proto"
        }
      },
      "patterns": [
        {
          "include": "#constants"
        },
        {
          "include": "#number"
        },
        {
          "include": "#string"
        },
        {
          "include": "#subMsgOption"
        }
      ]
    },
    "package": {
      "captures": {
        "1": {
          "name": "keyword.other.proto"
        },
        "2": {
          "name": "string.unquoted.proto.package"
        },
        "3": {
          "name": "punctuation.terminator.proto"
        }
      },
      "match": "\\s*(package)\\s+([\\w.]+)\\s*(;)"
    },
    "reserved": {
      "begin": "(reserved)\\s+",
      "beginCaptures": {
        "1": {
          "name": "keyword.other.proto"
        }
      },
      "end": "(;)",
      "endCaptures": {
        "1": {
          "name": "punctuation.terminator.proto"
        }
      },
      "patterns": [
        {
          "captures": {
            "1": {
              "name": "constant.numeric.proto"
            },
            "3": {
              "name": "keyword.other.proto"
            },
            "4": {
              "name": "constant.numeric.proto"
            }
          },
          "match": "(\\d+)(\\s+(to)\\s+(\\d+))?"
        },
        {
          "include": "#string"
        }
      ]
    },
    "rpcKeywords": {
      "match": "\\b(stream|returns)\\b",
      "name": "keyword.other.proto"
    },
    "service": {
      "begin": "(service)\\s+([A-Za-z][A-Za-z0-9_.]*)\\s*\\{?",
      "beginCaptures": {
        "1": {
          "name": "keyword.other.proto"
        },
        "2": {
          "name": "entity.name.class.message.proto"
        }
      },
      "end": "\\}",
      "patterns": [
        {
          "include": "#comments"
        },
        {
          "include": "#optionStmt"
        },
        {
          "include": "#method"
        }
      ]
    },
    "storagetypes": {
      "match": "\\b(double|float|int32|int64|uint32|uint64|sint32|sint64|fixed32|fixed64|sfixed32|sfixed64|bool|string|bytes)\\b",
      "name": "storage.type.proto"
    },
    "string": {
      "match": "([\"'])(?:\\\\.|[^\\\\])*?\\1",
      "name": "string.quoted.double.proto"
    },
    "subMsgOption": {
      "begin": "\\{",
      "end": "\\}",
      "patterns": [
        {
          "include": "#kv"
        },
        {
          "include": "#comments"
        }
      ]
    },
    "syntax": {
      "captures": {
        "1": {
          "name": "keyword.other.proto"
        },
        "2": {
          "name": "keyword.operator.assignment.proto"
        },
        "3": {
          "name": "string.quoted.double.proto.syntax"
        },
        "4": {
          "name": "punctuation.terminator.proto"
        }
      },
      "match": "\\s*(syntax)\\s*(=)\\s*(\"proto[23]\")\\s*(;)"
    }
  },
  "scopeName": "source.proto"
}
"""


// Themes

@org.intellij.lang.annotations.Language("json")
private const val SolarizedDarkTheme = """
{
  "colors": {
    "activityBar.background": "#003847",
    "agentsBottomPanel.border": "#00000000",
    "agentsCard.border": "#00000000",
    "agentsChatInput.border": "#586E7566",
    "agentsChatInput.focusBorder": "#2AA19899",
    "agentsNewSessionButton.border": "#586E7566",
    "agentsPanel.border": "#586E7566",
    "badge.background": "#047aa6",
    "button.background": "#2AA19899",
    "debugExceptionWidget.background": "#00212B",
    "debugExceptionWidget.border": "#AB395B",
    "debugToolBar.background": "#00212B",
    "dropdown.background": "#00212B",
    "dropdown.border": "#2AA19899",
    "editor.background": "#002B36",
    "editor.foreground": "#839496",
    "editor.lineHighlightBackground": "#073642",
    "editor.selectionBackground": "#274642",
    "editor.selectionHighlightBackground": "#005A6FAA",
    "editor.wordHighlightBackground": "#004454AA",
    "editor.wordHighlightStrongBackground": "#005A6FAA",
    "editorBracketHighlight.foreground1": "#cdcdcdff",
    "editorBracketHighlight.foreground2": "#b58900ff",
    "editorBracketHighlight.foreground3": "#d33682ff",
    "editorCursor.foreground": "#D30102",
    "editorGroup.border": "#00212B",
    "editorGroup.dropBackground": "#2AA19844",
    "editorGroupHeader.tabsBackground": "#004052",
    "editorHoverWidget.background": "#004052",
    "editorIndentGuide.activeBackground": "#C3E1E180",
    "editorIndentGuide.background": "#93A1A180",
    "editorLineNumber.activeForeground": "#949494",
    "editorMarkerNavigationError.background": "#AB395B",
    "editorMarkerNavigationWarning.background": "#5B7E7A",
    "editorWhitespace.foreground": "#93A1A180",
    "editorWidget.background": "#00212B",
    "errorForeground": "#ffeaea",
    "focusBorder": "#2AA19899",
    "input.background": "#003847",
    "input.foreground": "#93A1A1",
    "input.placeholderForeground": "#93A1A1AA",
    "inputOption.activeBorder": "#2AA19899",
    "inputValidation.errorBackground": "#571b26",
    "inputValidation.errorBorder": "#a92049",
    "inputValidation.infoBackground": "#052730",
    "inputValidation.infoBorder": "#363b5f",
    "inputValidation.warningBackground": "#5d5938",
    "inputValidation.warningBorder": "#9d8a5e",
    "list.activeSelectionBackground": "#005A6F",
    "list.dropBackground": "#00445488",
    "list.highlightForeground": "#1ebcc5",
    "list.hoverBackground": "#004454AA",
    "list.inactiveSelectionBackground": "#00445488",
    "minimap.selectionHighlight": "#274642",
    "modernActivityBar.activeBackground": "#005A6F",
    "modernActivityBar.background": "#00000000",
    "modernActivityBar.hoverBackground": "#005A6F87",
    "panel.border": "#2b2b4a",
    "peekView.border": "#2b2b4a",
    "peekViewEditor.background": "#10192c",
    "peekViewEditor.matchHighlightBackground": "#7744AA40",
    "peekViewResult.background": "#00212B",
    "peekViewTitle.background": "#00212B",
    "pickerGroup.border": "#2AA19899",
    "pickerGroup.foreground": "#2AA19899",
    "ports.iconRunningProcessForeground": "#369432",
    "progressBar.background": "#047aa6",
    "quickInputList.focusBackground": "#005A6F",
    "selection.background": "#2AA19899",
    "sideBar.background": "#00212B",
    "sideBarTitle.foreground": "#93A1A1",
    "statusBar.background": "#00212B",
    "statusBar.debuggingBackground": "#00212B",
    "statusBar.foreground": "#93A1A1",
    "statusBar.noFolderBackground": "#00212B",
    "statusBarItem.prominentBackground": "#003847",
    "statusBarItem.prominentHoverBackground": "#003847",
    "statusBarItem.remoteBackground": "#2AA19899",
    "surface.border": "#00222c",
    "tab.activeBackground": "#002B37",
    "tab.activeForeground": "#d6dbdb",
    "tab.border": "#003847",
    "tab.inactiveBackground": "#004052",
    "tab.inactiveForeground": "#93A1A1",
    "tab.lastPinnedBorder": "#2AA19844",
    "terminal.ansiBlack": "#073642",
    "terminal.ansiBlue": "#268bd2",
    "terminal.ansiBrightBlack": "#002b36",
    "terminal.ansiBrightBlue": "#839496",
    "terminal.ansiBrightCyan": "#93a1a1",
    "terminal.ansiBrightGreen": "#586e75",
    "terminal.ansiBrightMagenta": "#6c71c4",
    "terminal.ansiBrightRed": "#cb4b16",
    "terminal.ansiBrightWhite": "#fdf6e3",
    "terminal.ansiBrightYellow": "#657b83",
    "terminal.ansiCyan": "#2aa198",
    "terminal.ansiGreen": "#859900",
    "terminal.ansiMagenta": "#d33682",
    "terminal.ansiRed": "#dc322f",
    "terminal.ansiWhite": "#eee8d5",
    "terminal.ansiYellow": "#b58900",
    "titleBar.activeBackground": "#002C39"
  },
  "displayName": "Solarized Dark",
  "name": "solarized-dark",
  "semanticHighlighting": true,
  "tokenColors": [
    {
      "settings": {
        "foreground": "#839496"
      }
    },
    {
      "scope": [
        "meta.embedded",
        "source.groovy.embedded",
        "string meta.image.inline.markdown",
        "variable.legacy.builtin.python"
      ],
      "settings": {
        "foreground": "#839496"
      }
    },
    {
      "scope": "comment",
      "settings": {
        "fontStyle": "italic",
        "foreground": "#586E75"
      }
    },
    {
      "scope": "string",
      "settings": {
        "foreground": "#2AA198"
      }
    },
    {
      "scope": "string.regexp",
      "settings": {
        "foreground": "#DC322F"
      }
    },
    {
      "scope": "constant.numeric",
      "settings": {
        "foreground": "#D33682"
      }
    },
    {
      "scope": [
        "variable.language",
        "variable.other"
      ],
      "settings": {
        "foreground": "#268BD2"
      }
    },
    {
      "scope": "keyword",
      "settings": {
        "foreground": "#859900"
      }
    },
    {
      "scope": "storage",
      "settings": {
        "fontStyle": "bold",
        "foreground": "#93A1A1"
      }
    },
    {
      "scope": [
        "entity.name.class",
        "entity.name.type",
        "entity.name.namespace",
        "entity.name.scope-resolution"
      ],
      "settings": {
        "fontStyle": "",
        "foreground": "#CB4B16"
      }
    },
    {
      "scope": "entity.name.function",
      "settings": {
        "foreground": "#268BD2"
      }
    },
    {
      "scope": "punctuation.definition.variable",
      "settings": {
        "foreground": "#859900"
      }
    },
    {
      "scope": [
        "punctuation.section.embedded.begin",
        "punctuation.section.embedded.end"
      ],
      "settings": {
        "foreground": "#DC322F"
      }
    },
    {
      "scope": [
        "constant.language",
        "meta.preprocessor"
      ],
      "settings": {
        "foreground": "#B58900"
      }
    },
    {
      "scope": [
        "support.function.construct",
        "keyword.other.new"
      ],
      "settings": {
        "foreground": "#CB4B16"
      }
    },
    {
      "scope": [
        "constant.character",
        "constant.other"
      ],
      "settings": {
        "foreground": "#CB4B16"
      }
    },
    {
      "scope": [
        "entity.other.inherited-class",
        "punctuation.separator.namespace.ruby"
      ],
      "settings": {
        "foreground": "#6C71C4"
      }
    },
    {
      "scope": "variable.parameter",
      "settings": {
      }
    },
    {
      "scope": "entity.name.tag",
      "settings": {
        "foreground": "#268BD2"
      }
    },
    {
      "scope": "punctuation.definition.tag",
      "settings": {
        "foreground": "#586E75"
      }
    },
    {
      "scope": "entity.other.attribute-name",
      "settings": {
        "foreground": "#93A1A1"
      }
    },
    {
      "scope": "support.function",
      "settings": {
        "foreground": "#268BD2"
      }
    },
    {
      "scope": "punctuation.separator.continuation",
      "settings": {
        "foreground": "#DC322F"
      }
    },
    {
      "scope": [
        "support.constant",
        "support.variable"
      ],
      "settings": {
      }
    },
    {
      "scope": [
        "support.type",
        "support.class"
      ],
      "settings": {
        "foreground": "#859900"
      }
    },
    {
      "scope": "support.type.exception",
      "settings": {
        "foreground": "#CB4B16"
      }
    },
    {
      "scope": "support.other.variable",
      "settings": {
      }
    },
    {
      "scope": "invalid",
      "settings": {
        "foreground": "#DC322F"
      }
    },
    {
      "scope": [
        "meta.diff",
        "meta.diff.header"
      ],
      "settings": {
        "fontStyle": "italic",
        "foreground": "#268BD2"
      }
    },
    {
      "scope": "markup.deleted",
      "settings": {
        "fontStyle": "",
        "foreground": "#DC322F"
      }
    },
    {
      "scope": "markup.changed",
      "settings": {
        "fontStyle": "",
        "foreground": "#CB4B16"
      }
    },
    {
      "scope": "markup.inserted",
      "settings": {
        "foreground": "#859900"
      }
    },
    {
      "scope": "markup.quote",
      "settings": {
        "foreground": "#859900"
      }
    },
    {
      "scope": "markup.list",
      "settings": {
        "foreground": "#B58900"
      }
    },
    {
      "scope": [
        "markup.bold",
        "markup.italic"
      ],
      "settings": {
        "foreground": "#D33682"
      }
    },
    {
      "scope": "markup.bold",
      "settings": {
        "fontStyle": "bold"
      }
    },
    {
      "scope": "markup.italic",
      "settings": {
        "fontStyle": "italic"
      }
    },
    {
      "scope": "markup.strikethrough",
      "settings": {
        "fontStyle": "strikethrough"
      }
    },
    {
      "scope": "markup.inline.raw",
      "settings": {
        "fontStyle": "",
        "foreground": "#2AA198"
      }
    },
    {
      "scope": "markup.heading",
      "settings": {
        "fontStyle": "bold",
        "foreground": "#268BD2"
      }
    },
    {
      "scope": "markup.heading.setext",
      "settings": {
        "fontStyle": "",
        "foreground": "#268BD2"
      }
    }
  ],
  "type": "dark"
}
"""