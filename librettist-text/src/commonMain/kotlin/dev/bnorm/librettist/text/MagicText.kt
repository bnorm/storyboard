package dev.bnorm.librettist.text

import androidx.compose.animation.*
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.SeekableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun MagicText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    // Used to time FadeOut -> Move -> FadeIn animations.
    moveDurationMillis: Int = DefaultDurationMillis,
    fadeDurationMillis: Int = moveDurationMillis / 2,
    // Can be used to provide a custom diff equation.
    diff: (before: AnnotatedString, after: AnnotatedString) -> List<MagicTextDiff> = { before, after ->
        diff(before.toWords(), after.toWords())
    },
) {
    val textFlow = remember { MutableStateFlow(text) }
    textFlow.value = text

    val initialValue = remember { listOf<SharedText?>(SharedText(text)) } // null == newline
    val transitionState = remember { SeekableTransitionState(initialValue) }
    LaunchedEffect(Unit) {
        var previous: AnnotatedString? = null
        textFlow.collect { next ->
            previous?.also { previous ->
                val result = diff(previous, next)
                checkNoRepeatedKeys(result)
                transitionState.snapTo(result.toLines(after = false)) // Re-render the previous text, split up based on diff with the next text.
                transitionState.animateTo(result.toLines(after = true)) // Render the next text, split up based on diff with the previous text.
            }
            previous = next
        }
    }

    val transition = rememberTransition(transitionState)
    MagicTextInternal(transition, modifier, fadeDurationMillis, moveDurationMillis)
}

private data class SharedText(
    val text: AnnotatedString,
    val crossFade: Boolean = false,
    val key: String? = null,
)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun MagicTextInternal(
    transition: Transition<List<SharedText?>>,
    modifier: Modifier,
    fadeDuration: Int,
    moveDuration: Int,
) {
    SharedTransitionLayout {
        transition.AnimatedContent(
            transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
        ) { parts ->

            @Composable
            fun SharedText.toModifier(): Modifier {
                return when {
                    // Text is completely different and should fade in and out.
                    key == null -> Modifier.animateEnterExit(
                        enter = fadeIn(tween(fadeDuration, delayMillis = moveDuration + fadeDuration)),
                        exit = fadeOut(tween(fadeDuration)),
                    )

                    // Text content is the same, but the styling may be different,
                    // so move and cross-fade.
                    crossFade -> Modifier.sharedBounds(
                        rememberSharedContentState(key),
                        animatedVisibilityScope = this@AnimatedContent,
                        enter = fadeIn(tween(moveDuration, delayMillis = fadeDuration)),
                        exit = fadeOut(tween(moveDuration, delayMillis = fadeDuration)),
                        boundsTransform = { _, _ ->
                            tween(moveDuration, delayMillis = fadeDuration)
                        },
                    )

                    // Text and styling are the same, so only move.
                    else -> Modifier.sharedElement(
                        rememberSharedContentState(key),
                        animatedVisibilityScope = this@AnimatedContent,
                        boundsTransform = { _, _ ->
                            tween(moveDuration, delayMillis = fadeDuration)
                        },
                    )
                }
            }

            Column(modifier) {
                val iterator = parts.iterator()
                while (iterator.hasNext()) {
                    Row {
                        while (iterator.hasNext()) {
                            val sharedText = iterator.next() ?: break
                            Text(sharedText.text, sharedText.toModifier())
                        }
                    }
                }
            }
        }
    }
}

private fun checkNoRepeatedKeys(result: List<MagicTextDiff>) {
    val uniqueKeys = mutableSetOf<Any>()
    val repeatedKeys = mutableSetOf<Any>()
    for (it in result) {
        if (it.key != null && !uniqueKeys.add(it.key)) repeatedKeys.add(it.key)
    }
    require(repeatedKeys.isEmpty()) { "Repeated keys: $repeatedKeys" }
}

private fun List<MagicTextDiff>.toLines(after: Boolean): List<SharedText?> {
    return buildList {
        var subKey = 1
        for (part in this@toLines) {
            val text = if (after) part.after else part.before
            for ((i, split) in text.split('\n').withIndex()) {
                if (i > 0) add(null)
                add(SharedText(split, part.before != part.after, part.key?.let { "$it-${subKey++}" }))
            }
        }
    }
}

private fun AnnotatedString.split(char: Char): List<AnnotatedString> {
    return buildList {
        var offset = 0
        while (true) {
            val index = indexOf(char, offset)
            if (index == -1) break

            add(subSequence(offset, index))
            offset = index + 1
        }
        add(subSequence(offset, length))
    }
}
