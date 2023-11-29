package com.example.alarmapp.model.data

// list of week day Strings
val weekdays = listOf<String>("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

// list of task types
val taskTypes = listOf<String>("Memory", "Math", "Shake phone", "Scan code", "None")

//list of task difficulties
val taskDifficulties = listOf<String>("Easy", "Normal", "Hard")

// list of Alarm objects
val alarmData = listOf<Alarm>(
    Alarm(days = listOf("Mon"), hour = 8, minute = 0, task = "Memory"),
    Alarm(days = listOf("Sun", "Fri", "Wed"), hour = 9, minute = 15, task = "Math", rounds = 3, difficulty = "Hard"),
    Alarm(days = listOf("Sat"), hour = 6, minute = 40),
    Alarm(days = listOf("Mon", "Wed", "Sun", "Thu", "Tue", "Sat", "Fri"), hour = 11, minute = 25),
    )


