    package com.example.alarmapp.components.alarm

    import android.app.NotificationManager
    import android.app.PendingIntent
    import android.content.BroadcastReceiver
    import android.content.Context
    import android.content.Intent
    import android.os.Build
    import android.util.Log
    import androidx.core.app.NotificationCompat
    import com.example.alarmapp.MainActivity
    import com.example.alarmapp.R
    import com.example.alarmapp.utils.AlarmSoundManager
    import java.util.Calendar

    class AlarmReceiver: BroadcastReceiver() {
        //private var alarmSound: Ringtone? = null
        private var soundManager: AlarmSoundManager? = null

        override fun onReceive(context: Context?, intent: Intent?) {
            val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            val currentMinute = Calendar.getInstance().get(Calendar.MINUTE)

            val id = intent?.getIntExtra("alarmId", 0)
            val day = intent?.getIntExtra("dayOfWeek", today)
            val hour = intent?.getIntExtra("hour", -1)
            val minute = intent?.getIntExtra("minute", -1)
            val task = intent?.getStringExtra("task")
            val roundCount = intent?.getIntExtra("roundCount", 1)
            val difficulty = intent?.getStringExtra("difficulty")
            val sound = intent?.getStringExtra("sound")
            val snooze = intent?.getBooleanExtra("snooze", true)
            val enabled = intent?.getBooleanExtra("enabled", true)

            if (today == day && hour == currentHour && minute == currentMinute) {
                val alarmScreenIntent = Intent(context, MainActivity::class.java)

                alarmScreenIntent.putExtra("alarmId", id)
                alarmScreenIntent.putExtra("dayOfWeek", day)
                alarmScreenIntent.putExtra("hour", hour)
                alarmScreenIntent.putExtra("minute", minute)
                alarmScreenIntent.putExtra("task", task)
                alarmScreenIntent.putExtra("roundCount", roundCount)
                alarmScreenIntent.putExtra("difficulty", difficulty)
                alarmScreenIntent.putExtra("sound", sound)
                alarmScreenIntent.putExtra("snooze", snooze)
                alarmScreenIntent.putExtra("enabled", enabled)
                alarmScreenIntent.putExtra("alarmTriggered", true)

                alarmScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                val pendingIntent = PendingIntent.getActivity(
                    context, 0, alarmScreenIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channelId = "brainly_alarm_id"
                    val notificationManager =
                        context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    val builder = NotificationCompat.Builder(context.applicationContext, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Time to wake up!")
                        .setContentText("Click to turn off the alarm.")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                    notificationManager.notify(1, builder.build())
                }

                soundManager = AlarmSoundManager.getInstance(context)
                soundManager?.playAlarmSound(sound)

                context?.startActivity(alarmScreenIntent)
            }

            val alarmId = intent?.getIntExtra("alarmId", -1) ?: return
            Log.d("debug AlarmReceiver:", "RECEIVED ALARM ID $alarmId: $day")
        }
    }