package com.example.alarmapp.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.alarmapp.model.data.Alarm
import com.example.alarmapp.model.data.AlarmDatabase
import com.example.alarmapp.model.data.AlarmRepository

class AlarmDatabaseViewModel(application: Application): ViewModel() {
    private val alarmRepository: AlarmRepository
    val allAlarms: LiveData<List<Alarm>>
    val foundAlarm: LiveData<Alarm>

    init {
        val alarmDatabase = AlarmDatabase.getAlarmDatabase(application)
        val alarmDao = alarmDatabase.alarmDao()
        alarmRepository = AlarmRepository(alarmDao)

        allAlarms = alarmRepository.allAlarms
        foundAlarm = alarmRepository.foundAlarm
    }

    fun insertAlarm(alarm: Alarm) {
        alarmRepository.insertAlarm(alarm)
    }

    fun updateAlarm(alarm: Alarm) {
        alarmRepository.updateAlarm(alarm)
    }

    fun deleteAlarm(alarm: Alarm) {
        alarmRepository.deleteAlarm(alarm)
    }

    fun getAllAlarms() {
        alarmRepository.getAllAlarms()
    }

    fun getAlarmById(id: Int) {
        alarmRepository.getAlarmById(id)
    }
}