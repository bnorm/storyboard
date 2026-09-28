package dev.bnorm.storyboard

/**
 * The [Token] that will be passed to the diffing algorithm to find structural similarities.
 *
 * We are using a combination of the [content], and the `primary` [scope] + its [depth] to find
 * `anchor`s.
 */
class Token(
    /** The actual content of the parsed token. */
    val content: String,
    /** The primary scope */
    val scope: String,
    /** The depth of the primary scope. */
    val depth: Int,
    /* More context for animations and rendering. */
    val language: Language,
    val allScopes: List<String>,
    val lineNumber: Int,
    val startIndex: Int,
    val endIndex: Int
) {
    /** The underlying unique key that was assigned to the token.
     * This is guaranteed to be stable across a story board. */
    private var key: String? = null

    /**
     * A related token, when applicable. This is only ever populated for [Token]s that are
     * brackets. So `}`, `]`, `>`, `)` will point to their corresponding matching pairs.
     *
     * This helps ensure that once we find a match for one of these [Token]s, we also match the
     * corresponding matching related tokens.
     */
    public var related: Token? = null
        private set

    public var relatedIndex: Int? = null
        private set

    fun hasKey(): Boolean {
        return key != null
    }

    fun assignKey(newKey: String) {
        val key = key
        check(key == null) { "Cannot override `key` for $this" }
        this.key = newKey
    }

    fun assignRelated(related: Token, relatedIndex: Int) {
        this.related = related
        this.relatedIndex = relatedIndex
    }

    fun key(): String {
        val contentId = key
        check(contentId != null) { "`key` was not assigned to $this" }
        return contentId
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Token) return false
        if (content != other.content) return false
        if (scope != other.scope) return false

        return true
    }

    override fun hashCode(): Int {
        var result = depth
        result = 31 * result + content.hashCode()
        result = 31 * result + scope.hashCode()
        return result
    }

    override fun toString(): String {
        return "Token(content='$content', scope='$scope', depth=$depth, lineNumber=$lineNumber, startIndex=$startIndex, endIndex=$endIndex)"
    }
}
