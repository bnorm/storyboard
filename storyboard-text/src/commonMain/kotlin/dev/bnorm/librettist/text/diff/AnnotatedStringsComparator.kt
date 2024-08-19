package org.apache.commons.text.diff

import androidx.compose.ui.text.AnnotatedString

class AnnotatedStringsComparator(
    private val left: AnnotatedString,
    private val right: AnnotatedString
) {
    private class Snake(
        val start: Int,
        val end: Int,
        val diag: Int
    )

    private val vDown: IntArray

    private val vUp: IntArray

    init {
        val size = left.length + right.length + 2
        vDown = IntArray(size)
        vUp = IntArray(size)
    }

    private fun buildScript(
        start1: Int, end1: Int, start2: Int, end2: Int,
        script: EditScript<AnnotatedString>
    ) {
        val middle = getMiddleSnake(start1, end1, start2, end2)

        if (middle == null || middle.start == end1 && middle.diag == end1 - end2 || middle.end == start1 && middle.diag == start1 - start2) {
            var i = start1
            var j = start2
            while (i < end1 || j < end2) {
                val leftSequence by lazy { left.subSequence(i, i + 1) }
                val rightSequence by lazy { right.subSequence(j, j + 1) }
                if (i < end1 && j < end2 && leftSequence == rightSequence) {
                    script.append(KeepCommand(leftSequence))
                    ++i
                    ++j
                } else if (end1 - start1 > end2 - start2) {
                    script.append(DeleteCommand(leftSequence))
                    ++i
                } else {
                    script.append(InsertCommand(rightSequence))
                    ++j
                }
            }
        } else {
            buildScript(
                start1, middle.start,
                start2, middle.start - middle.diag,
                script
            )
            for (i in middle.start until middle.end) {
                script.append(KeepCommand(left.subSequence(i, i + 1)))
            }
            buildScript(
                middle.end, end1,
                middle.end - middle.diag, end2,
                script
            )
        }
    }

    /**
     * Builds a snake.
     *
     * @param start  the value of the start of the snake
     * @param diag  the value of the diagonal of the snake
     * @param end1  the value of the end of the first sequence to be compared
     * @param end2  the value of the end of the second sequence to be compared
     * @return The snake built
     */
    private fun buildSnake(start: Int, diag: Int, end1: Int, end2: Int): Snake {
        var end = start
        while (end - diag < end2 && end < end1 && left[end] == right[end - diag]) {
            ++end
        }
        return Snake(start, end, diag)
    }

    /**
     * Gets the middle snake corresponding to two subsequences of the
     * main sequences.
     *
     *
     * The snake is found using the MYERS Algorithm (this algorithms has
     * also been implemented in the GNU diff program). This algorithm is
     * explained in Eugene Myers article:
     * [An O(ND) Difference Algorithm and Its Variations](http://www.cs.arizona.edu/people/gene/PAPERS/diff.ps).
     *
     *
     * @param start1  the begin of the first sequence to be compared
     * @param end1  the end of the first sequence to be compared
     * @param start2  the begin of the second sequence to be compared
     * @param end2  the end of the second sequence to be compared
     * @return The middle snake
     */
    private fun getMiddleSnake(start1: Int, end1: Int, start2: Int, end2: Int): Snake? {
        // Myers Algorithm

        // Initialisations
        val m = end1 - start1
        val n = end2 - start2
        if (m == 0 || n == 0) {
            return null
        }

        val delta = m - n
        val sum = n + m
        val offset = (if (sum % 2 == 0) sum else sum + 1) / 2
        vDown[1 + offset] = start1
        vUp[1 + offset] = end1 + 1

        for (d in 0..offset) {
            // Down
            run {
                var k = -d
                while (k <= d) {
                    // First step
                    val i = k + offset
                    if (k == -d || k != d && vDown[i - 1] < vDown[i + 1]) {
                        vDown[i] = vDown[i + 1]
                    } else {
                        vDown[i] = vDown[i - 1] + 1
                    }

                    var x = vDown[i]
                    var y = x - start1 + start2 - k

                    while (x < end1 && y < end2 && left[x] == right[y]) {
                        vDown[i] = ++x
                        ++y
                    }
                    // Second step
                    if (delta % 2 != 0 && delta - d <= k && k <= delta + d) {
                        if (vUp[i - delta] <= vDown[i]) { // NOPMD
                            return buildSnake(vUp[i - delta], k + start1 - start2, end1, end2)
                        }
                    }
                    k += 2
                }
            }

            // Up
            run {
                var k = delta - d
                while (k <= delta + d) {
                    // First step
                    val i = k + offset - delta
                    if (k == delta - d
                        || k != delta + d && vUp[i + 1] <= vUp[i - 1]
                    ) {
                        vUp[i] = vUp[i + 1] - 1
                    } else {
                        vUp[i] = vUp[i - 1]
                    }

                    var x = vUp[i] - 1
                    var y = x - start1 + start2 - k
                    while (x >= start1 && y >= start2 && left[x] == right[y]) {
                        vUp[i] = x--
                        y--
                    }
                    // Second step
                    if (delta % 2 == 0 && -d <= k && k <= d) {
                        if (vUp[i] <= vDown[i + delta]) { // NOPMD
                            return buildSnake(vUp[i], k + start1 - start2, end1, end2)
                        }
                    }
                    k += 2
                }
            }
        }

        // this should not happen
        throw IllegalStateException("Internal Error")
    }

    val script: EditScript<AnnotatedString>
        get() {
            val script: EditScript<AnnotatedString> = EditScript()
            buildScript(0, left.length, 0, right.length, script)
            return script
        }
}
