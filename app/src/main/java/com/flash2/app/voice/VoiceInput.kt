package com.flash2.app.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class VoiceInput(
    private val context: Context,
    private val onHeard: (String) -> Unit,
    private val onListeningChanged: (Boolean) -> Unit,
    private val onFailure: (String) -> Unit
) {
    private var recognizer: SpeechRecognizer? = null

    fun start() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            onFailure("Is phone mein awaaz pehchan wali service nahi mili.")
            return
        }
        recognizer?.destroy()
        recognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) { onListeningChanged(true) }
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() { onListeningChanged(false) }
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}

                override fun onError(error: Int) {
                    onListeningChanged(false)
                    val msg = when (error) {
                        SpeechRecognizer.ERROR_NO_MATCH,
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Kuch sunai nahi diya, dobara mic dabao."
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Mic ki permission chahiye."
                        SpeechRecognizer.ERROR_NETWORK,
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Awaaz pehchan ke liye internet chahiye."
                        else -> "Awaaz sunne mein masla aaya (code $error)."
                    }
                    onFailure(msg)
                }

                override fun onResults(results: Bundle?) {
                    onListeningChanged(false)
                    val text = results
                        ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        ?.firstOrNull()
                    if (!text.isNullOrBlank()) onHeard(text)
                    else onFailure("Kuch sunai nahi diya, dobara mic dabao.")
                }
            })
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        recognizer?.startListening(intent)
    }

    fun stop() {
        recognizer?.stopListening()
        onListeningChanged(false)
    }

    fun destroy() {
        recognizer?.destroy()
        recognizer = null
    }
}
