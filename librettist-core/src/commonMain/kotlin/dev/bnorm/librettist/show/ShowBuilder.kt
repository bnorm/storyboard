package dev.bnorm.librettist.show

@DslMarker
annotation class ShowBuilderDsl

@ShowBuilderDsl
interface ShowBuilder {
    @ShowBuilderDsl
    fun slide(advancements: Int = 1, content: SlideContent)
}
