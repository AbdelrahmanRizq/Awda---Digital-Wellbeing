package com.awda.app.data.settings.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.awda.app.data.challenge.converters.CustomRepetitionPatternConverter
import com.awda.app.data.settings.models.SecureApp

/**
 * Created by Abdelrahman Rizq
 */

@Database(entities = [SecureApp::class], version = 1)
@TypeConverters(CustomRepetitionPatternConverter::class)
abstract class SecureAppRoomDatabase : RoomDatabase() {
    abstract fun dao(): SecureAppDao

    companion object {
        private var INSTANCE: SecureAppRoomDatabase? = null

        fun getInstance(applicationContext: Context): SecureAppRoomDatabase {
            if (INSTANCE == null) {
                synchronized(SecureAppRoomDatabase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            applicationContext,
                            SecureAppRoomDatabase::class.java,
                            "secure_app.dp"
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}