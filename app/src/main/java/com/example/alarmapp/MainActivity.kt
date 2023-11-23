package com.example.alarmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.alarmapp.components.games.MemoryGame

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //MemoryGame()
            AlarmClockApp()
        }
    }
}