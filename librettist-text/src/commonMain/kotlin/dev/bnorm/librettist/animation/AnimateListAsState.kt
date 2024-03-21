package dev.bnorm.librettist.animation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf

@Composable
fun <T> animateListAsState(
    targetIndex: Int,
    values: List<T>,
    animationSpec: AnimationSpec<Int> = tween(
        durationMillis = 1_000,
        easing = LinearEasing
    ),
    label: String = "ListAnimation",
    finishedListener: ((T) -> Unit)? = null,
): State<T> {
    val index = animateIntAsState(
        targetValue = targetIndex,
        animationSpec = animationSpec,
        label = label,
        finishedListener = finishedListener?.let { listener -> { listener(values[it]) } }
    )
    // TODO is this the best way to map a state object?
    return derivedStateOf { values[index.value] }
}
