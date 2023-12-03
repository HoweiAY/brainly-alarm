package com.example.alarmapp.components.menus.viewModels

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.alarmapp.model.data.Alarm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.abs

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
                //enabledAlarms = mutableListOf<Alarm>()
            )
        }
        updateNextAlarm(uiState.value.enabledAlarms)
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
        val enabledAlarms = _uiState.value.enabledAlarms
        val updatedEnabledAlarms = mutableListOf<Alarm>()
        for (enabledAlarm in enabledAlarms) {
            if (alarm.id != enabledAlarm.id) updatedEnabledAlarms.add(enabledAlarm)
        }
        if (enable) updatedEnabledAlarms.add(alarm)
        _uiState.update { currentState ->
            currentState.copy(enabledAlarms = updatedEnabledAlarms, enableAlarmChanged = !_uiState.value.enableAlarmChanged)
        }
        updateNextAlarm(uiState.value.enabledAlarms)
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
        updateNextAlarm(uiState.value.enabledAlarms)
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

    fun updateNextAlarmMsg(alarmData: List<Alarm>) {
        var nextAlarmDay = _uiState.value.nextAlarmDay
        val nextAlarmHour = _uiState.value.nextAlarmHour
        val nextAlarmMinute = _uiState.value.nextAlarmMinute

        val nextAlarmMsg = if (alarmData.isNotEmpty())
            StringBuilder()
                .append("Next alarm in ")
                .append(nextAlarmDay.toString())
                .append(if (nextAlarmDay != 1) " days " else " day ")
                .append(nextAlarmHour.toString())
                .append(if (nextAlarmHour != 1) " hours " else " hour ")
                .append(nextAlarmMinute.toString())
                .append(if (nextAlarmMinute != 1) " minutes " else " minute ")
                .toString()
            else "No alarms set"

        _uiState.update { currentState ->
            currentState.copy(
                nextAlarmMsg = nextAlarmMsg,
                alarmMsgChanged = !_uiState.value.alarmMsgChanged
            )
        }
    }

    fun updateNextAlarm(alarmData: List<Alarm> = listOf<Alarm>()) {
        if (alarmData.isEmpty()) {
            updateNextAlarmMsg(_uiState.value.enabledAlarms)
            return
        }

        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val alarmCalendars = mutableListOf<Calendar>()

        alarmData.forEach { alarm ->
            val days = alarm.days
                .map { alarm.getCalendarDay(it) }
                .sorted()
            days.forEach { day ->
                val alarmCalendar = Calendar.getInstance()
                alarmCalendar.set(Calendar.DAY_OF_WEEK, day)
                alarmCalendar.set(Calendar.HOUR_OF_DAY, alarm.hour)
                alarmCalendar.set(Calendar.MINUTE, alarm.minute)
                alarmCalendar.set(Calendar.SECOND, 0)
                alarmCalendar.set(Calendar.MILLISECOND, 0)

                alarmCalendars.add(alarmCalendar)
            }
        }

        if (alarmCalendars.isNotEmpty()) {
            val nextAlarmCalendar = nextAlarmCalendar(calendar, alarmCalendars)
            val alarmDay = nextAlarmCalendar.get(Calendar.DAY_OF_WEEK)
            val alarmHour = nextAlarmCalendar.get(Calendar.HOUR_OF_DAY)
            val alarmMinute = nextAlarmCalendar.get(Calendar.MINUTE)

            var nextAlarmMinute =
                if (currentMinute > alarmMinute) 60 - currentMinute + alarmMinute
                else alarmMinute - currentMinute

            var nextAlarmHour = when {
                currentHour == alarmHour ->
                    if (currentDay == alarmDay && currentMinute > alarmMinute) 23
                    else {
                        if (currentMinute > alarmMinute) 23
                        else 0
                    }
                else -> {
                    if (currentHour > alarmHour) {
                        if (currentMinute > alarmMinute) 24 - abs(currentHour - alarmHour) - 1
                        else 24 - abs(currentHour - alarmHour)
                    } else {
                        if (currentMinute > alarmMinute) alarmHour - currentHour - 1
                        else alarmHour - currentHour
                    }
                }
            }

            var nextAlarmDay = when {
                currentDay == alarmDay -> {
                    if (currentHour > alarmHour ||
                        (currentHour == alarmHour && currentMinute > alarmMinute)
                    ) 6 else 0
                }

                currentDay > alarmDay -> {
                    if (currentHour > alarmHour ||
                        (currentHour == alarmHour && currentMinute > alarmMinute)
                    ) {
                        7 - abs(currentDay - alarmDay) - 1
                    } else 7 - abs(currentDay - alarmDay)
                }

                else -> {
                    if (currentHour > alarmHour ||
                        (currentHour == alarmHour && currentMinute > alarmMinute)
                    ) {
                        alarmDay - currentDay - 1
                    } else alarmDay - currentDay
                }
            }

            _uiState.update { currentState ->
                currentState.copy(
                    nextAlarmDay = nextAlarmDay,
                    nextAlarmHour = nextAlarmHour,
                    nextAlarmMinute = nextAlarmMinute
                )
            }
        }
        updateNextAlarmMsg(_uiState.value.enabledAlarms)
    }

    private fun nextAlarmCalendar(currentCalendar: Calendar, calendars: List<Calendar>): Calendar {
        val currentDay = currentCalendar.get(Calendar.DAY_OF_WEEK)
        val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentCalendar.get(Calendar.MINUTE)

        var nextAlarmDay: Int? = null
        var nextAlarmHour: Int? = null
        var nextAlarmMinute: Int? = null
        var nextAlarmCalendar: Calendar = currentCalendar

        for (calendar in calendars) {
            //if (calendar.timeInMillis - currentCalendar.timeInMillis <= 0) continue

            val alarmDay = calendar.get(Calendar.DAY_OF_WEEK)
            val alarmHour = calendar.get(Calendar.HOUR_OF_DAY)
            val alarmMinute = calendar.get(Calendar.MINUTE)

            val minuteDifference =
                if (currentMinute > alarmMinute) 60 - currentMinute + alarmMinute
                else alarmMinute - currentMinute

            val hourDifference = when {
                currentHour == alarmHour ->
                    if (currentDay == alarmDay && currentMinute > alarmMinute) 23
                    else {
                        if (currentMinute > alarmMinute) 23
                        else 0
                    }
                else -> {
                    if (currentHour > alarmHour) {
                        if (currentMinute > alarmMinute) 24 - abs(currentHour - alarmHour) - 1
                        else 24 - abs(currentHour - alarmHour)
                    } else {
                        if (currentMinute > alarmMinute) alarmHour - currentHour - 1
                        else alarmHour - currentHour
                    }
                }
            }

            val dayDifference = when {
                currentDay == alarmDay -> {
                    if (currentHour > alarmHour ||
                        (currentHour == alarmHour && currentMinute > alarmMinute)
                    ) 6 else 0
                }
                currentDay > alarmDay -> {
                    if (currentHour > alarmHour ||
                        (currentHour == alarmHour && currentMinute > alarmMinute)
                    ) {
                        7 - abs(currentDay - alarmDay) - 1
                    } else 7 - abs(currentDay - alarmDay)
                }
                else -> {
                    if (currentHour > alarmHour ||
                        (currentHour == alarmHour && currentMinute > alarmMinute)
                    ) { alarmDay - currentDay - 1
                    } else alarmDay - currentDay
                }
            }
            if (nextAlarmMinute == null && nextAlarmHour == null && nextAlarmDay == null) {
                nextAlarmMinute = minuteDifference
                nextAlarmHour = hourDifference
                nextAlarmDay = dayDifference
                nextAlarmCalendar = calendar
            } else {
                if ((dayDifference < nextAlarmDay!!) ||
                    ((dayDifference == nextAlarmDay && hourDifference < nextAlarmHour!!) ||
                            (hourDifference == nextAlarmHour && minuteDifference < nextAlarmMinute!!))) {
                    nextAlarmMinute = minuteDifference
                    nextAlarmHour = hourDifference
                    nextAlarmDay = dayDifference
                    nextAlarmCalendar = calendar
                }
            }
        }
        return nextAlarmCalendar
    }
}