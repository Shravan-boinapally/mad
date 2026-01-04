package com.example.waterminder.db.modules

import android.content.Context
import androidx.room.Room
import com.example.waterminder.db.AppDatabase

object DatabaseModule {
    private var INSTANCE: AppDatabase? = null

    fun getDb(context: Context): AppDatabase {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "waterminder_db"
            ).build()
        }
        return INSTANCE!!
    }
}
