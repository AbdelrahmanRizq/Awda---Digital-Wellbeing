package com.awda.app.domain.usage.usecase

import com.awda.app.data.common.models.Resource
import com.awda.app.data.usage.AppUsageRepository
import com.awda.app.domain.common.usecase.AsyncUseCase
import kotlinx.coroutines.flow.first

/**
 * Created by Abdelrahman Rizq
 */

class AppWeekAverageUsagePerDayUseCase(private val repository: AppUsageRepository) :
    AsyncUseCase<String, Resource<Long>> {

    override suspend fun execute(params: String): Resource<Long> {
        return repository.getAppWeekAverageUsagePerDay(params).first()
    }
}