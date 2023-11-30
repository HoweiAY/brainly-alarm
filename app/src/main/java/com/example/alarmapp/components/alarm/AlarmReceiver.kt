package com.example.alarmapp.components.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.alarmapp.MainActivity

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmScreenIntent = Intent(context, MainActivity::class.java)
        Toast.makeText(context, "Alarm is triggered!!!", Toast.LENGTH_LONG).show()
    }
}