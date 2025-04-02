package dev.bnorm.storyboard.text.magic

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import kotlin.jvm.JvmName

const val DefaultMoveDurationMillis = 300
const val DefaultFadeDurationMillis = 300
const val DefaultDelayDurationMillis = 0

@Composable
fun MagicText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    // Used to time FadeOut --(Delay)-> Move --(Delay)-> FadeIn animations.
    moveDurationMillis: Int = DefaultMoveDurationMillis,
    fadeDurationMillis: Int = DefaultFadeDurationMillis,
    delayDurationMillis: Int = DefaultDelayDurationMillis,
) {
    val words = remember(text) { text.toWords() }
    val transition = updateTransition(words)
    MagicText(transition, modifier, moveDurationMillis, fadeDurationMillis, delayDurationMillis)
}

@Composable
@JvmName("MagicTextAnnotatedString")
fun MagicText(
    transition: Transition<AnnotatedString>,
    modifier: Modifier = Modifier,
    // Used to time FadeOut --(Delay)-> Move --(Delay)-> FadeIn animations.
    moveDurationMillis: Int = DefaultMoveDurationMillis,
    fadeDurationMillis: Int = DefaultFadeDurationMillis,
    delayDurationMillis: Int = DefaultDelayDurationMillis,
) {
    val worlds = transition.createChildTransition { remember(it) { it.toWords() } }
    MagicText(worlds, modifier, moveDurationMillis, fadeDurationMillis, delayDurationMillis)
}

@Composable
fun MagicText(
    tokenizedText: List<AnnotatedString>,
    modifier: Modifier = Modifier,
    // Used to time FadeOut --(Delay)-> Move --(Delay)-> FadeIn animations.
    moveDurationMillis: Int = DefaultMoveDurationMillis,
    fadeDurationMillis: Int = DefaultFadeDurationMillis,
    delayDurationMillis: Int = DefaultDelayDurationMillis,
) {
    val transition = updateTransition(tokenizedText)
    MagicText(transition, modifier, moveDurationMillis, fadeDurationMillis, delayDurationMillis)
}

@Composable
@JvmName("MagicTextList")
fun MagicText(
    transition: Transition<List<AnnotatedString>>,
    modifier: Modifier = Modifier,
    // Used to time FadeOut --(Delay)-> Move --(Delay)-> FadeIn animations.
    moveDurationMillis: Int = DefaultMoveDurationMillis,
    fadeDurationMillis: Int = DefaultFadeDurationMillis,
    delayDurationMillis: Int = DefaultDelayDurationMillis,
) {
    // Keyed on current and target state, so a new transition is created with each new segment.
    // This allows re-rendering of the previous text with the new transition keys.
    val currentState = transition.currentState
    val targetState = transition.targetState
    key(currentState, targetState) { // TODO does this need to be keyed on currentState?

        // TODO instead of Map<*, *>, could this be a special data structure?
        // TODO should we be caching these maps for repeated transitions?
        val sharedText = remember {
            when (currentState == targetState) {
                true -> mapOf(currentState to currentState.map { SharedText(it) }.flatMap { toLines(it) })

                false -> {
                    val (current, target) = findShared(currentState, targetState)
                    mapOf(
                        currentState to current.flatMap { toLines(it) }, // Re-render the previous text, split up based on diff with the next text.
                        targetState to target.flatMap { toLines(it) }, // Render the next text, split up based on diff with the previous text.
                    )
                }
            }
        }

        val child = transition.createChildTransition { text -> sharedText.getValue(text) }
        MagicTextInternal(child, modifier, fadeDurationMillis, moveDurationMillis, delayDurationMillis)
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun MagicTextInternal(
    transition: Transition<List<SharedText>>,
    modifier: Modifier,
    fadeDuration: Int, // Millis
    moveDuration: Int, // Millis
    delayDuration: Int, // Millis
) {
    val moveDelay = delayDuration + fadeDuration
    val fadeInDelay = 2 * delayDuration + fadeDuration + moveDuration

    SharedTransitionLayout {
        transition.AnimatedContent(
            modifier = modifier.wrapContentSize(align = Alignment.TopStart, unbounded = true),
            transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
        ) { parts ->

            @Composable
            fun SharedText.toModifier(): Modifier {
                return when {
                    // Text is completely different and should fade in and out.
                    key == null -> Modifier.animateEnterExit(
                        enter = fadeIn(tween(fadeDuration, delayMillis = fadeInDelay, easing = EaseInCubic)),
                        exit = fadeOut(tween(fadeDuration, easing = EaseOutCubic)),
                    )

                    // Text content is the same, but the styling may be different,
                    // so move and cross-fade.
                    crossFade -> Modifier.sharedBounds(
                        rememberSharedContentState(key),
                        animatedVisibilityScope = this@AnimatedContent,
                        enter = fadeIn(tween(moveDuration, delayMillis = moveDelay, easing = EaseInOut)),
                        exit = fadeOut(tween(moveDuration, delayMillis = moveDelay, easing = EaseInOut)),
                        boundsTransform = { _, _ ->
                            tween(moveDuration, delayMillis = moveDelay, easing = EaseInOut)
                        },
                    )

                    // Text and styling are the same, so only move.
                    else -> Modifier.sharedElement(
                        rememberSharedContentState(key),
                        animatedVisibilityScope = this@AnimatedContent,
                        boundsTransform = { _, _ ->
                            tween(moveDuration, delayMillis = moveDelay, easing = EaseInOut)
                        },
                    )
                }
            }

            Column(horizontalAlignment = Alignment.Start) {
                val iterator = parts.iterator()
                while (iterator.hasNext()) {
                    Row {
                        var itemCount = 0
                        while (iterator.hasNext()) {
                            val sharedText = iterator.next().takeIf { it.value.text != "\n" }
                            if (sharedText != null) {
                                Text(sharedText.value, sharedText.toModifier().alignByBaseline())
                                itemCount++
                            } else {
                                if (itemCount == 0) Text("") // Need something in the row...
                                break
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun toLines(text: SharedText): List<SharedText> = buildList {
    return buildList {
        val string = text.value

        var subKeyCount = 0
        fun nextKey(): String? = text.key?.let { "$it-${subKeyCount++}" }

        var offset = 0
        while (true) {
            val index = string.indexOf('\n', offset)
            if (index == -1) break

            if (index > offset) add(SharedText(string.subSequence(offset, index), nextKey(), text.crossFade))
            add(SharedText(AnnotatedString("\n")))
            offset = index + 1
        }
        if (offset < string.length) {
            add(SharedText(string.subSequence(offset, string.length), nextKey(), text.crossFade))
        }
    }
}
