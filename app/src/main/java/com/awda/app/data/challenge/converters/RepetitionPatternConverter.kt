package com.awda.app.data.challenge.converters

import androidx.room.TypeConverter
import com.awda.app.data.challenge.models.RepetitionPattern
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Abdelrahman Rizq
 */

class RepetitionPatternConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromRepeatChallenge(value: RepetitionPattern): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toRepeatChallenge(value: String): RepetitionPattern {
        val type = object : TypeToken<RepetitionPattern>() {}.type
        return gson.fromJson(value, type)
    }
}