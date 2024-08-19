package dev.bnorm.librettist.animation

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import kotlinx.collections.immutable.ImmutableList

@Composable
fun <T> animateListAsState(
    targetIndex: Int,
    values: ImmutableList<T>,
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

@Composable
inline fun <S, T> Transition<S>.animateList(
    values: ImmutableList<T>,
    noinline transitionSpec: @Composable Transition.Segment<S>.() -> FiniteAnimationSpec<Int> = {
        tween(
            durationMillis = 1_000,
            easing = LinearEasing
        )
    },
    label: String = "ListAnimation",
    targetIndexByState: @Composable (state: S) -> Int,
): State<T> {
    val index = animateInt(
        transitionSpec = transitionSpec,
        label = label,
        targetValueByState = targetIndexByState,
    )
    // TODO is this the best way to map a state object?
    return derivedStateOf { values[index.value] }
}
