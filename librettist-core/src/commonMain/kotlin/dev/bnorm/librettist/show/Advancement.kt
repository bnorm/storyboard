package dev.bnorm.librettist.show

data class Advancement(
    val direction: Direction,
) {
    enum class Direction {
        Forward,
        Backward,
        ;

        fun <T> toValue(forward: T, backward: T): T {
            return when (this) {
                Forward -> forward
                Backward -> backward
            }
        }
    }
}
