package com.awda.app.domain.usage.usecase

import co.yml.charts.ui.barchart.models.BarData
import com.awda.app.data.common.models.Resource
import com.awda.app.data.usage.AppUsageRepository
import com.awda.app.domain.common.models.ChartTimeBase
import com.awda.app.domain.common.usecase.AsyncUseCase
import kotlinx.coroutines.flow.first

/**
 * Created by Abdelrahman Rizq
 */

class AppUsageChartUseCase(private val repository: AppUsageRepository) :
    AsyncUseCase<Pair<String, ChartTimeBase>, Resource<List<BarData>>> {
    override suspend fun execute(params: Pair<String, ChartTimeBase>): Resource<List<BarData>> {
        return repository.getAppUsageChartData(params.first, params.second).first()
    }
}