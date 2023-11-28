package com.example.alarmapp.viewmodels

data class HomeUiState(
    val optionsExpanded: Boolean = false,
    val alarmEditEnabled: Boolean = false,

    val selectedAlarms: MutableList<Int> = mutableListOf<Int>()
)
