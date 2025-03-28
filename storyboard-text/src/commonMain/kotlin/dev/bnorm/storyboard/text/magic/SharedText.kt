package dev.bnorm.storyboard.text.magic

import androidx.compose.ui.text.AnnotatedString

internal class SharedText(
    val value: AnnotatedString,
    val key: String? = null,
    val crossFade: Boolean = false,
)

private class MutableSharedText(
    val index: Int,
    val value: AnnotatedString,
) : Comparable<MutableSharedText> {

    var key: String? = null
    var crossFade: Boolean = false

    var prev: MutableSharedText? = null
    var next: MutableSharedText? = null

    fun move(direction: Move, stopOnLineBreaks: Boolean): MutableSharedText? {
        tailrec fun rec(text: MutableSharedText): MutableSharedText? {
            val iter = when (direction) {
                Move.Next -> text.next
                Move.Previous -> text.prev
            }
            return when {
                iter == null -> null
                iter.value.text != "\n" -> iter
                stopOnLineBreaks -> null
                else -> rec(iter)
            }
        }

        return rec(this)
    }

    fun toSharedItem(): SharedText = SharedText(value, key, crossFade)

    override fun compareTo(other: MutableSharedText): Int {
        return compareValues(index, other.index)
    }

    override fun toString(): String {
        return "MutableSharedItem(value=$value, key=$key, crossFade=$crossFade)"
    }
}

internal fun findShared(
    before: List<AnnotatedString>,
    after: List<AnnotatedString>,
): Pair<List<SharedText>, List<SharedText>> {
    val beforeItems = before.mapIndexed { index, value -> MutableSharedText(index, value) }
    val afterItems = after.mapIndexed { index, value -> MutableSharedText(index, value) }

    val beforeNonBlank = beforeItems.filter { it.value.isNotBlank() || it.value.text == "\n" }
    val afterNonBlank = afterItems.filter { it.value.isNotBlank() || it.value.text == "\n" }

    var prev: MutableSharedText? = null
    for (item in beforeNonBlank) {
        item.prev = prev
        prev?.next = item
        prev = item
    }

    prev = null
    for (item in afterNonBlank) {
        item.prev = prev
        prev?.next = item
        prev = item
    }

    findSharedInternal(beforeNonBlank, afterNonBlank)

    // TODO is there a way to merge items to help with large amounts of text that don't change?
    return beforeItems.map { it.toSharedItem() } to afterItems.map { it.toSharedItem() }
}

private enum class Move {
    Next,
    Previous,
    ;
}

private data class MatchPair(
    val before: MutableSharedText,
    val after: MutableSharedText,
) : Comparable<MatchPair> {
    fun move(direction: Move, stopOnLineBreaks: Boolean): MatchPair? {
        return MatchPair(
            before = before.move(direction, stopOnLineBreaks) ?: return null,
            after = after.move(direction, stopOnLineBreaks) ?: return null,
        )
    }

    fun couldAssociate(): Boolean = unkeyed() && matches()

    fun unkeyed(): Boolean {
        return before.key == null && after.key == null
    }

    fun matches(): Boolean {
        return before.value.text == after.value.text
    }

    fun sequence(direction: Move, stopOnLineBreaks: Boolean): Sequence<MatchPair> =
        generateSequence(this) { it.move(direction, stopOnLineBreaks) }

    override fun compareTo(other: MatchPair): Int {
        return when {
            after < other.after && before < other.before -> -1
            after > other.after && before > other.before -> 1
            after == other.after && before == other.before -> 0
            else -> error("!")
        }
    }
}

@ConsistentCopyVisibility
private data class MatchRegion private constructor(
    val start: MatchPair,
    val end: MatchPair,
) {
    constructor(pair: MatchPair) : this(pair, pair)

    fun widen(pair: MatchPair): MatchRegion {
        return when {
            pair == start || pair == end -> this
            pair > end -> MatchRegion(start, pair)
            pair < start -> MatchRegion(pair, end)
            else -> error("!")
        }
    }
}

