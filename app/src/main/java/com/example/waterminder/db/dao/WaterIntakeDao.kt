package com.example.waterminder.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.waterminder.db.entity.WaterIntakeEntity

@Dao
interface WaterIntakeDao {

    @Query("SELECT * FROM water_intake WHERE id = 1")
    suspend fun getIntake(): WaterIntakeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveIntake(intake: WaterIntakeEntity)
}
