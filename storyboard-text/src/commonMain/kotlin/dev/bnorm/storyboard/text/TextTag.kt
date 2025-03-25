package dev.bnorm.storyboard.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

data class TextTag(val id: String) {
    init {
        require(TAG_START !in id && TAG_END !in id) { "id cannot contain '$TAG_START' or '$TAG_END': $id" }
    }

    override fun toString(): String {
        return "$TAG_START$id$TAG_END"
    }

    companion object {
        // TODO could we make these tags customizable?
        //  - within a TextTagScope for example?
        private const val TAG_START = '⦕'
        private const val TAG_END = '⦖'
        val REGEX = "$TAG_START(?<id>[^$TAG_START$TAG_END]+)$TAG_END".toRegex()
        internal const val TAG_NAME = "$TAG_START$TAG_END"

        fun extractTags(str: AnnotatedString): AnnotatedString {
            return buildAnnotatedString {
                // Merge the original AnnotatedString.
                append(str)

                // And extracted tags from the raw text of the original AnnotatedString.
                val ranges = extractTags(str.text).getStringAnnotations(TAG_NAME, 0, length)
                for (range in ranges) {
                    addStringAnnotation(TAG_NAME, range.item, range.start, range.end)
                }
            }
        }

        fun extractTags(str: String): AnnotatedString {
            val starts = mutableMapOf<TextTag, Int>()
            val annotatedString = AnnotatedString.Builder()

            var offset = 0
            var removed = 0
            for (match in REGEX.findAll(str)) {
                // Build string without tags.
                annotatedString.append(str.substring(offset, match.range.start))
                offset = match.range.endInclusive + 1

                // Build a list of tag ranges.
                val tag = TextTag(match.groupValues[1])
                val last = starts.remove(tag)
                if (last != null) {
                    val range = last..<(match.range.start - removed)
                    annotatedString.addStringAnnotation(
                        tag = TAG_NAME,
                        annotation = tag.id,
                        start = range.start,
                        end = range.endInclusive + 1
                    )
                } else {
                    starts.put(tag, match.range.start - removed)
                }
                removed += match.value.length
            }

            // Add remaining text to string.
            if (offset < str.length) {
                annotatedString.append(str.substring(offset, str.length))
            }

            // Add remaining tags to string.
            // TODO should this be an error?
            for ((tag, start) in starts) {
                val range = (start - removed)..(str.length - removed)
                annotatedString.addStringAnnotation(
                    tag = TAG_NAME,
                    annotation = tag.id,
                    start = range.start,
                    end = range.endInclusive + 1
                )
            }

            return annotatedString.toAnnotatedString()
        }
    }
}

fun AnnotatedString.addStyleByTag(
    tag: TextTag,
    tagged: SpanStyle? = null,
    untagged: SpanStyle? = null,
): AnnotatedString {
    if (tagged == null && untagged == null) return this

    val ranges = getStringAnnotations(TextTag.TAG_NAME, 0, length).filter { it.item == tag.id }
    if (ranges.isEmpty()) {
        return when (untagged) {
            null -> this
            else -> buildAnnotatedString {
                append(this@addStyleByTag)
                addStyle(untagged, 0, length)
            }
        }
    }

    val newText = buildAnnotatedString {
        append(this@addStyleByTag)

        var last = 0
        for (range in ranges) {
            if (range.start != last && untagged != null) addStyle(untagged, last, range.start)
            if (tagged != null) addStyle(tagged, range.start, range.end)
            last = range.end
        }

        if (last != text.length && untagged != null) {
            addStyle(untagged, last, text.length)
        }
    }

    return newText
}

fun AnnotatedString.replaceAllByTag(
    tag: TextTag,
    replacement: AnnotatedString,
): AnnotatedString {
    val ranges = getStringAnnotations(TextTag.TAG_NAME, 0, length).filter { it.item == tag.id }
    if (ranges.isEmpty()) return this

    val builder = AnnotatedString.Builder()
    var last = 0
    for (range in ranges.sortedBy { it.start }) {
        if (range.start != last) {
            builder.append(subSequence(last, range.start))
        }
        // TODO if there is no replacement, could we merging continuous annotations?
        if (replacement.isNotEmpty()) {
            builder.append(replacement)
        }
        last = range.end
    }

    if (last != length) {
        builder.append(subSequence(last, length))
    }

    return builder.toAnnotatedString()
}
