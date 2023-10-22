package com.awda.app.data.challenge.models

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.awda.app.common.Constants
import com.awda.app.common.dayEnd
import com.awda.app.common.dayStart
import com.awda.app.common.parseTimestampToMonth
import com.awda.app.data.challenge.converters.AppChallengeStateConverter
import com.awda.app.data.challenge.converters.RepetitionPatternConverter
import com.awda.app.data.common.models.AwdaError
import com.awda.app.data.common.models.ErrorTypes
import com.awda.app.domain.receiver.ChallengeAlarmReceiver
import java.lang.Math.random
import java.util.Calendar

/**
 * Created by Abdelrahman Rizq
 */

@TypeConverters(AppChallengeStateConverter::class)
enum class AppChallengeState {
    PENDING,
    RUNNING,
    PASSED,
    LOST
}

@Entity(tableName = "app_challenge")
data class AppChallenge(
    @PrimaryKey var occurrence: Long = 0L,
    var createdAt: Long = 0L,
    @Embedded var app: InstalledApp = InstalledApp(),
    var time: Long = 0L,
    var enabled: Boolean = true,
    @TypeConverters(RepetitionPatternConverter::class) var repetitionPattern: RepetitionPattern = RepetitionPattern(),
    @TypeConverters(AppChallengeStateConverter::class) var state: AppChallengeState = AppChallengeState.PENDING
) {
    fun month() = parseTimestampToMonth(occurrence)

    suspend fun schedule(context: Context, store: suspend (AppChallenge) -> Unit): AwdaError? {
        if (!enabled) {
            return null
        }

        occurrence = nextOccurrence()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = occurrence

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                return AwdaError(
                    type = ErrorTypes.REQUEST_SCHEDULE_PERMISSION_NOT_GRANTED,
                    message = "Permission not granted"
                )
            }
        }

        val intent = Intent(context, ChallengeAlarmReceiver::class.java)
        intent.putExtra(Constants.CHALLENGE_OCCURRENCE, occurrence)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            (random() * 1000000.0).toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "Awda:Challenge"
        )

        try {
            wakeLock.acquire(AlarmManager.INTERVAL_FIFTEEN_MINUTES)

            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(
                    calendar.timeInMillis,
                    pendingIntent
                ),
                pendingIntent
            )

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } finally {
            wakeLock.release()
        }

        store(this)
        return null
    }

    fun nextOccurrence(): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = occurrence

        while (calendar.timeInMillis < dayStart().timeInMillis) {
            calendar.timeInMillis += AlarmManager.INTERVAL_DAY
        }

        when (repetitionPattern.default) {
            DefaultRepetitionPattern.CUSTOM -> {
                if (repetitionPattern.custom.isNotEmpty()) {
                    val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

                    if (repetitionPattern.custom.find { it.value == today } != null && calendar.timeInMillis in System.currentTimeMillis()..dayEnd().timeInMillis) {
                        return calendar.timeInMillis
                    }

                    val nextDay =
                        CustomRepetitionPattern.next(today, repetitionPattern.custom)
                    if (nextDay.value < today) {
                        calendar.timeInMillis += 7 * AlarmManager.INTERVAL_DAY
                    }

                    val customRepetitionCalendar = Calendar.getInstance().apply {
                        timeInMillis = calendar.timeInMillis
                        set(Calendar.DAY_OF_WEEK, nextDay.value)
                    }

                    return customRepetitionCalendar.timeInMillis
                } else {
                    return calendar.timeInMillis
                }
            }

            DefaultRepetitionPattern.ONCE -> {
                if (calendar.timeInMillis in System.currentTimeMillis()..dayEnd().timeInMillis) {
                    return calendar.timeInMillis
                }
                return calendar.timeInMillis + AlarmManager.INTERVAL_DAY
            }

            DefaultRepetitionPattern.DAILY -> {
                if (calendar.timeInMillis in System.currentTimeMillis()..dayEnd().timeInMillis) {
                    return calendar.timeInMillis
                }

                return calendar.timeInMillis + AlarmManager.INTERVAL_DAY
            }

            DefaultRepetitionPattern.WORKDAYS -> {
                return when (calendar.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.FRIDAY -> {
                        if (calendar.timeInMillis in System.currentTimeMillis()..dayEnd().timeInMillis) {
                            return calendar.timeInMillis
                        }

                        calendar.timeInMillis + (3 * AlarmManager.INTERVAL_DAY)
                    }

                    Calendar.SATURDAY -> {
                        calendar.timeInMillis + (2 * AlarmManager.INTERVAL_DAY)
                    }

                    else -> {
                        if (
                            calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY &&
                            calendar.timeInMillis in System.currentTimeMillis()..dayEnd().timeInMillis
                        ) {
                            return calendar.timeInMillis
                        }

                        calendar.timeInMillis + AlarmManager.INTERVAL_DAY
                    }
                }
            }

            DefaultRepetitionPattern.WEEKENDS -> {
                return when (calendar.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.SUNDAY -> {
                        if (calendar.timeInMillis in System.currentTimeMillis()..dayEnd().timeInMillis) {
                            return calendar.timeInMillis
                        }

                        calendar.timeInMillis + (6 * AlarmManager.INTERVAL_DAY)
                    }

                    Calendar.MONDAY -> {
                        calendar.timeInMillis + (5 * AlarmManager.INTERVAL_DAY)
                    }

                    Calendar.TUESDAY -> {
                        calendar.timeInMillis + (4 * AlarmManager.INTERVAL_DAY)
                    }

                    Calendar.WEDNESDAY -> {
                        calendar.timeInMillis + (3 * AlarmManager.INTERVAL_DAY)
                    }

                    Calendar.THURSDAY -> {
                        calendar.timeInMillis + (2 * AlarmManager.INTERVAL_DAY)
                    }

                    else -> {
                        if (
                            calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY &&
                            calendar.timeInMillis in System.currentTimeMillis()..dayEnd().timeInMillis
                        ) {
                            return calendar.timeInMillis
                        }

                        calendar.timeInMillis + AlarmManager.INTERVAL_DAY
                    }
                }
            }
        }
    }
}
