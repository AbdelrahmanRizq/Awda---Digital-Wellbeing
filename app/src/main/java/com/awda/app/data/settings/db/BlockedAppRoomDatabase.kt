package com.awda.app.data.settings.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.awda.app.data.challenge.converters.CustomRepetitionPatternConverter
import com.awda.app.data.challenge.models.InstalledApp

/**
 * Created by Abdelrahman Rizq
 */

@Database(entities = [InstalledApp::class], version = 1)
@TypeConverters(CustomRepetitionPatternConverter::class)
abstract class BlockedAppRoomDatabase : RoomDatabase() {
    abstract fun dao(): BlockedAppDao

    companion object {
        private var INSTANCE: BlockedAppRoomDatabase? = null

        fun getInstance(applicationContext: Context): BlockedAppRoomDatabase {
            if (INSTANCE == null) {
                synchronized(BlockedAppRoomDatabase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            applicationContext,
                            BlockedAppRoomDatabase::class.java,
                            "blocked_app.dp"
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}