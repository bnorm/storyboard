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

    init {
        // Add default handler for slide index
        fun defaultHandler(it: Advancement): Boolean {
            val value = mutableIndex.value
            val nextValue = it.direction.toValue(forward = value + 1, backward = value - 1)

            val inRange = nextValue in slides.indices
            if (inRange) mutableIndex.value = nextValue
            return inRange
        }

        handlers.add(::defaultHandler)
    }

    override var direction = initialDirection
        private set

    fun advance(advancement: Advancement) {
        direction = advancement.direction
        handlers.reversed().any { it(advancement) }
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
