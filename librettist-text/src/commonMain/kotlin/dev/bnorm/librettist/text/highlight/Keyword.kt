package ch.deletescape.highlight.core

data class Keyword(
    val value: String,
    val className: String = "keyword",
    val relevance: Int = 0
)

fun keyword(value: String, className: String = "keyword", relevance: Int = 0) = value.split(" ").map {
    val parts = it.split('|')
    val keyword = parts[0]
    val rel = parts.getOrNull(1)?.toIntOrNull() ?: relevance
    Keyword(keyword, className = className, relevance = rel)
}

fun keywords(vararg keywords: String): List<Keyword> = keywords.flatMap {
    it.toString().split(" ").map {
        val parts = it.split('|')
        val keyword = parts[0]
        val relevance = parts.getOrNull(1)?.toIntOrNull() ?: 0
        Keyword(keyword, relevance = relevance)
    }
}

