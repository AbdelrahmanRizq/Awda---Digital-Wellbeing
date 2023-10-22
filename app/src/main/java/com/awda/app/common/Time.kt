package com.awda.app.common

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Created by Abdelrahman Rizq
 */

fun dayStart(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar
}

fun dayEnd(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar
}

fun parseClockToTimestamp(hour: Int, minute: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis
}

fun parseMillisToFullClock(millis: Long): String {
    val (hours, minutes) = parseMillisToClock(millis)
    val seconds = ((millis - hours * 60 * 60 * 1000 - minutes * 60 * 1000) / 1000).toInt()

    val formattedTime = StringBuilder()
    if (hours > 0) {
        formattedTime.append("%dh ".format(hours))
    }
    if (minutes > 0) {
        formattedTime.append("%dm ".format(minutes))
    }
    if (seconds > 0) {
        formattedTime.append("%ds".format(seconds))
    }

    if (hours == 0 && minutes == 0 && seconds == 0) {
        return "0s"
    }

    return formattedTime.toString()
}


fun parseMillisToFormattedClock(millis: Long): String {
    val (hours, minutes) = parseMillisToClock(millis)
    return "%dh %dm".format(hours, minutes)
}

fun parseMillisToClock(millis: Long): Pair<Int, Int> {
    val hours = millis / (60 * 60 * 1000)
    val minutes = (millis - hours * 60 * 60 * 1000) / (60 * 1000)

    return Pair(hours.toInt(), minutes.toInt())
}

fun parseClockToMillis(hour: Int, minute: Int): Long {
    val millisInHour = 60 * 60 * 1000L
    val millisInMinute = 60 * 1000L

    return (hour * millisInHour) + (minute * millisInMinute)
}

fun parseTimestampToFormattedClock(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("hh:mm a", Locale.ROOT)
    return formatter.format(date)
}

fun parseTimestampToClock(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("HH:mm", Locale.ROOT)
    return formatter.format(date)
}

fun parseTimestampToDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("dd-MM", Locale.ENGLISH)
    return formatter.format(date)
}

fun parseTimestampToFullDate(timestamp: Long): String {
    val date = Date(timestamp)
    val dayOfWeek = SimpleDateFormat("EEE", Locale.ENGLISH).format(date)
    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(date)

    return "$dayOfWeek $formattedDate"
}

fun parseTimestampToMonth(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMMM", Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}