package com.example.waterminder.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_goal")
data class WaterGoalEntity(
    @PrimaryKey val id: Int = 1,
    val goalMl: Int
)
