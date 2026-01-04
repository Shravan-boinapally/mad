package com.example.waterminder.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.waterminder.db.entity.WaterLogEntity

@Dao
interface WaterLogDao {

    @Insert
    suspend fun insertLog(log: WaterLogEntity)

    @Query("SELECT * FROM water_log ORDER BY timestamp ASC")
    suspend fun getAllLogs(): List<WaterLogEntity>

    @Query("DELETE FROM water_log")
    suspend fun clearLogs()
}
