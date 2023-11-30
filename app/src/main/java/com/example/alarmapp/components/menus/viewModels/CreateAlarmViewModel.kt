package com.example.alarmapp.components.menus.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.alarmapp.model.data.Alarm
import com.example.alarmapp.model.data.taskDifficulties
import com.example.alarmapp.model.data.taskTypes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateAlarmViewModel(): ViewModel() {
    private val _uiState = MutableStateFlow(CreateAlarmUiState())
    val uiState: StateFlow<CreateAlarmUiState> = _uiState.asStateFlow()

    fun resetUiState(alarm: Alarm? = null) {
        if (alarm != null) {
            _uiState.update { currentState ->
                if (alarm != null) {
                    currentState.copy(
                        alarmId = alarm.id,
                        weekdaysSelected = alarm.days.toMutableList(),
                        hourSelected = alarm.hour,
                        minuteSelected = alarm.minute,
                        taskSelected = alarm.task,
                        roundsSelected = alarm.rounds,
                        difficultySelected = alarm.difficulty,
                        alarmSoundSelected = alarm.sound,
                        snoozeEnabled = alarm.snooze
                    )
                } else {
                    currentState.copy(
                        alarmId = null,
                        weekdaysSelected = mutableListOf<String>(),
                        hourSelected = 8,
                        minuteSelected = 0,
                        taskSelected = taskTypes[0],
                        roundsSelected = 1,
                        difficultySelected = taskDifficulties[0],
                        alarmSoundSelected = "Default",
                        snoozeEnabled = true
                    )
                }
            }
        }
        val days = if (alarm != null) _uiState.value.weekdaysSelected.toString() else "Days: Nothing"
        val task = if (alarm != null) _uiState.value.taskSelected else "Task: Nothing"
        Log.i("debug create alarm resetUiState: ", "Create alarm UI state reset")
        Log.i("debug create alarm resetUiState: ", "days: $days")
        Log.i("debug create alarm resetUiState: ", "task: $task")
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