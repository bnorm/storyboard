package dev.bnorm.storyboard.text.magic

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import kotlin.concurrent.atomics.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi
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
    val child = transition.createSharedTextTransition()
    MagicTextInternal(child, modifier, fadeDurationMillis, moveDurationMillis, delayDurationMillis)
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

    SharedTransitionLayout(modifier) {
        transition.AnimatedContent(
            transitionSpec = {
                fadeIn(tween(fadeDuration, delayMillis = fadeInDelay, easing = EaseInCubic)) togetherWith
                        fadeOut(tween(fadeDuration, easing = EaseOutCubic)) using
                        SizeTransform(clip = false)
            },
        ) { parts ->

            @Composable
            fun SharedText.toModifier(): Modifier {
                return if (key == null) {
                    Modifier
                } else {
                    Modifier.sharedBounds(
                        rememberSharedContentState(key),
                        animatedVisibilityScope = this@AnimatedContent,
                        enter =
                            if (crossFade) fadeIn(tween(moveDuration, delayMillis = moveDelay, easing = EaseInOut))
                            else EnterTransition.None,
                        exit =
                            if (crossFade) fadeOut(tween(moveDuration, delayMillis = moveDelay, easing = EaseInOut))
                            else ExitTransition.None,
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

@OptIn(ExperimentalAtomicApi::class)
@Composable
private fun Transition<List<AnnotatedString>>.createSharedTextTransition(): Transition<List<SharedText>> {
    // TODO this is still not great...
    //  - switching from element to bounds seems to break everything
    val key: AtomicLong = remember { AtomicLong(0) }
    var previousState by remember { mutableStateOf<List<SharedText>>(emptyList()) }
    val child = createChildTransition("SharedText") { targetState ->
        remember(targetState) { findShared(key, previousState, targetState.flatMap { toLines(it) }) }
    }

    // TODO doing this in remember seems broken...
    remember(child.currentState, child.targetState) {
        // When we reach the target state, update the previous state.
        if (child.currentState == child.targetState) {
            previousState = child.currentState
        }
    }

    return child
}

private fun toLines(string: AnnotatedString): List<AnnotatedString> = buildList {
    return buildList {
        var offset = 0
        while (true) {
            val index = string.indexOf('\n', offset)
            if (index == -1) break

            if (index > offset) add(string.subSequence(offset, index))
            add(AnnotatedString("\n"))
            offset = index + 1
        }
        if (offset < string.length) {
            add(string.subSequence(offset, string.length))
        }
    }
}
