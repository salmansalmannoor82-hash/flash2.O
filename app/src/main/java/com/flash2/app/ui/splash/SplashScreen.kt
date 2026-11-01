package com.flash2.app.ui.splash

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.flash2.app.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val finished = remember { booleanArrayOf(false) }
    val finish = {
        if (!finished[0]) {
            finished[0] = true
            onFinished()
        }
    }

    // Video na chale to bhi 8 second baad app aage badh jaye
    LaunchedEffect(Unit) {
        delay(8000)
        finish()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF191E28))
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                VideoView(ctx).apply {
                    setVideoURI(Uri.parse("android.resource://${ctx.packageName}/${R.raw.splash}"))
                    setOnCompletionListener { finish() }
                    setOnErrorListener { _, _, _ -> finish(); true }
                    start()
                }
            }
        )
    }
}
