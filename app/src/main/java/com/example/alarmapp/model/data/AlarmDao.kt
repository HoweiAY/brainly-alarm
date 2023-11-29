package com.example.alarmapp.model.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alarm: Alarm)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(alarm: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)

    @Query("SELECT * from alarms ORDER BY id ASC")
    fun getAllAlarms(): LiveData<List<Alarm>>

    @Query("SELECT * from alarms WHERE id = :id LIMIT 1")
    fun getAlarmById(id: Int): Alarm
}