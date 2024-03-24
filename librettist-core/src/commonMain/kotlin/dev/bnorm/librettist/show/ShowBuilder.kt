package dev.bnorm.librettist.show

@DslMarker
annotation class ShowBuilderDsl

@ShowBuilderDsl
interface ShowBuilder {
    @ShowBuilderDsl
    fun slide(advancements: Int = 1, content: SlideContent<SlideState<Int>>)
}

@ShowBuilderDsl
fun <T> ShowBuilder.slideForValues(values: List<T>, content: SlideContent<SlideState<T>>) {
    slide(advancements = values.size) {
        createChildScope { state -> state.map { values[it.coerceIn(values.indices)] } }.content()
    }
}

@ShowBuilderDsl
inline fun <reified E : Enum<E>> ShowBuilder.slideForEnum(crossinline content: SlideContent<SlideState<E>>) {
    val values = enumValues<E>()
    slide(advancements = values.size) {
        createChildScope { state -> state.map { values[it.coerceIn(values.indices)] } }.content()
    }
}

@ShowBuilderDsl
fun ShowBuilder.slideForBoolean(content: SlideContent<SlideState<Boolean>>) {
    slide(advancements = 2) {
        createChildScope { state -> state.map { it > 0 } }.content()
    }
}
