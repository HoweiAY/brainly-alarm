package com.example.alarmapp.model.data

import kotlinx.coroutines.flow.Flow

class OfflineAlarmRepository(private val alarmDao: AlarmDao): AlarmRepository {
    override suspend fun insertAlarm(alarm: Alarm) = alarmDao.insert(alarm)
    override suspend fun updateAlarm(alarm: Alarm) = alarmDao.update(alarm)
    override suspend fun deleteAlarm(alarm: Alarm) = alarmDao.delete(alarm)

    override fun getAllAlarms(): Flow<List<Alarm>> = alarmDao.getAllAlarms()
    override fun getAlarmById(id: Int): Alarm = alarmDao.getAlarmById(id)
}