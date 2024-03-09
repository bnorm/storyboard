package dev.bnorm.librettist.show

@DslMarker
annotation class ShowBuilderDsl

@ShowBuilderDsl
interface ShowBuilder {
    @ShowBuilderDsl
    fun slide(content: SlideContent)
}
