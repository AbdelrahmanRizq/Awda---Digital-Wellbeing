package com.awda.app.data.challenge.converters

import androidx.room.TypeConverter
import com.awda.app.data.challenge.models.CustomRepetitionPattern

/**
 * Created by Abdelrahman Rizq
 */

class CustomRepetitionPatternConverter {
    @TypeConverter
    fun fromCustomRepeatChallengeList(value: List<CustomRepetitionPattern>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toCustomRepeatChallengeList(value: String): List<CustomRepetitionPattern> {
        return value.split(",").mapNotNull {
            try {
                CustomRepetitionPattern.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}