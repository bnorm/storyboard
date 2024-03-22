package dev.bnorm.librettist.show

@DslMarker
annotation class ShowBuilderDsl

@ShowBuilderDsl
interface ShowBuilder {
    @ShowBuilderDsl
    fun slide(advancements: Int = 1, content: SlideContent<Int>)
}

@ShowBuilderDsl
fun <T> ShowBuilder.slideForValues(values: List<T>, content: SlideContent<T>) {
    slide(advancements = values.size) {
        createChildScope { values[it.coerceIn(values.indices)] }.content()
    }
}

@ShowBuilderDsl
inline fun <reified E : Enum<E>> ShowBuilder.slideForEnum(crossinline content: SlideContent<E>) {
    val values = enumValues<E>()
    slide(advancements = values.size) {
        createChildScope { values[it.coerceIn(values.indices)] }.content()
    }
}

@ShowBuilderDsl
fun ShowBuilder.slideForBoolean(content: SlideContent<Boolean>) {
    slide(advancements = 2) {
        createChildScope { it >= 1 }.content()
    }
}
