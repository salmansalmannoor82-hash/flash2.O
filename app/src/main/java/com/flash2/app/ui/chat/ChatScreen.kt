package com.flash2.app.ui.chat

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flash2.app.ui.theme.Neon
import kotlinx.coroutines.launch

data class ChatMessage(val text: String, val fromUser: Boolean)

@Composable
fun ChatScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Salaam! Main aapka assistant hoon. Kuch poochiye.", false)
        )
    }
    var input by remember { mutableStateOf("") }
    var advanced by remember { mutableStateOf(false) }
    var femaleVoice by remember { mutableStateOf(true) }

    val t by rememberInfiniteTransition(label = "title").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2500), RepeatMode.Reverse),
        label = "titleValue"
    )
    val titleColor = lerp(Neon.Cyan, Neon.Pink, t)

    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.lastIndex)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Neon.Surface) {
                Column(Modifier.padding(20.dp)) {
                    Text("Settings", color = Neon.Cyan, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Advanced mode", color = Color.White, modifier = Modifier.weight(1f))
                        Switch(checked = advanced, onCheckedChange = { advanced = it })
                    }
                    Text(
                        text = if (advanced) "Advanced: poora phone control" else "Simple: safe mode",
                        color = Color(0xFF9FB0D0),
                        fontSize = 12.sp
                    )

                    Spacer(Modifier.height(24.dp))
                    Text("Awaaz", color = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Row {
                        if (femaleVoice) {
                            Button(onClick = { femaleVoice = true }) { Text("Female") }
                            Spacer(Modifier.width(8.dp))
                            OutlinedButton(onClick = { femaleVoice = false }) { Text("Male") }
                        } else {
                            OutlinedButton(onClick = { femaleVoice = true }) { Text("Female") }
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = { femaleVoice = false }) { Text("Male") }
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(containerColor = Neon.Bg) { innerPadding ->
            Column(Modifier.padding(innerPadding).fillMaxSize()) {

                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "☰",
                        color = Color.White,
                        fontSize = 26.sp,
                        modifier = Modifier.clickable { scope.launch { drawerState.open() } }
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = "Flash 2.0",
                        color = titleColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(messages) { m -> MessageBubble(m) }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = input,
                        onValueChange = { input = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Yahan likho...") },
                        maxLines = 3
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val text = input.trim()
                            if (text.isEmpty()) {
                                messages.add(ChatMessage("Mic jald aa raha hai (voice wale part mein).", false))
                            } else {
                                messages.add(ChatMessage(text, true))
                                messages.add(ChatMessage("AI abhi connect nahi hai, jald hoga.", false))
                                input = ""
                            }
                        },
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(52.dp)
                    ) {
                        Text(if (input.isBlank()) "🎤" else "➤", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(m: ChatMessage) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (m.fromUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (m.fromUser) Neon.Purple.copy(alpha = 0.35f) else Neon.Surface,
            border = BorderStroke(1.dp, if (m.fromUser) Neon.Purple else Neon.Cyan)
        ) {
            Text(m.text, modifier = Modifier.padding(12.dp), color = Color.White)
        }
    }
}
