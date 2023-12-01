package com.example.alarmapp.components.menus.viewModels

import com.example.alarmapp.model.data.Alarm

data class HomeUiState(
    val optionsExpanded: Boolean = false,
    val alarmEditEnabled: Boolean = false,

    val enableAlarmChanged: Boolean = false,
    val alarmMsgChanged: Boolean = false,

    val selectedAlarms: MutableList<Alarm> = mutableListOf<Alarm>(),
    val enabledAlarms: MutableList<Alarm> = mutableListOf<Alarm>(),

    val nextAlarmDay: Int = 0,
    val nextAlarmHour: Int = 0,
    val nextAlarmMinute: Int = 0,

    val nextAlarmMsg: String = "No alarms set"
)
