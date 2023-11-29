package com.example.alarmapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.alarmapp.components.missions.Difficulty
import com.example.alarmapp.model.data.Alarm
import com.example.alarmapp.model.data.AlarmDatabase
import com.example.alarmapp.model.data.AlarmRepository
import com.example.alarmapp.model.data.taskTypes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.exp

class CreateAlarmViewModel(): ViewModel() {
    private val _uiState = MutableStateFlow(CreateAlarmUiState())
    val uiState: StateFlow<CreateAlarmUiState> = _uiState.asStateFlow()

    fun resetUiState(alarm: Alarm? = null) {
        if (alarm != null) {
            _uiState.update { currentState ->
                currentState.copy(
                    weekdaysSelected = alarm.days.toMutableList(),
                    hourSelected = alarm.hour,
                    minuteSelected = alarm.minute,
                    taskSelected = alarm.task,
                    roundsSelected = alarm.rounds,
                    difficultySelected = alarm.difficulty,
                    alarmSoundSelected = alarm.sound,
                    snoozeEnabled = alarm.snooze
                )
            }
        }
        val days = if (alarm != null) _uiState.value.weekdaysSelected.toString() else "Days: Nothing"
        val task = if (alarm != null) _uiState.value.taskSelected else "Task: Nothing"
        Log.i("debug create alarm resetUiState: ", "Create alarm UI state reset")
        Log.i("debug create alarm resetUiState: ", days)
        Log.i("debug create alarm resetUiState: ", task)
    }

    fun expandTaskSelector(expand: Boolean = false) {
        _uiState.update {
                currentState -> currentState.copy(taskSelectorExpanded = expand)
        }
    }

    fun updateWeekdays(weekday: String) {
        val weekdaysSelected = _uiState.value.weekdaysSelected
        if (weekdaysSelected.contains(weekday)) {
            weekdaysSelected.remove(weekday)
        } else {
            weekdaysSelected.add(weekday)
        }
        _uiState.update { currentState ->
            currentState.copy(weekdaysSelected = weekdaysSelected)
        }
        Log.i("debug updateWeekdays: ", "updated days: $weekdaysSelected")
    }

    fun updateHourSelected(hour: Int) {
        _uiState.update { currentState ->
            currentState.copy(hourSelected = hour)
        }
        Log.i("debug updateHourSelected: ", "updated hour: ${_uiState.value.hourSelected}")
    }

    fun updateMinuteSelected(minute: Int) {
        _uiState.update { currentState ->
            currentState.copy(minuteSelected = minute)
        }
        Log.i("debug updateMinuteSelected: ", "updated minute: ${_uiState.value.minuteSelected}")
    }

    fun updateTaskSelected(taskSelected: String = taskTypes[0]) {
        _uiState.update { currentState ->
            currentState.copy(taskSelected = taskSelected)
        }
    }

    fun updateRoundCount(rounds: Int = 1) {
        _uiState.update { currentState ->
            currentState.copy(roundsSelected = rounds)
        }
    }

    fun updateTaskDifficulty(difficulty: String = "Normal") {
        _uiState.update { currentState ->
            currentState.copy(difficultySelected = difficulty)
        }
    }

    fun updateSoundSelected(sound: String = "Default") {
        _uiState.update { currentState ->
            currentState.copy(alarmSoundSelected = sound)
        }
    }

    fun updateSnoozeEnabled(enabled: Boolean = true) {
        _uiState.update { currentState ->
            currentState.copy(snoozeEnabled = enabled)
        }
    }

}