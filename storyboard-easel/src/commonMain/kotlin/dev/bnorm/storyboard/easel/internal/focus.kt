package dev.bnorm.storyboard.easel.internal

import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*

@Composable
internal fun Modifier.requestFocus(): Modifier {
    val focusRequester = remember { FocusRequester() }
    var focusState by remember { mutableStateOf<FocusState?>(null) }

    if (focusState?.hasFocus != true) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }

    return this
        .clickable(interactionSource = null, indication = null) { focusRequester.requestFocus() }
        .focusRequester(focusRequester)
        .onFocusChanged { focusState = it }
        .focusTarget()
}