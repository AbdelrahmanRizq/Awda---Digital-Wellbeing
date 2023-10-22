package com.awda.app.data.challenge.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.awda.app.data.challenge.models.AppChallenge
import kotlinx.coroutines.flow.Flow

/**
 * Created by Abdelrahman Rizq
 */

@Dao
interface ChallengeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(challenge: AppChallenge)

    @Update
    fun update(challenge: AppChallenge)

    @Delete
    fun delete(challenge: AppChallenge)

    @Query("SELECT * FROM app_challenge")
    fun retrieve(): Flow<List<AppChallenge>>
}