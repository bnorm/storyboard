package dev.bnorm.storyboard

enum class AdvanceDirection {
    Forward,
    Backward,
    ;

    companion object {
        fun <T : Comparable<T>> from(current: T, target: T): AdvanceDirection? {
            val compare = current.compareTo(target)
            return when {
                compare < 0 -> Forward
                compare > 0 -> Backward
                else -> null
            }
        }

        fun <T> from(current: T, target: T, comparator: Comparator<T>): AdvanceDirection? {
            val compare = comparator.compare(current, target)
            return when {
                compare < 0 -> Forward
                compare > 0 -> Backward
                else -> null
            }
        }
    }
}
