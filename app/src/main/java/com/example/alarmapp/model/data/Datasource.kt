package com.example.alarmapp.model.data

// list of Alarm objects
val alarmData = listOf<Alarm>(
    Alarm(0, listOf("Mon"), 8, 0),
    Alarm(1, listOf("Sun", "Fri", "Wed"), 9, 15),
    Alarm(2, listOf("Sat"), 6, 40),
    Alarm(3, listOf("Mon", "Wed", "Sun", "Thu", "Tue", "Sat", "Fri"), 11, 25),
    )

// list of week day Strings
val weekdays = listOf<String>("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

// list of task types
val taskTypes = listOf<String>("Memory", "Math", "Shake phone", "Scan code", "None")

//list of task difficulties
val taskDifficulties = listOf<String>("Easy", "Normal", "Hard")