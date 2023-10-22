package com.awda.app.data.challenge.models

import androidx.room.TypeConverters
import com.awda.app.data.challenge.converters.CustomRepetitionPatternConverter
import com.awda.app.data.challenge.converters.DefaultRepetitionPatternConverter
import java.util.Calendar
import java.util.Locale

/**
 * Created by Abdelrahman Rizq
 */

data class RepetitionPattern(
    val default: DefaultRepetitionPattern = DefaultRepetitionPattern.CUSTOM,
    val custom: List<CustomRepetitionPattern> = emptyList()
)

@TypeConverters(DefaultRepetitionPatternConverter::class)
enum class DefaultRepetitionPattern {
    CUSTOM,
    ONCE,
    DAILY,
    WORKDAYS,
    WEEKENDS;

    fun capitalizedName() =
        name.lowercase(Locale.getDefault()).replaceFirstChar { it.titlecase(Locale.getDefault()) }
}

@TypeConverters(CustomRepetitionPatternConverter::class)
enum class CustomRepetitionPattern(val value: Int) {
    MON(Calendar.MONDAY),
    TUE(Calendar.TUESDAY),
    WED(Calendar.WEDNESDAY),
    THU(Calendar.THURSDAY),
    FRI(Calendar.FRIDAY),
    SAT(Calendar.SATURDAY),
    SUN(Calendar.SUNDAY);

    companion object {
        fun next(
            today: Int,
            daysList: List<CustomRepetitionPattern>
        ): CustomRepetitionPattern {
            val sortedDays = daysList.sortedBy { it.value }
            val nextDay = sortedDays.firstOrNull { it.value > today }
            return nextDay ?: sortedDays.first()
        }
    }

    fun capitalizedName() =
        name.lowercase(Locale.getDefault()).replaceFirstChar { it.titlecase(Locale.getDefault()) }

    fun firstChar() = name.first().uppercaseChar()
}

