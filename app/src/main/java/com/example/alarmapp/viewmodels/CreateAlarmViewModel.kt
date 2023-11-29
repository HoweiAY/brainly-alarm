package com.example.alarmapp.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.alarmapp.model.data.Alarm
import com.example.alarmapp.model.data.AlarmDatabase
import com.example.alarmapp.model.data.AlarmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateAlarmViewModel(): ViewModel() {
    private val _uiState = MutableStateFlow(CreateAlarmUiState())
    val uiState: StateFlow<CreateAlarmUiState> = _uiState.asStateFlow()

    init {
        resetUiState()
    }

    fun resetUiState() {

    }



}