package com.awda.app.data.home.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.awda.app.data.home.models.AppUsage

/**
 * Created by Abdelrahman Rizq
 */

@Database(entities = [AppUsage::class], version = 1)
abstract class AppUsageRoomDatabase : RoomDatabase() {

    abstract fun dao(): AppUsageDao

    companion object {
        private var INSTANCE: AppUsageRoomDatabase? = null

        fun getInstance(applicationContext: Context): AppUsageRoomDatabase {
            if (INSTANCE == null) {
                synchronized(AppUsageRoomDatabase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            applicationContext,
                            AppUsageRoomDatabase::class.java,
                            "app_usage.dp"
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}