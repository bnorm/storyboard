package dev.bnorm.storyboard.text.magic

import androidx.compose.ui.text.AnnotatedString

internal class SharedText(
    val value: AnnotatedString,
    val key: String? = null,
    val crossFade: Boolean = false,
)

private class MutableSharedText(
    val value: AnnotatedString,
) {
    var key: String? = null
    var crossFade: Boolean = false

    var prev: MutableSharedText? = null
    var next: MutableSharedText? = null

    fun toSharedItem(): SharedText = SharedText(value, key, crossFade)

    override fun toString(): String {
        return "MutableSharedItem(value=$value, key=$key, crossFade=$crossFade)"
    }
}

internal fun findShared(
    before: List<AnnotatedString>,
    after: List<AnnotatedString>,
): Pair<List<SharedText>, List<SharedText>> {
    val beforeItems = before.map { MutableSharedText(it) }
    val afterItems = after.map { MutableSharedText(it) }

    val beforeNonBlank = beforeItems.filter { it.value.isNotBlank() }
    val afterNonBlank = afterItems.filter { it.value.isNotBlank() }

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

    return beforeItems.map { it.toSharedItem() } to afterItems.map { it.toSharedItem() }
}

private fun findSharedInternal(
    beforeList: List<MutableSharedText>,
    afterList: List<MutableSharedText>,
) {
    var index = 1
    fun nextKey() = "stable:${index++}"

    fun associate(before: MutableSharedText, after: MutableSharedText) {
        val key = nextKey()
        before.key = key
        after.key = key

        if (before.value != after.value) {
            before.crossFade = true
            after.crossFade = true
        }
    }

    fun associateForward(before: MutableSharedText, after: MutableSharedText) {
        var nextBefore = before.next
        var nextAfter = after.next
        while (nextBefore != null && nextAfter != null) {
            if (nextBefore.key != null || nextAfter.key != null) break
            if (nextBefore.value.text != nextAfter.value.text) break

            associate(nextBefore, nextAfter)
            nextBefore = nextBefore.next
            nextAfter = nextAfter.next
        }
    }

    fun associateBackward(before: MutableSharedText, after: MutableSharedText) {
        var prevBefore = before.prev
        var prevAfter = after.prev
        while (prevBefore != null && prevAfter != null) {
            if (prevBefore.key != null || prevAfter.key != null) break
            if (prevBefore.value.text != prevAfter.value.text) break

            associate(prevBefore, prevAfter)
            prevBefore = prevBefore.prev
            prevAfter = prevAfter.prev
        }
    }

    fun countForward(before: MutableSharedText, after: MutableSharedText): Int {
        var iterBefore = before
        var iterAfter = after
        var count = 0

        while (true) {
            val nextBefore = iterBefore.next ?: break
            val nextAfter = iterAfter.next ?: break

            if (nextBefore.key != null || nextAfter.key != null) break
            if (nextBefore.value.text != nextAfter.value.text) break

            iterBefore = nextBefore
            iterAfter = nextAfter
            count++
        }

        return count
    }

    fun countBackward(before: MutableSharedText, after: MutableSharedText): Int {
        var iterBefore = before
        var iterAfter = after
        var count = 0

        while (true) {
            val prevBefore = iterBefore.prev ?: break
            val prevAfter = iterAfter.prev ?: break

            if (prevBefore.key != null || prevAfter.key != null) break
            if (prevBefore.value.text != prevAfter.value.text) break

            iterBefore = prevBefore
            iterAfter = prevAfter
            count++
        }

        return count
    }

    // TODO use priority queue of "key" to "value size" to avoid multiple loops
    outer@ while (true) {
        // TODO could optimize the management of these maps quite a bit i bet...
        var groupedBefore: Map<String, List<MutableSharedText>>
        var groupedAfter: Map<String, List<MutableSharedText>>

        unique@ while (true) {
            groupedBefore = beforeList.filter { it.key == null }.groupBy { it.value.text }
            groupedAfter = afterList.filter { it.key == null }.groupBy { it.value.text }

            val beforeKeys = groupedBefore.filter { it.value.size == 1 }.keys
            val afterKeys = groupedAfter.filter { it.value.size == 1 }.keys

            val sharedKeys = beforeKeys intersect afterKeys
            if (sharedKeys.isEmpty()) break@unique

            for (key in sharedKeys) {
                val before = groupedBefore.getValue(key)[0]
                val after = groupedAfter.getValue(key)[0]
                associate(before, after)
                associateForward(before, after)
                associateBackward(before, after)
            }
        }

        // TODO improvements:
        //  - up to 3?
        //  - sort by count and take first?
        //  - this is exponential, so it should be limited... maybe?
        val beforeKeys = groupedBefore.filter { it.value.size <= 4 }.keys
        val afterKeys = groupedAfter.filter { it.value.size <= 4 }.keys

        val sharedKeys = beforeKeys intersect afterKeys
        if (sharedKeys.isEmpty()) break@outer

        double@ for (key in sharedKeys) {
            val combinations = buildList {
                val beforeItems = groupedBefore.getValue(key)
                val afterItems = groupedAfter.getValue(key)
                for (before in beforeItems) {
                    for (after in afterItems) {
                        add(before to after)
                    }
                }
            }

            val (before, after) = combinations.maxBy { (before, after) ->
                countForward(before, after) + countBackward(before, after)
            }

            associate(before, after)
            associateForward(before, after)
            associateBackward(before, after)

            continue@outer
        }
    }

    // Match edges as much as possible as a last option.
    if (beforeList.isNotEmpty() && afterList.isNotEmpty()) {
        run {
            val before = beforeList.first()
            val after = afterList.first()
            if (before.value.text == after.value.text && before.key == null && after.key == null) {
                associate(before, after)
                associateForward(before, after)
            }
        }

        run {
            val before = beforeList.last()
            val after = afterList.last()
            if (before.value.text == after.value.text && before.key == null && after.key == null) {
                associate(before, after)
                associateBackward(before, after)
            }
        }
    }
}
