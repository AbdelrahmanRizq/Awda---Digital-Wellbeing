package com.awda.app.domain.home.usecase

import com.awda.app.data.common.models.Resource
import com.awda.app.data.home.HomeRepository
import com.awda.app.data.home.models.CombinedAppUsage
import com.awda.app.domain.common.models.TimeRange
import com.awda.app.domain.common.usecase.AsyncUseCase
import kotlinx.coroutines.flow.first

/**
 * Created by Abdelrahman Rizq
 */

class AppUsageUseCase(private val repository: HomeRepository) :
    AsyncUseCase<TimeRange, Resource<List<CombinedAppUsage>>> {

    override suspend fun execute(params: TimeRange): Resource<List<CombinedAppUsage>> {
        return repository.getAppUsage(params).first()
    }
}