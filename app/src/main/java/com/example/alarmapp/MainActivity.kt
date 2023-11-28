package com.example.alarmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.alarmapp.components.games.MemoryGame
import com.example.alarmapp.components.missions.Difficulty
import com.example.alarmapp.components.missions.MathEquation
import com.example.alarmapp.components.missions.PhoneShaking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //MemoryGame()
            //AlarmClockApp()
            //PhoneShaking()
            MathEquation(difficulty = Difficulty.EASY)
        }
    }
}