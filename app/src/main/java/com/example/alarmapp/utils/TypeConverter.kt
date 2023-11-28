package com.example.alarmapp.utils

import androidx.room.TypeConverter

class TypeConverter {
    @TypeConverter
    fun listToString(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun stringToList(string: String): List<String> {
        return string.split(",")
    }
}