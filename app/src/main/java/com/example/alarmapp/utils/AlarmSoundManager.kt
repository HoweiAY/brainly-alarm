package com.example.alarmapp.utils

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri

class AlarmSoundManager(private val context: Context?) {
    private var alarmSound: Ringtone? = null

    fun playAlarmSound(sound: String?) {
        if (alarmSound == null) {
            if (sound == null || (sound != null && sound == "Default")) {
                alarmSound = RingtoneManager.getRingtone(
                    context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                )
            } else {
                sound?.let {
                    alarmSound = RingtoneManager.getRingtone(
                        context, Uri.parse(sound)
                    )
                }
            }
        }
        alarmSound?.isLooping = true
        alarmSound?.play()
    }

    fun stopAlarmSound() {
        alarmSound?.stop()
    }
}