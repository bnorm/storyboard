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
 * Placement of [quarter2] is determined by the measured height of [quarter1]:
 * if the measured height is shorter than the maximum allowed height,
 * the quarters are placed vertically, otherwise they are placed horizontally.
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

        val quarterBoxHeight = constraints.maxHeight / 2
        val quarterBoxWidth = constraints.maxWidth / 2
        val quarterBoxConstraints = Constraints(
            minWidth = 0, maxWidth = quarterBoxWidth,
            minHeight = 0, maxHeight = quarterBoxHeight,
        )

        val quarter1Placeable = quarter1Measurable.measure(quarterBoxConstraints)
        val quarter2Placeable = quarter2Measurable.measure(quarterBoxConstraints)

        val vertical = maxOf(quarter1Placeable.height, quarter2Placeable.height) < quarterBoxConstraints.maxHeight
        val remainingConstraints = when {
            vertical -> Constraints.fixed(
                width = constraints.maxWidth,
                height = constraints.maxHeight - quarter1Placeable.height,
            )

            else -> Constraints.fixed(
                width = constraints.maxWidth - quarter1Placeable.width,
                height = constraints.maxHeight,
            )
        }

        val remainingMeasurable = measurables[2]
        val remainingPlaceable = remainingMeasurable.measure(remainingConstraints)

        layout(constraints.maxWidth, constraints.maxHeight) {
            quarter1Placeable.placeRelative(0, 0)
            if (vertical) {
                quarter2Placeable.placeRelative(quarter1Placeable.width, 0)
                remainingPlaceable.placeRelative(0, quarter1Placeable.height)
            } else {
                quarter2Placeable.placeRelative(0, quarter1Placeable.height)
                remainingPlaceable.placeRelative(quarter1Placeable.width, 0)
            }
        }
    }
}
