package com.example.alarmapp.model

import com.example.alarmapp.model.data.weekdays
import java.util.Calendar
import java.util.Locale

data class Alarm(
    val days: List<String>,
    val hour: Int,
    val minute: Int
) {
    fun getTimeInMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val daysOfWeek = days
            .map { getCalendarDay(it) }
            .sorted()
        for (dayOfWeek in daysOfWeek) {
            if (dayOfWeek > currentDay ||
                (dayOfWeek == currentDay &&
                        (hour > currentHour ||
                                (hour == currentHour && minute > currentMinute)))) {
                calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek)
            }
        }
        return calendar.timeInMillis
    }

    fun getTimeString(): String {
        val hourString = String.format(Locale.getDefault(), "%02d", hour)
        val minuteString = String.format(Locale.getDefault(), "%02d", minute)
        return "$hourString:$minuteString"
    }

    fun getDaysString(): String {
        val weekdaysInOrder = weekdays
        return when(days.distinct().size) {
            7 -> "Every day"
            else -> days.distinct().sortedBy { weekdaysInOrder.indexOf(it) }.joinToString()
        }
    }

    private fun getCalendarDay(day: String): Int {
        return when (day.lowercase()) {
            "sun" -> Calendar.SUNDAY
            "mon" -> Calendar.MONDAY
            "tue" -> Calendar.TUESDAY
            "wed" -> Calendar.WEDNESDAY
            "thur" -> Calendar.THURSDAY
            "fri" -> Calendar.FRIDAY
            "sat" -> Calendar.SATURDAY
            else -> throw IllegalArgumentException("Invalid day of week: $day")
        }
    }
}
