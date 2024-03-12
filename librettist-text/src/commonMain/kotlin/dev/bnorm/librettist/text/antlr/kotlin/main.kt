package dev.bnorm.librettist.text.antlr.kotlin

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import dev.bnorm.librettist.ShowTheme
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.tree.ParseTreeWalker

fun main() {
    val code = """
       @Test
       fun test() {
           val actual: List<Int> = subject.operation()
           assertEquals(4, actual.size, "Actual: ${'$'}actual !")
       }
   """.trimIndent()

    val lexer = KotlinLexer(CharStreams.fromString(code))
    val parser = KotlinParser(CommonTokenStream(lexer))
    val context = parser.kotlinFile()

    val style = ShowTheme.CodeStyle(
        simple = SpanStyle(Color(0xFFBCBEC4)),
        number = SpanStyle(Color(0xFF2AACB8)),
        keyword = SpanStyle(Color(0xFFCF8E6D)),
        punctuation = SpanStyle(Color(0xFFA1C17E)),
        annotation = SpanStyle(Color(0xFFBBB529)),
        comment = SpanStyle(Color(0xFF7A7E85)),
        string = SpanStyle(Color(0xFF6AAB73)),
    )

    buildAnnotatedString {
        withStyle(style.simple) {
            append(
                """
                   @Test()
                   fun test() {
                       val actual: List<Int> = subject.operation()
                       assertEquals(4, actual.size, "Actual: ${'$'}actual !")
                   }
               """.trimIndent()
            )
        }
        val walker = ParseTreeWalker()
        walker.walk(object : KotlinParserBaseListener() {
            override fun enterStringLiteral(ctx: KotlinParser.StringLiteralContext) {
                super.enterStringLiteral(ctx)
            }
        }, context)
    }
}
