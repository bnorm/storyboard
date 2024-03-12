package dev.bnorm.librettist.show

import androidx.compose.runtime.*

fun ShowState(builder: ShowBuilder.() -> Unit): ShowState {
    val slides = buildList {
        object : ShowBuilder {
            override fun slide(content: SlideContent) {
                add(content)
            }
        }.builder()
    }

    return ShowState(slides)
}

class ShowState(
    val slides: List<SlideContent>,
    initialDirection: Advancement.Direction = Advancement.Direction.Forward,
) : SlideScope {
    private val mutableIndex = mutableIntStateOf(0)

    var index: Int
        get() = mutableIndex.value
        // TODO instead of a setter, should this be a jumpToSlide() function instead?
        set(value) {
            require(index in 0..slides.size)
            direction = Advancement.Direction.Forward // Jumping to a slide is always considered a forward advancement.
            mutableIndex.value = value
        }

    private val handlers = mutableListOf<AdvancementHandler>()
    private val listeners = mutableListOf<AdvancementListener>() // TODO shared flow?

    override var direction = initialDirection
        private set

    fun advance(advancement: Advancement) {
        direction = advancement.direction

        fun advanceSlideIndex(): Boolean {
            val value = mutableIndex.value
            val nextValue = advancement.direction.toValue(forward = value + 1, backward = value - 1)

            val inRange = nextValue in slides.indices
            if (inRange) mutableIndex.value = nextValue
            return inRange
        }

        /*
         * We need to call handlers in different directions based on advancement direction. When advancing forward,
         * handlers should be called in natural order. When advancing backwards, handlers should be called in reverse
         * order. This is so advancement handling happens in LIFO order. When something is the last to advance forward,
         * it needs to be the first to advance backwards. This is all so multiple advancement handlers defined within
         * the same Composable function are called in the expected order when advancing, regardless of direction.
         *
         * Advancing though the slide index is always the last "handler", since it is outside the slide Composable
         * function.
         */
        when (advancement.direction) {
            Advancement.Direction.Forward -> handlers.any { it(advancement) }
            Advancement.Direction.Backward -> handlers.reversed().any { it(advancement) }
        } || advanceSlideIndex()

        listeners.forEach { it(advancement) }
    }

    fun addAdvancementHandler(handler: AdvancementHandler) {
        handlers.add(handler)
    }

    fun removeAdvancementHandler(handler: AdvancementHandler) {
        handlers.remove(handler)
    }

    fun addAdvancementListener(listener: AdvancementListener) {
        listeners.add(listener)
    }

    fun removeAdvancementListener(listener: AdvancementListener) {
        listeners.remove(listener)
    }
}

val LocalShowState = compositionLocalOf<ShowState> {
    error("LocalShowState is not provided")
}

typealias AdvancementHandler = (Advancement) -> Boolean
typealias AdvancementListener = (Advancement) -> Unit

@Composable
fun HandleAdvancement(handler: AdvancementHandler) {
    val handlerState = rememberUpdatedState(handler)
    val state = LocalShowState.current
    DisposableEffect(handlerState) {
        val localHandler: AdvancementHandler = {
            handlerState.value(it)
        }
        state.addAdvancementHandler(localHandler)
        onDispose {
            state.removeAdvancementHandler(localHandler)
        }
    }
}
