package com.example.alarmapp.components.alarm

import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.alarmapp.MainActivity
import com.example.alarmapp.R
import com.example.alarmapp.model.data.AlarmDatabaseViewModel
import java.util.Calendar

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        //val alarmViewModel = AlarmViewModel(context)
        //val alarmDatabaseViewModel = AlarmDatabaseViewModel(context.applicationContext as Application)
        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val day = intent?.getIntExtra("dayOfWeek", 1)

        if (today == day) {
            val alarmScreenIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, 0, alarmScreenIntent, PendingIntent.FLAG_IMMUTABLE
            )
            Toast.makeText(context, "Alarm is triggered!!!", Toast.LENGTH_LONG).show()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "brainly_alarm_id"
                val notificationManager =
                    context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val builder = NotificationCompat.Builder(context.applicationContext, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Time to wake up!")
                    .setContentText("Click to turn off the alarm.")
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                notificationManager.notify(1, builder.build())
            }
        }
        val id = intent?.getIntExtra("alarmId", -1) ?: return
        Log.d("debug AlarmReceiver:", "RECEIVED ALARM ID $id: $day")
        }
}