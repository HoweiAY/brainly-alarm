package com.example.alarmapp.model.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AlarmRepository(private val alarmDao: AlarmDao) {
    val allAlarms: LiveData<List<Alarm>> = alarmDao.getAllAlarms()
    val foundAlarm = MutableLiveData<Alarm?>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertAlarm(alarm: Alarm) {
        coroutineScope.launch(Dispatchers.IO) {
            alarmDao.insert(alarm)
        }
    }

    fun updateAlarm(alarm: Alarm) {
        coroutineScope.launch(Dispatchers.IO) {
            alarmDao.update(alarm)
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        coroutineScope.launch(Dispatchers.IO) {
            alarmDao.delete(alarm)
        }
    }

    fun getAllAlarms() {
        coroutineScope.launch(Dispatchers.IO) {
            alarmDao.getAllAlarms()
        }
    }

    suspend fun getAlarmById(id: Int): Alarm? {
        return coroutineScope.async(Dispatchers.IO) {
            alarmDao.getAlarmById(id)
        }.await()
    }
}