package com.example.alarmapp.components.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.alarmapp.model.data.Alarm
import java.util.Calendar

class AlarmViewModel(private val context: Context): ViewModel() {
    @SuppressLint("ScheduleExactAlarm")
    fun setAlarm(alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmId = alarm.id

        val daysOfWeek = alarm.days.map { alarm.getCalendarDay(it) }
        Log.d("debug set alarm:", "daysOfWeek: $daysOfWeek")

        for (dayOfWeek in daysOfWeek) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            //calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek)
            calendar.set(Calendar.HOUR_OF_DAY, alarm.hour)
            calendar.set(Calendar.MINUTE, alarm.minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                calendar.set(Calendar.DAY_OF_WEEK, calendar.get(Calendar.DAY_OF_WEEK) + 1)
            }

            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra("alarmId", alarmId)
            intent.putExtra("dayOfWeek", dayOfWeek)
            intent.putExtra("task", alarm.task)
            intent.putExtra("roundCount", alarm.rounds)
            intent.putExtra("difficulty", alarm.difficulty)
            intent.putExtra("sound", alarm.sound)
            intent.putExtra("snooze", alarm.snooze)

            val requestCode = getPendingIntentRequestCode(alarmId, dayOfWeek)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmTimeInMillis = calendar.timeInMillis
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTimeInMillis, pendingIntent)

            Log.d("debug set alarm:", "alarm set: $alarmId - $requestCode")
        }
    }

    fun cancelAlarm(alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmId = alarm.id
        val daysOfWeek = alarm.days.map { alarm.getCalendarDay(it) }
        for (dayOfWeek in daysOfWeek) {
            val intent = Intent(context, AlarmReceiver::class.java)
            val requestCode = getPendingIntentRequestCode(alarmId, dayOfWeek)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.cancel(pendingIntent)

            Log.d("debug cancel alarm:", "alarm cancelled: $alarmId")
        }
    }

    private fun getPendingIntentRequestCode(alarmId: Int, dayOfWeek: Int): Int {
        return ("$alarmId$dayOfWeek").hashCode()
    }

}