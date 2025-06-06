package dev.bnorm.storyboard

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import kotlin.jvm.JvmField

public fun interface SceneEnterTransition : (AdvanceDirection) -> EnterTransition {
    public companion object {
        @JvmField
        public val Default: SceneEnterTransition = SceneEnterTransition { EnterTransition.Companion.None }
    }
}

public fun interface SceneExitTransition : (AdvanceDirection) -> ExitTransition {
    public companion object {
        @JvmField
        public val Default: SceneExitTransition = SceneExitTransition { ExitTransition.None }
    }
}
