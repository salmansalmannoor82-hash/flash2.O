package com.flash2.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object Neon {
    val Bg = Color(0xFF050B1A)
    val Surface = Color(0xFF0B1630)
    val Cyan = Color(0xFF00E5FF)
    val Purple = Color(0xFFB026FF)
    val Pink = Color(0xFFFF2BD6)
}

private val scheme = darkColorScheme(
    primary = Neon.Cyan,
    secondary = Neon.Purple,
    background = Neon.Bg,
    surface = Neon.Surface,
    onPrimary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun FlashTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = scheme, content = content)
}
