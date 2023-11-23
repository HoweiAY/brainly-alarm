package com.example.alarmapp.model

import java.util.Calendar
import java.util.Locale

data class Alarm(
    val day: String,
    val hour: Int,
    val minute: Int
) {
    fun getTimeInMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, getCalendarDay())
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun getTimeString(): String {
        val hourString = String.format(Locale.getDefault(), "%02d", hour)
        val minuteString = String.format(Locale.getDefault(), "%02d", minute)
        return "$hourString:$minuteString"
    }

    private fun getCalendarDay(): Int {
        return when (day.toLowerCase()) {
            "sunday" -> Calendar.SUNDAY
            "monday" -> Calendar.MONDAY
            "tuesday" -> Calendar.TUESDAY
            "wednesday" -> Calendar.WEDNESDAY
            "thursday" -> Calendar.THURSDAY
            "friday" -> Calendar.FRIDAY
            "saturday" -> Calendar.SATURDAY
            else -> throw IllegalArgumentException("Invalid day of week: $day")
        }
    }
}
