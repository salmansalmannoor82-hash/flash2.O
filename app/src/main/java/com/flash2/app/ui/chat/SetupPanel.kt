package com.flash2.app.ui.chat

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flash2.app.ui.theme.Neon

@Composable
fun SetupPanel() {
    val context = LocalContext.current
    Column {
        Text("Har app mein Noor", color = Neon.Cyan, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Volume button 2 baar dabane par Noor kisi bhi app ke upar khulegi. Ek baar Accessibility mein Flash 2.0 on karo.",
            color = Color(0xFF9FB0D0),
            fontSize = 12.sp
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }) { Text("Accessibility kholo") }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = {
            val i = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + context.packageName)
            )
            context.startActivity(i)
        }) { Text("App info (battery)") }
        Spacer(Modifier.height(24.dp))
    }
}
