package com.example.alarmapp.model.data

import com.example.alarmapp.model.Alarm


val alarmData = listOf<Alarm>(
    Alarm(listOf("Mon"), 8, 0),
    Alarm(listOf("Sun", "Fri", "Wed"), 9, 15),
    Alarm(listOf("Sat"), 6, 40),
    Alarm(listOf("Mon", "Wed", "Sun", "Thur", "Tue", "Sat", "Fri"), 11, 25),
    )
