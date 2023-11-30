package com.example.alarmapp.components.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.alarmapp.model.data.Alarm
import java.util.Calendar

class AlarmViewModel(private val context: Context): ViewModel() {
    @SuppressLint("ScheduleExactAlarm", "UnspecifiedImmutableFlag")
    fun setAlarm(alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmId = alarm.id

        val daysOfWeek = alarm.days.map { alarm.getCalendarDay(it) }
        for (dayOfWeek in daysOfWeek) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek)
            calendar.set(Calendar.HOUR_OF_DAY, alarm.hour)
            calendar.set(Calendar.MINUTE, alarm.minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra("alarmId", alarmId)
            intent.putExtra("dayOfWeek", dayOfWeek)

            val requestCode = getPendingIntentRequestCode(alarmId, dayOfWeek)
            val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)

            val alarmTimeInMillis = calendar.timeInMillis
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTimeInMillis,
                pendingIntent)
        }
    }

    fun setMultipleAlarms(alarmData: List<Alarm>) {
        for (alarm in alarmData) {
            setAlarm(alarm)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun cancelAlarm(alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmId = alarm.id
        val daysOfWeek = alarm.days.map { alarm.getCalendarDay(it) }
        for (dayOfWeek in daysOfWeek) {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra("alarmId", alarmId)
            intent.putExtra("dayOfWeek", dayOfWeek)

            val requestCode = getPendingIntentRequestCode(alarmId, dayOfWeek)
            val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun getPendingIntentRequestCode(alarmId: Int, dayOfWeek: Int): Int {
        return (alarmId * 10 + dayOfWeek) % Int.MAX_VALUE
    }

}