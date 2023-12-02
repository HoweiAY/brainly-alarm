package com.example.alarmapp.utils

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log

class AlarmSoundManager private constructor (private val context: Context?) {
    private var alarmSound: Ringtone? = null

    companion object {
        @Volatile
        private var instance: AlarmSoundManager?= null

        fun getInstance(context: Context?): AlarmSoundManager =
            instance ?: synchronized(this) {
                instance ?: AlarmSoundManager(context?.applicationContext).also {
                    instance = it
                }
            }
    }

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
        Log.d("debug playAlarmSound: ", "play alarmSound: %%%%$alarmSound")
        Log.d("debug playAlarmSound: ", "playAlarmSound: context: $context")
    }

    fun stopAlarmSound() {
        Log.d("debug stopAlarmSound: ", "stop alarmSound: ----$alarmSound")
        alarmSound?.stop()
        alarmSound = null
        Log.d("debug stopAlarmSound: ", "stopAlarmSound: context: $context")
    }
}