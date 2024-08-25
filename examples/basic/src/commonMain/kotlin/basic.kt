import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import dev.bnorm.storyboard.core.SlideDecorator
import dev.bnorm.storyboard.core.Storyboard

val BASIC_STORYBOARD by lazy {
    Storyboard.build(
        name = "Basic Storyboard",
        size = Storyboard.DEFAULT_SIZE,
        decorator = theme,
    ) {
        NavigationSlide()
        StateSlide()
        AnimationSlide()
        CodeSlide()
    }
}

private val theme = SlideDecorator { content ->
    val colors = darkColors(
        background = Color.Black,
        surface = Color(0xFF1E1F22),
        onBackground = Color(0xFFBCBEC4),
        primary = Color(0xFF7F51FF),
        primaryVariant = Color(0xFF7E53FE),
        secondary = Color(0xFFFDB60D),
    )

    val typography = Typography().run {
        copy(
            h1 = h3.copy(fontWeight = FontWeight.Light),
            h2 = h4.copy(fontWeight = FontWeight.Light),
            h3 = h5.copy(fontWeight = FontWeight.Normal),
            h4 = h6.copy(fontWeight = FontWeight.Normal),
            h5 = h6.copy(fontWeight = FontWeight.Normal),
        )
    }

    MaterialTheme(colors, typography) {
        content()
    }
}
