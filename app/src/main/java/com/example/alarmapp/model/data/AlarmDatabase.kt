package com.example.alarmapp.model.data

import android.content.Context
import android.provider.CalendarContract.Instances
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Alarm::class], version = 1)
abstract class AlarmDatabase: RoomDatabase() {
    abstract fun alarmDao(): AlarmDao

    companion object {
        @Volatile
        private var Instance: AlarmDatabase? = null

        fun getAlarmDatabase(context: Context): AlarmDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AlarmDatabase::class.java, "alarm_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}