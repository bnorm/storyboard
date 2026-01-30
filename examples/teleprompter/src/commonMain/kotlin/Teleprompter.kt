

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

val isTeleprompter: Boolean
    @Composable
    @ReadOnlyComposable
    get() = LocalTeleprompter.current

internal val LocalTeleprompter = compositionLocalOf { false }
