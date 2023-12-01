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
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableIntStateOf
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
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

            val id = intent?.getIntExtra("alarmId", 0)
            val day = intent?.getIntExtra("dayOfWeek", today)
            var hour = intent?.getIntExtra("hour", 8)
            var minute = intent?.getIntExtra("minute", 0)
            var task = intent?.getStringExtra("task")
            var roundCount = intent?.getIntExtra("roundCount", 1)
            var difficulty = intent?.getStringExtra("difficulty")
            var sound = intent?.getStringExtra("sound")
            var snooze = intent?.getBooleanExtra("snooze", true)
            var enabled = intent?.getBooleanExtra("enabled", true)

            if (today == day) {
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

                alarmScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                val pendingIntent = PendingIntent.getActivity(
                    context, 0, alarmScreenIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
                )

                //Toast.makeText(context, "Alarm is triggered!!!", Toast.LENGTH_LONG).show()

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
                context?.startActivity(alarmScreenIntent)
            }
            val alarmId = intent?.getIntExtra("alarmId", -1) ?: return
            Log.d("debug AlarmReceiver:", "RECEIVED ALARM ID $alarmId: $day")
            }
    }