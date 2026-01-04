package com.example.waterminder.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_intake")
data class WaterIntakeEntity(
    @PrimaryKey val id: Int = 1,
    val intakeMl: Int
)

