package com.example.alarmapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.alarmapp.model.data.taskDifficulties
import com.example.alarmapp.model.data.taskTypes

data class CreateAlarmUiState(
    val alarmId: Int? = null,

    val weekdaysSelected: MutableList<String> = mutableListOf<String>(),
    val hourSelected: Int = 8,
    val minuteSelected: Int = 0,

    val taskSelected: String = taskTypes[0],
    val roundsSelected: Int = 1,
    val difficultySelected: String = taskDifficulties[0],
    val alarmSoundSelected: String = "Default",
    val snoozeEnabled: Boolean = true,

    var taskSelectorExpanded: Boolean = false,
)
