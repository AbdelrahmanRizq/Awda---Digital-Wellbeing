package com.awda.app.domain.common.models

import java.util.concurrent.TimeUnit

/**
 * Created by Abdelrahman Rizq
 */

data class TimeRange(
    val start: Long,
    val end: Long
) {
    fun requiresCache(): Boolean {
        val difference = end - start
        val millisPerDay = TimeUnit.DAYS.toMillis(1)
        return difference > (8 * millisPerDay)
    }
}
