package com.example.alarmapp.model.data

import android.content.Context

interface AppContainer {
    val alarmRepository: AlarmRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val alarmRepository: AlarmRepository by lazy {
        OfflineAlarmRepository(AlarmDatabase.getAlarmDatabase(context).alarmDao())
    }
}