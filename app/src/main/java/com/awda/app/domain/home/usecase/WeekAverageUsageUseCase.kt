package com.awda.app.domain.home.usecase

import com.awda.app.data.common.models.Resource
import com.awda.app.data.home.HomeRepository
import com.awda.app.domain.common.usecase.AsyncUseCase
import kotlinx.coroutines.flow.first

/**
 * Created by Abdelrahman Rizq
 */

class WeekAverageUsageUseCase(private val repository: HomeRepository) :
    AsyncUseCase<Unit, Resource<Long>> {

    override suspend fun execute(params: Unit): Resource<Long> {
        return repository.getWeekAverageUsage().first()
    }
}