package ch.deletescape.highlight.render

import ch.deletescape.highlight.core.Highlighter
import ch.deletescape.highlight.core.StyleRenderer

/*
 * Created by Sergej Kravcenko on 5/16/2017.
 * Copyright (c) 2017 Sergej Kravcenko
 */

fun main() {
   val code = """
       @Test
       fun test() {
           val actual: List<Int> = subject.operation()
           assertEquals(4, actual.size, "Actual: ${'$'}actual !")
       }
   """.trimIndent()

   val highlighter = Highlighter { HtmlRenderer("") }
   val result = highlighter.highlight("kotlin", code, graceful = false)
   println(result?.result)
}

/**
 * Basic HTML renderer similar to Highlight.js
 */
class HtmlRenderer(private val prefix: String) : StyleRenderer<String> {
   private var result: String = ""

   override fun onStart() {
      result = ""
   }
   
   override fun onFinish() {

   }

   override fun onPushStyle(style: String) {
      result += "<span class=\"${prefix}${style}\">"
   }

   override fun onPopStyle() {
      result += "</span>"
   }

   override fun onPushCodeBlock(block: CharSequence) {
      result += block.toString().escape()
   }

   override fun onPushSubLanguage(name: String?, code: CharSequence?) {
      result += "<span class=\"$name\">${code ?: ""}</span>"
   }

   override fun onAbort(code: CharSequence) {
      result = code.toString()
   }

   override fun getResult() = result

   private fun String.escape() = replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
}
