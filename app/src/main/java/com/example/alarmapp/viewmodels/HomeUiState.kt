package com.example.alarmapp.viewmodels

import com.example.alarmapp.model.data.Alarm

data class HomeUiState(
    val optionsExpanded: Boolean = false,
    val alarmEditEnabled: Boolean = false,

    val selectedAlarms: MutableList<Alarm> = mutableListOf<Alarm>(),
    val enabledAlarms: MutableList<Alarm> = mutableListOf<Alarm>()
)
