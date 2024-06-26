package com.example.alarmapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alarmapp.model.data.AlarmDatabaseViewModel
import com.example.alarmapp.utils.AlarmSoundManager

class MainActivity : ComponentActivity() {
    private var soundManager: AlarmSoundManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("debug onCreate: ", "onCreate: $applicationContext")
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        val alarmIntent = intent
        soundManager = AlarmSoundManager.getInstance(applicationContext)

        setContent {
            val context = LocalContext.current
            val owner = LocalViewModelStoreOwner.current
            owner?.let {
                val alarmDatabaseViewModel: AlarmDatabaseViewModel = viewModel(
                    it,
                    "AlarmDataBaseViewModel",
                    AlarmDatabaseViewModelFactory(
                        LocalContext.current.applicationContext as Application
                    )
                )
                BrainlyAlarmApp(alarmIntent, context, {stopAlarmSound()}, alarmDatabaseViewModel = alarmDatabaseViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundManager?.stopAlarmSound()
    }

    private fun stopAlarmSound() {
        soundManager?.stopAlarmSound()
    }

    private fun createNotificationChannel() {
        val channelId = "brainly_alarm_id"
        val channelName = "brainly_alarm"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }
}

class AlarmDatabaseViewModelFactory(val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmDatabaseViewModel(application) as T
    }
}