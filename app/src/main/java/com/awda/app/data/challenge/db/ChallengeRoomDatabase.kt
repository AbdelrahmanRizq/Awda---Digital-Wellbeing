package com.awda.app.data.challenge.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.awda.app.data.challenge.converters.AppChallengeStateConverter
import com.awda.app.data.challenge.converters.CustomRepetitionPatternConverter
import com.awda.app.data.challenge.converters.DefaultRepetitionPatternConverter
import com.awda.app.data.challenge.converters.RepetitionPatternConverter
import com.awda.app.data.challenge.models.AppChallenge

/**
 * Created by Abdelrahman Rizq
 */

@Database(entities = [AppChallenge::class], version = 1)
@TypeConverters(
    RepetitionPatternConverter::class,
    AppChallengeStateConverter::class,
    DefaultRepetitionPatternConverter::class,
    CustomRepetitionPatternConverter::class
)
abstract class ChallengeRoomDatabase : RoomDatabase() {
    abstract fun dao(): ChallengeDao

    companion object {
        private var INSTANCE: ChallengeRoomDatabase? = null

        fun getInstance(applicationContext: Context): ChallengeRoomDatabase {
            if (INSTANCE == null) {
                synchronized(ChallengeRoomDatabase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            applicationContext,
                            ChallengeRoomDatabase::class.java,
                            "challenge.dp"
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}