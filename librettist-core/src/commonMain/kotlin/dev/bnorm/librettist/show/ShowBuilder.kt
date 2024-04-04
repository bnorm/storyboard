package dev.bnorm.librettist.show

import androidx.compose.runtime.Composable

@DslMarker
annotation class ShowBuilderDsl

typealias SlideContent<T> = @Composable SlideScope<T>.() -> Unit

@ShowBuilderDsl
interface ShowBuilder {
    @ShowBuilderDsl
    fun slide(states: Int = 1, content: SlideContent<SlideState<Int>>)
}

@ShowBuilderDsl
fun <T> ShowBuilder.slideForList(values: List<T>, content: SlideContent<SlideState<T>>) {
    slide(states = values.size) {
        createChildScope { state -> state.map { values[it] } }.content()
    }
}

@ShowBuilderDsl
inline fun <reified E : Enum<E>> ShowBuilder.slideForEnum(crossinline content: SlideContent<SlideState<E>>) {
    val values = enumValues<E>()
    slide(states = values.size) {
        createChildScope { state -> state.map { values[it] } }.content()
    }
}

@ShowBuilderDsl
fun ShowBuilder.slideForBoolean(content: SlideContent<SlideState<Boolean>>) {
    slide(states = 2) {
        createChildScope { state -> state.map { it > 0 } }.content()
    }
}
