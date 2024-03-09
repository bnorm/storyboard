package ch.deletescape.highlight.core

/**
 * Various helper functions used inside Language definitions
 */

internal inline fun Mode.Companion.getLanguage(lang: String) = Highlighter.findLanguage(lang)!!
internal fun Mode.Companion.COMMENT(start: Any? = null, second: Any? = null, third: Any? = null): Mode {
    val end = (if (third != null && second !is Mode) second else if (second != false) second else second).toString()
    val inherits = if (third == null) { if (second is Mode) second else null } else third as? Mode?
    return COMMENT(start?.toString(), end, inherits)
}
internal typealias hljs = Mode

