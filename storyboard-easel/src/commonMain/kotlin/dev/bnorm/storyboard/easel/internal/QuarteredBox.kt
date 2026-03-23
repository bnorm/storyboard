package dev.bnorm.storyboard.easel.internal

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints

/**
 * Places [quarter1] in the top-start quarter box,
 * places [quarter2] in either the top-end or bottom-start quarter box,
 * and places [remaining] in the remaining space.
 * Placement of [quarter2] is determined by the measured height of [quarter1] and [quarter2]:
 * if the max measured height is shorter than half the maximum allowed height,
 * the quarters are placed horizontally, otherwise they are placed vertically.
 */
@Composable
internal fun QuarteredBox(
    quarter1: @Composable () -> Unit,
    quarter2: @Composable () -> Unit,
    remaining: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Layout(
        content = {
            Box { quarter1() }
            Box { quarter2() }
            Box { remaining() }
        },
        modifier = modifier,
    ) { measurables, constraints ->
        val quarter1Measurable = measurables[0]
        val quarter2Measurable = measurables[1]
        val remainingMeasurable = measurables[2]

        val quarterBoxHeight = constraints.maxHeight / 2
        val quarterBoxWidth = constraints.maxWidth / 2
        val quarterBoxConstraints = Constraints(
            minWidth = 0, maxWidth = quarterBoxWidth,
            minHeight = 0, maxHeight = quarterBoxHeight,
        )

        val quarter1Placeable = quarter1Measurable.measure(quarterBoxConstraints)
        val quarter2Placeable = quarter2Measurable.measure(quarterBoxConstraints)

        val quarterMaxHeight = maxOf(quarter1Placeable.height, quarter2Placeable.height)
        val quarterMaxWidth = maxOf(quarter1Placeable.width, quarter2Placeable.width)

        val heightUsePercent = quarterMaxHeight.toFloat() / quarterBoxConstraints.maxHeight
        val verticalUsePercent = quarterMaxWidth.toFloat() / quarterBoxConstraints.maxWidth
        when (heightUsePercent <= verticalUsePercent) {
            true -> {
                // If 'remaining' is placed vertically-relative to 'quarter1' and 'quarter2'.
                val remainingPlaceable = remainingMeasurable.measure(
                    Constraints.fixed(
                        width = constraints.maxWidth,
                        height = constraints.maxHeight - quarterMaxHeight,
                    )
                )

                layout(
                    width = maxOf(quarter1Placeable.width + quarter2Placeable.width, remainingPlaceable.width),
                    height = quarterMaxHeight + remainingPlaceable.height
                ) {
                    quarter1Placeable.placeRelative(0, 0)
                    quarter2Placeable.placeRelative(quarter1Placeable.width, 0)
                    remainingPlaceable.placeRelative(0, quarterMaxHeight)
                }
            }

            false -> {
                // If 'remaining' is placed horizontally-relative to 'quarter1' and 'quarter2'.
                val remainingPlaceable = remainingMeasurable.measure(
                    Constraints.fixed(
                        width = constraints.maxWidth - quarterMaxWidth,
                        height = constraints.maxHeight,
                    )
                )

                layout(
                    width = quarterMaxWidth + remainingPlaceable.width,
                    height = maxOf(quarter1Placeable.height + quarter2Placeable.height, remainingPlaceable.height)
                ) {
                    quarter1Placeable.placeRelative(0, 0)
                    quarter2Placeable.placeRelative(0, quarter1Placeable.height)
                    remainingPlaceable.placeRelative(quarterMaxWidth, 0)
                }
            }
        }
    }
}
