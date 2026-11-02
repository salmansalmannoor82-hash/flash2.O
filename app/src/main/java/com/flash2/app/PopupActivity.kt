package com.flash2.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flash2.app.assistant.AssistantController
import com.flash2.app.ui.theme.FlashTheme
import com.flash2.app.ui.theme.Neon
import kotlinx.coroutines.delay

class PopupActivity : ComponentActivity() {

    private var controller: AssistantController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val c = AssistantController(applicationContext)
        controller = c
        c.onFinished = { finish() }

        val micOk = checkSelfPermission(Manifest.permission.RECORD_AUDIO) ==
            PackageManager.PERMISSION_GRANTED
        if (micOk) {
            c.toggleMic()
        } else {
            c.say("Pehle Flash 2.0 app kholo aur mic ki permission do.", speak = false)
        }

        setContent {
            FlashTheme { PopupCard(c) { finish() } }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        controller?.let { if (!it.listening) it.toggleMic() }
    }

    override fun onDestroy() {
        controller?.shutdown()
        super.onDestroy()
    }
}

@Composable
fun PopupCard(controller: AssistantController, onClose: () -> Unit) {
    val last = controller.messages.lastOrNull()
    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 0.88f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "pulseValue"
    )

    // 15 second kuch na ho to popup khud band
    LaunchedEffect(controller.messages.size, controller.listening) {
        delay(15000)
        onClose()
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Neon.Surface,
            border = BorderStroke(1.dp, Neon.Cyan)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Flash 2.0",
                        color = Neon.Cyan,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "✕",
                        color = Color.White,
                        fontSize = 22.sp,
                        modifier = Modifier.clickable { onClose() }.padding(4.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .scale(if (controller.listening) pulse else 1f)
                        .clip(CircleShape)
                        .background(if (controller.listening) Neon.Pink else Neon.Cyan)
                        .clickable { controller.toggleMic() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (controller.listening) "⏹" else "🎤", fontSize = 28.sp)
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = if (controller.listening) "Sun rahi hoon..." else (last?.text ?: ""),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
