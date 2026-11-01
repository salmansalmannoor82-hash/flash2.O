package com.flash2.app.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flash2.app.ui.theme.Neon
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val flicker = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        val steps = listOf(0.9f, 0.1f, 1f, 0.3f, 1f, 0.6f, 1f)
        for (v in steps) {
            flicker.animateTo(v, tween(90))
        }
        delay(1000)
        onFinished()
    }

    val glow by rememberInfiniteTransition(label = "glow").animateFloat(
        initialValue = 12f,
        targetValue = 44f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "glowValue"
    )

    Box(
        modifier = Modifier.fillMaxSize().background(Neon.Bg),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "FLASH 2.0",
                modifier = Modifier.alpha(flicker.value),
                style = TextStyle(
                    color = Neon.Cyan,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp,
                    shadow = Shadow(
                        color = Neon.Cyan,
                        offset = Offset.Zero,
                        blurRadius = glow
                    )
                )
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Your Smart Personal Assistant",
                modifier = Modifier.alpha(flicker.value),
                color = Color(0xFFB9C4E0),
                fontSize = 14.sp
            )
        }
    }
}
