package com.example.alarmapp.viewmodels

import android.util.Log
import androidx.compose.runtime.currentRecomposeScope
import androidx.lifecycle.ViewModel
import com.example.alarmapp.model.data.Alarm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        resetUiState()
    }

    fun resetUiState() {
        _uiState.update { currentState ->
            currentState.copy(
                optionsExpanded = false,
                alarmEditEnabled = false,
                selectedAlarms = mutableListOf<Alarm>()
            )
        }
    }

    fun enableEdit() {
        dismissDropdown(editEnabled = true)
    }

    fun selectOptions() {
        _uiState.update { currentState ->
            currentState.copy(optionsExpanded = true)
        }
    }

    fun dismissDropdown(editEnabled: Boolean = false) {
        _uiState.update { currentState ->
            currentState.copy(
                optionsExpanded = false,
                alarmEditEnabled = editEnabled
            )
        }
    }

    fun toggleAlarmSelected(alarm: Alarm) {
        val alarmsSelected = _uiState.value.selectedAlarms
        _uiState.update {currentState ->
            if (alarmsSelected.contains(alarm)) {
                alarmsSelected.remove(alarm)
            }
            else {
                alarmsSelected.add(alarm)
            }
            currentState.copy(selectedAlarms = alarmsSelected)
        }
    }

    fun clearSelectedAlarm() {
        _uiState.update {currentState ->
            currentState.copy(selectedAlarms = mutableListOf<Alarm>())
        }
    }

    fun cancelAlarmsEdit() {
        _uiState.update { currentState ->
            currentState.copy(
                alarmEditEnabled = false
            )
        }
    }
}