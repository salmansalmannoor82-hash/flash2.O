package com.flash2.app.assistant

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.flash2.app.commands.CommandRouter
import com.flash2.app.commands.defaultCommands
import com.flash2.app.voice.VoiceInput
import com.flash2.app.voice.VoiceOutput

data class ChatMessage(val text: String, val fromUser: Boolean)

class AssistantController(context: Context) {
    private val appContext = context.applicationContext
    private val router = CommandRouter(defaultCommands())
    private val speaker = VoiceOutput(appContext)
    private val handler = Handler(Looper.getMainLooper())

    var onFinished: (() -> Unit)? = null

    private val stopPhrases = listOf(
        "bas kar", "bas karo", "band kar", "band karo", "stop", "chup",
        "बस कर", "बस करो", "बंद कर", "बंद करो", "चुप"
    )

    val messages = mutableStateListOf(
        ChatMessage("Salaam! Mic dabao aur bolo, jaise \"camera kholo\".", false)
    )

    var listening by mutableStateOf(false)
        private set
    var advancedMode by mutableStateOf(false)
    var femaleVoice by mutableStateOf(true)
        private set

    private val input = VoiceInput(
        context = appContext,
        onHeard = { handleText(it) },
        onListeningChanged = { listening = it },
        onFailure = { say(it) }
    )

    fun setFemale(value: Boolean) {
        femaleVoice = value
        speaker.female = value
    }

    fun toggleMic() {
        if (listening) {
            input.stop()
        } else {
            speaker.stop()
            input.start()
        }
    }

    fun say(text: String, speak: Boolean = true) {
        messages.add(ChatMessage(text, false))
        if (speak) speaker.speak(text)
    }

    private fun finishSoon(delayMs: Long) {
        handler.postDelayed({ onFinished?.invoke() }, delayMs)
    }

    fun handleText(raw: String) {
        val text = raw.trim()
        if (text.isEmpty()) return
        messages.add(ChatMessage(text, true))
        val t = text.lowercase()

        if (stopPhrases.any { t.contains(it) }) {
            speaker.stop()
            input.stop()
            say("Theek hai, ruk gayi.", speak = false)
            finishSoon(600)
            return
        }

        val command = router.find(t, advancedMode)
        if (command != null) {
            val reply = try {
                command.run(appContext)
            } catch (e: Exception) {
                "Ye command is phone par nahi chal saka."
            }
            say(reply)
            finishSoon(1800)
        } else {
            say("AI abhi connect nahi hai, wo agle hisse mein aayega.")
        }
    }

    fun shutdown() {
        handler.removeCallbacksAndMessages(null)
        speaker.shutdown()
        input.destroy()
    }
}
