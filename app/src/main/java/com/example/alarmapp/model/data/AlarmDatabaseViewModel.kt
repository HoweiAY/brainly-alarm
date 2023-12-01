package com.example.alarmapp.model.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarmapp.model.data.Alarm
import com.example.alarmapp.model.data.AlarmDatabase
import com.example.alarmapp.model.data.AlarmRepository
import kotlinx.coroutines.launch

class AlarmDatabaseViewModel(application: Application): ViewModel() {
    private val alarmRepository: AlarmRepository
    val allAlarms: LiveData<List<Alarm>>
    private val _foundAlarm = MutableLiveData<Alarm?>()
    val foundAlarm: LiveData<Alarm?>
        get() = _foundAlarm

    init {
        val alarmDatabase = AlarmDatabase.getAlarmDatabase(application)
        val alarmDao = alarmDatabase.alarmDao()
        alarmRepository = AlarmRepository(alarmDao)

        allAlarms = alarmRepository.allAlarms
        //foundAlarm = alarmRepository.foundAlarm
    }

    fun insertAlarm(alarm: Alarm) {
        alarmRepository.insertAlarm(alarm)
        Log.i("debug insertAlarm viewModel: ", "Inserting alarm with ID ${alarm.id}")
    }

    fun updateAlarm(alarm: Alarm) {
        alarmRepository.updateAlarm(alarm)
        Log.i("debug updateAlarm viewModel: ", "Updating alarm with ID ${alarm.id}")
    }

    fun deleteAlarm(alarm: Alarm) {
        alarmRepository.deleteAlarm(alarm)
        Log.i("debug deleteAlarm viewModel: ", "Deleting alarm with ID ${alarm.id}")
    }

    fun getAllAlarms(): List<Alarm> {
        return allAlarms.value ?: emptyList()
    }

    fun getAlarmById(id: Int) {
        viewModelScope.launch {
            val alarm = alarmRepository.getAlarmById(id)
            _foundAlarm.value = alarm
            Log.i("debug getAlarmById: ", "Getting alarm by ID $id: ${foundAlarm.value?.id}")
        }
    }
}