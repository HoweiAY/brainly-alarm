package com.example.alarmapp.components.menus.viewModels

import com.example.alarmapp.model.data.Alarm

data class HomeUiState(
    val optionsExpanded: Boolean = false,
    val alarmEditEnabled: Boolean = false,

    val enableAlarmChanged: Boolean = false,

    val selectedAlarms: MutableList<Alarm> = mutableListOf<Alarm>(),
    val enabledAlarms: MutableList<Alarm> = mutableListOf<Alarm>()
)
