package com.example.alarmapp.model.data

import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun insertAlarm(alarm: Alarm)
    suspend fun updateAlarm(alarm: Alarm)
    suspend fun deleteAlarm(alarm: Alarm)

    fun getAllAlarms(): Flow<List<Alarm>>
    fun getAlarmById(id: Int): Alarm
}