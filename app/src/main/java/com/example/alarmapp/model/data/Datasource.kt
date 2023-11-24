package com.example.alarmapp.model.data

import com.example.alarmapp.model.Alarm

// list of Alarm objects
val alarmData = listOf<Alarm>(
    Alarm(listOf("Mon"), 8, 0),
    Alarm(listOf("Sun", "Fri", "Wed"), 9, 15),
    Alarm(listOf("Sat"), 6, 40),
    Alarm(listOf("Mon", "Wed", "Sun", "Thu", "Tue", "Sat", "Fri"), 11, 25),
    )

// list of week day Strings
val weekdays = listOf<String>("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

// list of task types
val taskTypes = listOf<String>("Memory", "Maths", "QR code")

//list of task difficulties
val taskDifficulties = listOf<String>("Easy", "Normal", "Hard")