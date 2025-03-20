package dev.bnorm.storyboard.text.magic

import androidx.compose.animation.*
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString

@Composable
fun MagicText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    // Used to time FadeOut -> Move -> FadeIn animations.
    moveDurationMillis: Int = DefaultDurationMillis,
    fadeDurationMillis: Int = moveDurationMillis / 2,
) {
    val words = remember(text) { text.toWords() }
    val transition = updateTransition(words)
    MagicText(transition, modifier, moveDurationMillis, fadeDurationMillis)
}

@Composable
fun MagicText(
    tokenizedText: List<AnnotatedString>,
    modifier: Modifier = Modifier,
    // Used to time FadeOut -> Move -> FadeIn animations.
    moveDurationMillis: Int = DefaultDurationMillis,
    fadeDurationMillis: Int = moveDurationMillis / 2,
) {
    val transition = updateTransition(tokenizedText)
    MagicText(transition, modifier, moveDurationMillis, fadeDurationMillis)
}

@Composable
fun MagicText(
    transition: Transition<List<AnnotatedString>>,
    modifier: Modifier = Modifier,
    // Used to time FadeOut -> Move -> FadeIn animations.
    moveDurationMillis: Int = DefaultDurationMillis,
    fadeDurationMillis: Int = moveDurationMillis / 2,
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
                true -> mapOf(currentState to currentState.map { SharedText(it) })

                false -> {
                    val (current, target) = findShared(currentState, targetState)
                    mapOf(
                        currentState to current, // Re-render the previous text, split up based on diff with the next text.
                        targetState to target, // Render the next text, split up based on diff with the previous text.
                    )
                }
            }
        }

        val child = transition.createChildTransition { text -> sharedText.getValue(text) }
        MagicTextInternal(child, modifier, fadeDurationMillis, moveDurationMillis)
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun MagicTextInternal(
    transition: Transition<List<SharedText>>,
    modifier: Modifier,
    fadeDuration: Int,
    moveDuration: Int,
) {
    SharedTransitionLayout {
        transition.AnimatedContent(
            modifier = modifier,
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
