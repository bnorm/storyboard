package dev.bnorm.storyboard

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import kotlin.jvm.JvmField

public fun interface SceneEnterTransition : (AdvanceDirection) -> EnterTransition {
    public companion object {
        @JvmField
        public val None: SceneEnterTransition = SceneEnterTransition { EnterTransition.None }
    }
}

public fun interface SceneExitTransition : (AdvanceDirection) -> ExitTransition {
    public companion object {
        @JvmField
        public val None: SceneExitTransition = SceneExitTransition { ExitTransition.None }
    }
}
