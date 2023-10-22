package com.awda.app.data.settings.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.awda.app.data.settings.models.SecureApp
import kotlinx.coroutines.flow.Flow

/**
 * Created by Abdelrahman Rizq
 */

@Dao
interface SecureAppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(app: SecureApp)

    @Delete
    fun delete(app: SecureApp)

    @Query("SELECT * FROM secure_app")
    fun retrieve(): Flow<List<SecureApp>>
}