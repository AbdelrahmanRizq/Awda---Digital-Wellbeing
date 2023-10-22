package com.awda.app.data.home.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.awda.app.data.home.models.AppUsage
import kotlinx.coroutines.flow.Flow

/**
 * Created by Abdelrahman Rizq
 */

@Dao
interface AppUsageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(appUsage: List<AppUsage>)

    @Query("SELECT * FROM app_usage")
    fun retrieve(): Flow<List<AppUsage>>
}