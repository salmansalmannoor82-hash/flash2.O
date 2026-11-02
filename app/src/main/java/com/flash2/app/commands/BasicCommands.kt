package com.flash2.app.commands

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.AlarmClock
import android.provider.MediaStore
import android.provider.Settings

private fun launch(ctx: Context, intent: Intent) {
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    ctx.startActivity(intent)
}

class OpenCameraCommand : PhoneCommand {
    override val triggers = listOf("camera", "कैमरा", "कैमेरा")
    override fun run(ctx: Context): String {
        launch(ctx, Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA))
        return "Camera khol diya."
    }
}

class OpenCalculatorCommand : PhoneCommand {
    override val triggers = listOf("calculator", "कैलकुलेटर", "कैल्कुलेटर")
    override fun run(ctx: Context): String {
        launch(ctx, Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_CALCULATOR))
        return "Calculator khol diya."
    }
}

class OpenYouTubeCommand : PhoneCommand {
    override val triggers = listOf("youtube", "यूट्यूब")
    override fun run(ctx: Context): String {
        launch(ctx, Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com")))
        return "YouTube khol diya."
    }
}

class OpenSettingsCommand : PhoneCommand {
    override val triggers = listOf("settings", "सेटिंग")
    override fun run(ctx: Context): String {
        launch(ctx, Intent(Settings.ACTION_SETTINGS))
        return "Settings khol di."
    }
}

class OpenWifiCommand : PhoneCommand {
    override val triggers = listOf("wifi", "wi-fi", "वाईफाई", "वाई-फाई")
    override fun run(ctx: Context): String {
        launch(ctx, Intent(Settings.ACTION_WIFI_SETTINGS))
        return "WiFi ki settings khol di."
    }
}

class OpenAlarmCommand : PhoneCommand {
    override val triggers = listOf("alarm", "अलार्म")
    override fun run(ctx: Context): String {
        launch(ctx, Intent(AlarmClock.ACTION_SHOW_ALARMS))
        return "Alarm khol diya."
    }
}

class OpenDialerCommand : PhoneCommand {
    override val triggers = listOf("dialer", "dial", "call", "कॉल")
    override fun run(ctx: Context): String {
        launch(ctx, Intent(Intent.ACTION_DIAL))
        return "Dialer khol diya."
    }
}

fun defaultCommands(): List<PhoneCommand> = listOf(
    OpenCameraCommand(),
    OpenCalculatorCommand(),
    OpenYouTubeCommand(),
    OpenWifiCommand(),
    OpenSettingsCommand(),
    OpenAlarmCommand(),
    OpenDialerCommand()
)
