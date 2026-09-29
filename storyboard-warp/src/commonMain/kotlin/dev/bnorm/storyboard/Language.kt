package dev.bnorm.storyboard


/**
 * The canonical list of `languages` supported for parsing and rendering.
 */
sealed interface Language {
    fun scopeName(): String
}

// When adding well-known languages here. Also, add the corresponding entries to the language registry for all
// Parser implementations.

/** JavaScript Object notation. */
object Json : Language {
    override fun scopeName(): String {
        return "source.json"
    }
}

/** The Kotlin programming language. */
object Kotlin : Language {
    override fun scopeName(): String {
        return "source.kotlin"
    }
}

/** XML */
object Xml : Language {
    override fun scopeName(): String {
        return "text.xml"
    }
}

/** Protobuf v3 */
object ProtobufV3 : Language {
    override fun scopeName(): String {
        return "source.proto"
    }
}

/** A custom language that is [Parser] aware */
class Custom(val parser: Parser) : Language {
    override fun scopeName(): String {
        return "scope.custom"
    }
}
