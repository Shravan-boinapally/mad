package com.example.waterminder.db.dao

import androidx.room.*
import com.example.waterminder.db.entity.WaterGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterGoalDao {

    @Query("SELECT * FROM water_goal WHERE id = 1")
    fun getWaterGoal(): Flow<WaterGoalEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGoal(goal: WaterGoalEntity)

    @Query("SELECT * FROM water_goal ORDER BY id DESC LIMIT 1")
    suspend fun getLatestGoal(): WaterGoalEntity?
}
