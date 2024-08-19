package dev.bnorm.storyboard.text.highlight

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.SpanStyle
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap

@Immutable
class Highlighting private constructor(
    val simple: SpanStyle,
    val number: SpanStyle,
    val keyword: SpanStyle,
    val punctuation: SpanStyle,
    val annotation: SpanStyle,
    val comment: SpanStyle,
    val string: SpanStyle,
    val property: SpanStyle,
    val functionDeclaration: SpanStyle,
    val staticFunctionCall: SpanStyle,
    val extensionFunctionCall: SpanStyle,
    val typeParameters: SpanStyle,

    // Language specific extensions
    val extensions: ImmutableMap<String, Highlighting>,
) {
    fun get(extension: String): Highlighting = extensions[extension] ?: this

    companion object {
        val current: Highlighting
            @Composable
            get() = LocalHighlighting.current

        fun build(
            base: SpanStyle = SpanStyle(),
            builder: Builder.() -> Unit,
        ): Highlighting {
            return Builder(
                base = Highlighting(
                    simple = base,
                    number = base,
                    keyword = base,
                    punctuation = base,
                    annotation = base,
                    comment = base,
                    string = base,
                    property = base,
                    functionDeclaration = base,
                    staticFunctionCall = base,
                    extensionFunctionCall = base,
                    typeParameters = base,
                    extensions = persistentMapOf()
                )
            ).apply(builder).build()
        }
    }

    class Builder internal constructor(base: Highlighting) {
        var simple: SpanStyle = base.simple
        var number: SpanStyle = base.number
        var keyword: SpanStyle = base.keyword
        var punctuation: SpanStyle = base.punctuation
        var annotation: SpanStyle = base.annotation
        var comment: SpanStyle = base.comment
        var string: SpanStyle = base.string
        var property: SpanStyle = base.property
        var functionDeclaration: SpanStyle = base.functionDeclaration
        var staticFunctionCall: SpanStyle = base.staticFunctionCall
        var extensionFunctionCall: SpanStyle = base.extensionFunctionCall
        var typeParameters: SpanStyle = base.typeParameters

        val extensions: MutableMap<String, Highlighting> = base.extensions.toMutableMap()

        fun build(): Highlighting {
            return Highlighting(
                simple = simple,
                number = number,
                keyword = keyword,
                punctuation = punctuation,
                annotation = annotation,
                comment = comment,
                string = string,
                property = property,
                functionDeclaration = functionDeclaration,
                staticFunctionCall = staticFunctionCall,
                extensionFunctionCall = extensionFunctionCall,
                typeParameters = typeParameters,
                extensions = extensions.toImmutableMap(),
            )
        }
    }
}

private val LocalHighlighting = staticCompositionLocalOf<Highlighting> {
    // TODO provide a default highlighting
    //  compatible with default material theme?
    error("Highlighting is not provided")
}

@Composable
fun Highlighting(highlighting: Highlighting, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalHighlighting provides highlighting) {
        content()
    }
}

@Composable
fun <T : Any> rememberHighlighted(vararg key: Any?, content: (Highlighting) -> T): T {
    val highlighting = Highlighting.current
    return rememberSaveable(highlighting, *key) { content(highlighting) }
}
