package dev.bnorm.librettist.show

import androidx.compose.runtime.Composable

@DslMarker
annotation class ShowBuilderDsl

@ShowBuilderDsl
interface ShowBuilder {
    @ShowBuilderDsl
    fun slide(advancements: Int = 1, content: SlideContent)
}

@ShowBuilderDsl
fun <T> ShowBuilder.slide(values: List<T>, content: @Composable SlideScope.(value: T) -> Unit) {
    slide(advancements = values.size) {
        content(values[transition.targetState])
    }
}

@ShowBuilderDsl
inline fun <reified E : Enum<E>> ShowBuilder.slide(crossinline content: @Composable SlideScope.(value: E) -> Unit) {
    val values = enumValues<E>()
    slide(advancements = values.size) {
        content(values[transition.targetState])
    }
}
