package com.awda.app.data.settings.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.awda.app.data.challenge.models.InstalledApp
import kotlinx.coroutines.flow.Flow

/**
 * Created by Abdelrahman Rizq
 */

@Dao
interface BlockedAppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(app: InstalledApp)

    @Delete
    fun delete(app: InstalledApp)

    @Query("SELECT * FROM blocked_app")
    fun retrieve(): Flow<List<InstalledApp>>
}