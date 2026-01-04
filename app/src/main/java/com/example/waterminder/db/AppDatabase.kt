package com.example.waterminder.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.waterminder.db.dao.UserDAO
import com.example.waterminder.db.dao.WaterGoalDao
import com.example.waterminder.db.dao.WaterIntakeDao
import com.example.waterminder.db.dao.WaterLogDao
import com.example.waterminder.db.entity.UserEntity
import com.example.waterminder.db.entity.WaterGoalEntity
import com.example.waterminder.db.entity.WaterIntakeEntity
import com.example.waterminder.db.entity.WaterLogEntity

@Database(
    entities = [
        UserEntity::class,
        WaterGoalEntity::class,
        WaterIntakeEntity::class,
        WaterLogEntity::class
    ],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun waterGoalDao(): WaterGoalDao
    abstract fun waterIntakeDao(): WaterIntakeDao
    abstract fun waterLogDao(): WaterLogDao
}
