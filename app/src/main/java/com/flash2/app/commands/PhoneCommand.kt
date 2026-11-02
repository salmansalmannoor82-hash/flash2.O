package com.flash2.app.commands

import android.content.Context

interface PhoneCommand {
    val triggers: List<String>
    val needsAdvanced: Boolean get() = false
    fun run(ctx: Context): String
}

class CommandRouter(private val commands: List<PhoneCommand>) {
    fun find(text: String, advancedOn: Boolean): PhoneCommand? {
        val t = text.lowercase()
        return commands.firstOrNull { c ->
            (!c.needsAdvanced || advancedOn) && c.triggers.any { t.contains(it) }
        }
    }
}
