package com.awda.app.data.challenge.converters

import androidx.room.TypeConverter
import com.awda.app.data.challenge.models.DefaultRepetitionPattern

/**
 * Created by Abdelrahman Rizq
 */

class DefaultRepetitionPatternConverter {
    @TypeConverter
    fun fromDefaultRepeatChallenge(value: DefaultRepetitionPattern): String {
        return value.name
    }

    @TypeConverter
    fun toDefaultRepeatChallenge(value: String): DefaultRepetitionPattern {
        return DefaultRepetitionPattern.valueOf(value)
    }
}