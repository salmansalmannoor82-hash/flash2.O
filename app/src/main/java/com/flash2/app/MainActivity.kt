package com.flash2.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.flash2.app.assistant.AssistantController
import com.flash2.app.ui.chat.ChatScreen
import com.flash2.app.ui.splash.SplashScreen
import com.flash2.app.ui.theme.FlashTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashTheme {
                val controller = remember { AssistantController(applicationContext) }
                DisposableEffect(Unit) { onDispose { controller.shutdown() } }

                var showSplash by remember { mutableStateOf(true) }
                if (showSplash) {
                    SplashScreen { showSplash = false }
                } else {
                    ChatScreen(controller)
                }
            }
        }
    }
}
