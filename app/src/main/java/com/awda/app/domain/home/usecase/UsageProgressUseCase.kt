package com.awda.app.domain.home.usecase

import com.awda.app.common.parseMillisToFormattedClock
import com.awda.app.data.home.HomeRepository
import com.awda.app.domain.common.usecase.UseCase

/**
 * Created by Abdelrahman Rizq
 */

class UsageProgressUseCase(private val repository: HomeRepository) :
    UseCase<Long, Pair<Float, String>> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(currentScreenTime: Long): Pair<Float, String> {
        val screenTimeLimit = repository.getUsageTimeLimit()
        val progress = (currentScreenTime.toFloat() / screenTimeLimit.toFloat()) * 100f
        val remainder = screenTimeLimit - currentScreenTime
        val text = if (remainder > 0) {
            "${parseMillisToFormattedClock(remainder)} left"
        } else {
            "Time is up!"
        }

        return Pair(progress, text)
    }
}