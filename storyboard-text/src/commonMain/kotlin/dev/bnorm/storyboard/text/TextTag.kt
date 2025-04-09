package dev.bnorm.storyboard.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

data class TextTag(
    val id: String,
    val description: String,
    val category: String? = null,
) {
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
                val tag = TextTag(match.groupValues[1], "", null)
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

    // TODO(CMP-7955) this is actually a bug!
    // Note: unfortunately, it seems that while styles are applied first to last,
    // they are also applied start to end.
    //
    // Consider the following AnnotatedString, with the applied styles:
    //              "Hello, World!"
    // style1               |---|
    // style2        |-----------|
    //
    // In this case (it seems) that while 'style2' was applied after 'style1',
    // 'style1' will still be displayed because it starts at a later offset.
    // It seems like there is some kind of running style,
    // and new styles are folded in as they are encountered,
    // without existing styles being reapplied.

    val newText = buildAnnotatedString {
        // TODO optimize by only adding the text?
        //  - would need to also add the TextTag annotations as well
        //  - AnnotatedString.Builder.flatMapAnnotations might be the answer?!
        //     - would need to do that as a last step...
        append(this@addStyleByTag)

        var last = 0
        for (range in ranges) {
            if (range.start != last && untagged != null) {
                // !!! Note: see the comment on 'newText' for why this is needed.
                val styles = this@addStyleByTag.subSequence(last, range.start).spanStyles
                for (subRange in styles) {
                    addStyle(subRange.item + untagged, last + subRange.start, last + subRange.end)
                }
                addStyle(untagged, last, range.start)
            }
            if (tagged != null) {
                // !!! Note: see the comment on 'newText' for why this is needed.
                val styles = this@addStyleByTag.subSequence(range.start, range.end).spanStyles
                for (subRange in styles) {
                    addStyle(subRange.item + tagged, range.start + subRange.start, range.start + subRange.end)
                }
                addStyle(tagged, range.start, range.end)
            }
            last = range.end
        }

        if (last != text.length && untagged != null) {
            // !!! Note: see the comment on 'newText' for why this is needed.
            val styles = this@addStyleByTag.subSequence(last, text.length).spanStyles
            for (subRange in styles) {
                addStyle(subRange.item + untagged, last + subRange.start, last + subRange.end)
            }
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
        builder.addStringAnnotation(TextTag.TAG_NAME, tag.id, builder.length - replacement.length, builder.length)

        last = range.end
    }

    if (last != length) {
        builder.append(subSequence(last, length))
    }

    return builder.toAnnotatedString()
}

fun AnnotatedString.splitByTags(): List<AnnotatedString> {
    val ranges = getStringAnnotations(TextTag.TAG_NAME, 0, length)
    val splits = buildSet {
        for (range in ranges) {
            add(range.start)
            add(range.end)
        }

        var padding = true
        text.forEachIndexed { index, char ->
            if (padding && !char.isWhitespace()) {
                add(index)
                padding = false
            }
            if (char == '\n') {
                add(index)
                padding = true
            }
        }
    }.sorted()

    if (splits.isEmpty()) return listOf(this)

    val str = this
    val split = buildList {
        var last = 0
        for (next in splits) {
            if (next <= last) continue
            add(str.subSequence(last, next))
            last = next
        }
        if (last < length) {
            add(str.subSequence(last, length))
        }
    }
    return split
}