private fun findSharedInternal(
    beforeList: List<MutableSharedText>,
    afterList: List<MutableSharedText>,
) {
    val matches = mutableListOf<MatchRegion>()
    val ignoredKeys = mutableSetOf<String>()

    var index = 1
    fun nextKey() = "stable:${index++}"

    fun associate(pair: MatchPair) {
        val key = nextKey()
        pair.before.key = key
        pair.after.key = key

        if (pair.before.value != pair.after.value) {
            pair.before.crossFade = true
            pair.after.crossFade = true
        }
    }

    fun associateDirection(
        pair: MatchPair,
        direction: Move,
        stopOnLineBreaks: Boolean,
    ): MatchPair {
        return pair.sequence(direction, stopOnLineBreaks)
            .drop(1)
            .takeWhile { it.couldAssociate() }
            .onEach { associate(it) }
            .lastOrNull() ?: pair
    }

    fun countDirection(
        pair: MatchPair,
        direction: Move,
        stopOnLineBreaks: Boolean,
    ): Int {
        return pair.sequence(direction, stopOnLineBreaks)
            .drop(1)
            .takeWhile { it.couldAssociate() }
            .count()
    }

    fun associateCenter(pair: MatchPair, stopOnLineBreaks: Boolean): MatchRegion {
        ignoredKeys.clear()

        var region = MatchRegion(pair)
        associate(pair)
        region = region.widen(associateDirection(pair, Move.Next, stopOnLineBreaks))
        region = region.widen(associateDirection(pair, Move.Previous, stopOnLineBreaks))
        return region
    }

    fun findMultiMatch(
        sharedKeys: Set<String>,
        groupedBefore: Map<String, List<MutableSharedText>>,
        groupedAfter: Map<String, List<MutableSharedText>>,
        ignoredKeys: MutableSet<String>,
        stopOnLineBreaks: Boolean,
        skipKeysWithoutBest: Boolean,
    ): MatchRegion? {
        for (key in sharedKeys) {
            val combinations = mutableListOf<Pair<Int, MatchPair>>()

            val beforeItems = groupedBefore.getValue(key)
            val afterItems = groupedAfter.getValue(key)
            for (before in beforeItems) {
                for (after in afterItems) {
                    val pair = MatchPair(before, after)
                    val strength = countDirection(pair, Move.Next, stopOnLineBreaks) +
                            countDirection(pair, Move.Previous, stopOnLineBreaks)
                    combinations.add(strength to pair)
                }
            }

            combinations.sortBy { -it.first }

            if (skipKeysWithoutBest && combinations[0].first == combinations[1].first) {
                ignoredKeys.add(key)
            } else {
                val pair = combinations[0].second
                return associateCenter(pair, stopOnLineBreaks)
            }
        }

        return null
    }

    fun matchUnique(stopOnLineBreaks: Boolean, skipKeysWithoutBest: Boolean) {
        ignoredKeys.clear()

        // TODO use priority queue of "key" to "value size" to avoid multiple loops
        outer@ while (true) {
            // TODO could optimize the management of these maps quite a bit i bet...
            var groupedBefore: Map<String, List<MutableSharedText>>
            var groupedAfter: Map<String, List<MutableSharedText>>

            unique@ while (true) {
                groupedBefore = beforeList.filter { it.key == null && it.value.text != "\n" }.groupBy { it.value.text }
                groupedAfter = afterList.filter { it.key == null && it.value.text != "\n" }.groupBy { it.value.text }

                val beforeKeys = groupedBefore.filter { it.value.size == 1 }.keys
                val afterKeys = groupedAfter.filter { it.value.size == 1 }.keys

                val sharedKeys = beforeKeys intersect afterKeys
                if (sharedKeys.isEmpty()) break@unique

                for (key in sharedKeys) {
                    val pair = MatchPair(groupedBefore.getValue(key)[0], groupedAfter.getValue(key)[0])
                    matches.add(associateCenter(pair, stopOnLineBreaks))
                }
            }

            // TODO improvements:
            //  - up to 3?
            //  - sort by count and take first?
            //  - this is exponential, so it should be limited... maybe?
            val beforeKeys = groupedBefore.filter { it.value.size <= 4 }.keys
            val afterKeys = groupedAfter.filter { it.value.size <= 4 }.keys

            val sharedKeys = (beforeKeys intersect afterKeys) - ignoredKeys
            if (sharedKeys.isEmpty()) break@outer

            val region = findMultiMatch(
                sharedKeys,
                groupedBefore,
                groupedAfter,
                ignoredKeys,
                stopOnLineBreaks,
                skipKeysWithoutBest,
            ) ?: break@outer
            matches.add(region)
        }
    }

    fun widenMatches() {
        for (region in matches.toList().also { matches.clear() }) {
            var wider = region
            wider = wider.widen(associateDirection(wider.end, Move.Next, stopOnLineBreaks = false))
            wider = wider.widen(associateDirection(wider.start, Move.Previous, stopOnLineBreaks = false))
            matches.add(wider)
        }
    }

    fun matchEdges() {
        // Match edges as much as possible as a last option.
        if (beforeList.isNotEmpty() && afterList.isNotEmpty()) {
            run {
                val pair = MatchPair(beforeList.first(), afterList.first())
                if (pair.matches() && pair.unkeyed()) {
                    associate(pair)
                    associateDirection(pair, Move.Next, stopOnLineBreaks = false)
                }
            }

            run {
                val pair = MatchPair(beforeList.last(), afterList.last())
                if (pair.matches() && pair.unkeyed()) {
                    associate(pair)
                    associateDirection(pair, Move.Previous, stopOnLineBreaks = false)
                }
            }
        }
    }

    matchUnique(stopOnLineBreaks = true, skipKeysWithoutBest = true)
    widenMatches()
    matchEdges()
    matchUnique(stopOnLineBreaks = false, skipKeysWithoutBest = false)
}
