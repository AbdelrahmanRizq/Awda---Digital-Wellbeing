package com.awda.app.data.challenge.converters

import androidx.room.TypeConverter
import com.awda.app.data.challenge.models.AppChallengeState

/**
 * Created by Abdelrahman Rizq
 */

class AppChallengeStateConverter {
    @TypeConverter
    fun fromAppChallengeState(value: AppChallengeState): String {
        return value.name
    }

    @TypeConverter
    fun toAppChallengeState(value: String): AppChallengeState {
        return AppChallengeState.valueOf(value)
    }
}