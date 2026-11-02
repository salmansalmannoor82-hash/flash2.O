package com.flash2.app.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.SystemClock
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import com.flash2.app.PopupActivity

class FlashAccessibilityService : AccessibilityService() {

    private var lastDown = 0L
    private var swallowUp = false

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {}

    override fun onKeyEvent(event: KeyEvent): Boolean {
        val isVolume = event.keyCode == KeyEvent.KEYCODE_VOLUME_UP ||
            event.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
        if (!isVolume) return false

        if (event.action == KeyEvent.ACTION_UP && swallowUp) {
            swallowUp = false
            return true
        }
        if (event.action != KeyEvent.ACTION_DOWN || event.repeatCount != 0) return false

        val now = SystemClock.uptimeMillis()
        val isDouble = now - lastDown < 500
        lastDown = now

        if (isDouble) {
            lastDown = 0L
            swallowUp = true
            openPopup()
            return true
        }
        return false
    }

    private fun openPopup() {
        val intent = Intent(this, PopupActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }
}
