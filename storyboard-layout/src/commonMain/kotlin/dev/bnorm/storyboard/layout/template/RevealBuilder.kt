package dev.bnorm.storyboard.layout.template

import androidx.compose.animation.*
import androidx.compose.animation.core.Transition
import androidx.compose.runtime.*

interface RevealBuilder {
    /**
     * Items are applied in call order, but are revealed based on their [index],
     * which defaults to the item count.
     */
    fun item(
        index: Int? = null,
        enterTransition: () -> EnterTransition = { fadeIn() },
        exitTransition: () -> ExitTransition = { fadeOut() },
        content: @Composable AnimatedVisibilityScope.() -> Unit,
    )
}

@Composable
fun RevealEach(
    transition: Transition<Int>,
    content: @DisallowComposableCalls RevealBuilder.() -> Unit,
) {
    val latestContent = rememberUpdatedState(content)
    val items by remember {
        derivedStateOf(referentialEqualityPolicy()) {
            RevealBuilderImpl().apply(latestContent.value).build()
        }
    }

    for (item in items) {
        item(transition)
    }
}

private class RevealBuilderImpl : RevealBuilder {
    private val sink: MutableList<@Composable Transition<Int>.() -> Unit> = mutableListOf()
    private var count = 0

    override fun item(
        index: Int?,
        enterTransition: () -> EnterTransition,
        exitTransition: () -> ExitTransition,
        content: @Composable AnimatedVisibilityScope.() -> Unit,
    ) {
        val itemIndex = index ?: count
        count++

        sink.add(movableContentWithReceiverOf<Transition<Int>> {
            AnimatedVisibility(
                visible = { it >= itemIndex },
                enter = enterTransition(),
                exit = exitTransition(),
                content = content,
            )
        })
    }

    fun build(): List<@Composable Transition<Int>.() -> Unit> {
        return sink.toList().also {
            sink.clear()
            count = 0
        }
    }
}
