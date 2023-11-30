package com.example.alarmapp.viewmodels

import android.util.Log
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
                selectedAlarms = mutableListOf<Alarm>(),
                enabledAlarms = mutableListOf<Alarm>()
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

    fun toggleAlarmEnabled(alarm: Alarm, enable: Boolean = true) {
        val alarmsEnabled = _uiState.value.enabledAlarms
        if (enable && !alarmsEnabled.contains(alarm)) {
            alarmsEnabled.add(alarm)
        }
        else if (!enable && alarmsEnabled.contains(alarm)) {
            alarmsEnabled.remove(alarm)
        }
        _uiState.update { currentState ->
            currentState.copy(enabledAlarms = alarmsEnabled, enableAlarmChanged = !_uiState.value.enableAlarmChanged)
        }
        Log.i("debug toggleAlarmEnabled", "toggleAlarmEnabled: ${_uiState.value.enabledAlarms}")
        Log.i("debug toggleAlarmEnabled", "enableAlarmChanged: ${_uiState.value.enableAlarmChanged}")
    }

    fun enableAllAlarms(alarmData: List<Alarm>): Boolean {
        val alarms =
            if (_uiState.value.enabledAlarms.size == alarmData.size) mutableListOf<Alarm>()
            else alarmData.toMutableList()
        _uiState.update { currentState ->
            currentState.copy(enabledAlarms = alarms, enableAlarmChanged = !_uiState.value.enableAlarmChanged)
        }
        return _uiState.value.enabledAlarms.isNotEmpty()
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

    fun selectAllAlarms(alarmData: List<Alarm>) {
        val alarms =
            if (_uiState.value.selectedAlarms.size == alarmData.size) mutableListOf()
            else alarmData.toMutableList()
        _uiState.update { currentState ->
            currentState.copy(selectedAlarms = alarms)
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